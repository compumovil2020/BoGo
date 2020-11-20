package com.example.bogo.Services;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.bogo.Activities.ChatActivity;
import com.example.bogo.Activities.FriendMapActivity;
import com.example.bogo.Activities.MainMenu;
import com.example.bogo.Entidades.Mensaje;
import com.example.bogo.R;
import com.example.bogo.Utils.PermissionsManager;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class LocationJobIntentService extends JobIntentService {

    static final int JOB_ID = 1001;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;

    private Location lastLocation = null;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();


        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if(location!=null)
                {
                    if( lastLocation == null || Utils.distance(location.getLatitude(), location.getLongitude(),
                            lastLocation.getLatitude(), lastLocation.getLongitude() ) > 0.002 )
                    {
                        myRef = database.getReference( Utils.PATH_USUARIOS + mAuth.getUid()+"/latitud");
                        myRef.setValue(location.getLatitude());
                        myRef = database.getReference( Utils.PATH_USUARIOS + mAuth.getUid()+"/longitud");
                        myRef.setValue(location.getLongitude());
                    }
                    lastLocation = location;
                }
            }
        };


    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        myRef = database.getReference(Utils.PATH_OBSERVADORES + mAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null)
                {
                    if(snapshot.getValue() != null)
                    {
                        usePermission();
                    }
                    else
                    {
                        stopLocationUpdates();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void usePermission() {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }



    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void startLocationUpdates() {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                Toast.makeText(LocationJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
