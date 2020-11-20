package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.example.bogo.BuildConfig;
import com.example.bogo.Entidades.Usuario;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;

public class FriendMapActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;
    MapView mMap;
    IMapController mapController;
    SensorEventListener lightSensorListener;
    GeoPoint ubactual, ubamigo = null;
    int actualIndex, actualIndexFriend;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String friendUID;
    ValueEventListener friendListener;
    Marker locationMarker, friendMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_map);

        final Context ctx = getApplicationContext();
        Intent intent = getIntent();
        friendUID = intent.getStringExtra("idamigo");

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        PermissionsManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                "Es necesario para visualizar el mapa.", PermissionsManager.READ_STORAGE_PERMISSION);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    initMap();
            }


    }

    private void initMap() {
        mMap = findViewById(R.id.osmView);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mapController = mMap.getController();
        mMap.setMultiTouchControls(true);

        String myUID = mAuth.getUid();
        myRef = database.getReference(Utils.PATH_OBSERVADORES + friendUID + "/" + myUID);
        myRef.setValue( true );

        myRef = database.getReference(Utils.PATH_USUARIOS + friendUID );
        Log.i("LOGLOG", Utils.PATH_USUARIOS + friendUID);
        friendListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario friend = snapshot.getValue(Usuario.class);
                if(ubamigo != null)
                {
                    mMap.getOverlays().remove(friendMarker);
                    mMap.invalidate();
                    ubamigo.setLatitude(friend.getLatitud());
                    ubamigo.setLongitude(friend.getLongitud());
                    friendMarker = new Marker(mMap);
                    friendMarker.setIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_btnpinchofriend));
                    friendMarker.setPosition(ubamigo);
                    friendMarker.setTitle(friend.getNombre());
                    friendMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMap.getOverlays().add(friendMarker);
                    actualIndexFriend = mMap.getOverlays().size()-1;
                }
                else
                {
                    ubamigo = new GeoPoint(friend.getLatitud(), friend.getLongitud());
                    friendMarker = new Marker(mMap);
                    friendMarker.setIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_btnpinchofriend));
                    friendMarker.setPosition(ubamigo);
                    friendMarker.setTitle(friend.getNombre());
                    friendMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMap.getOverlays().add(friendMarker);
                    mapController.setZoom(13.0);
                    mapController.setCenter(ubamigo);
                    actualIndexFriend = mMap.getOverlays().size()-1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addValueEventListener(friendListener);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(mMap != null) {
                    if(sensorEvent.values[0] < 10) {
                        mMap.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                    }else {
                        mMap.getOverlayManager().getTilesOverlay().setColorFilter(null);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();


        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if(location!=null) {
                    if(ubactual==null) {
                        ubactual = new GeoPoint(location);
                        locationMarker = new Marker(mMap);
                        locationMarker.setIcon(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_btnpinchoyo));
                        locationMarker.setPosition(ubactual);
                        locationMarker.setTitle("Ubicación actual");
                        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMap.getOverlays().add(locationMarker);
                        actualIndex = mMap.getOverlays().size()-1;
                    }else {
                        mMap.getOverlays().remove(locationMarker);
                        mMap.invalidate();
                        ubactual.setLatitude(location.getLatitude());
                        ubactual.setLongitude(location.getLongitude());
                        locationMarker = new Marker(mMap);
                        locationMarker.setIcon(getDrawable(R.drawable.ic_btnpinchoyo));
                        locationMarker.setPosition(ubactual);
                        locationMarker.setTitle("Ubicación actual");
                        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMap.getOverlays().add(locationMarker);
                        actualIndex = mMap.getOverlays().size()-1;
                    }
                }
            }
        };

        PermissionsManager.requestPermission(this,Manifest.permission.ACCESS_FINE_LOCATION,"Es necesario para tener la ubicación del usuario",PermissionsManager.LOCATION_PERMISSION);
        usePermission();
    }

    private void usePermission() {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            checkSettings();
        }
    }

    private void checkSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client =  LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode){
                    case CommonStatusCodes.RESOLUTION_REQUIRED: {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(FriendMapActivity.this ,PermissionsManager.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:{
                        Toast.makeText(getBaseContext(),"No se pudo encender la localización.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PermissionsManager.REQUEST_CHECK_SETTINGS && resultCode==RESULT_OK){
            startLocationUpdates();
        }else {
            Toast.makeText(this, "Sin acceso a localización, hardware deshabilitado!", Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates() {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PermissionsManager.READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMap();
                } else {
                    Toast.makeText(this, "Sin acceso, mapa no disponible!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        if(sensorManager != null)
            sensorManager.registerListener(lightSensorListener,lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mFusedLocationProviderClient != null)
            stopLocationUpdates();
        if(sensorManager != null)
            sensorManager.unregisterListener(lightSensorListener);
        if(friendListener != null)
            myRef.removeEventListener(friendListener);
        String myUID = mAuth.getUid();
        myRef = database.getReference(Utils.PATH_OBSERVADORES + friendUID + "/" + myUID);
        myRef.removeValue();
    }

}