package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.BuildConfig;
import com.example.bogo.R;
import com.example.bogo.Utils.PermissionsManager;
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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;

public class FullMapActivity extends Fragment {

    MapView mMap;
    IMapController mapController;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;
    GeoPoint actual, ubicacion;
    int actualIndex;
    View view = null;
    Activity act = null;

    String Nombres[] = {"Factory Steak & Lobster Bogotá", "Santa Fe Restaurante", "Restaurante Peruano - El Indio de Machu Picchu", "Los Galenos Restaurante", "Restaurante Black Bear", "El irreverente Bogota"};
    double Lat[] = {4.6594548, 4.6118874, 4.6567465, 4.6803475, 4.672014, 4.6963027};
    double Lon[] = {-74.1089402, -74.0666129, -74.0574461, -74.0575475, -74.049693, -74.0290333};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_full_map, container, false);
        act = this.getActivity();
        final Context ctx = view.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        PermissionsManager.requestPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder cargar el mapa", PermissionsManager.READ_STORAGE_PERMISSION);
        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            initMap();
        }

        return view;
    }

    private void initMap() {
        mMap = this.view.findViewById(R.id.openmapviewfullmap);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mapController = mMap.getController();
        mMap.setMultiTouchControls(true);

        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        for(int i = 0; i<Nombres.length; i++){
            OverlayItem nuevo = new OverlayItem(Nombres[i], "", new GeoPoint(Lat[i], Lon[i]));
            nuevo.setMarker(ContextCompat.getDrawable(this.view.getContext(), R.drawable.btnpincho));
            items.add(nuevo); // Lat/Lon decimal degrees
        }

//the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, view.getContext());
        mOverlay.setFocusItemsOnTap(true);

        mMap.getOverlays().add(mOverlay);
        /*
        Marker placeMarker = new Marker(mMap);
        placeMarker.setIcon(ContextCompat.getDrawable(this.view.getContext(), R.drawable.btnpincho));
        placeMarker.setPosition(ubicacion);
        placeMarker.setTitle("Lugar seleccionado");
        placeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMap.getOverlays().add(placeMarker);
         */

        mapController.setCenter(new GeoPoint(4.648961,-74.0943807));
        mapController.setZoom(13.0);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        mLocationRequest = createLocationRequest();


        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if(location!=null)
                {
                    if(actual==null)
                    {
                        actual = new GeoPoint(location);
                        Marker locationMarker = new Marker(mMap);
                        locationMarker.setIcon(ContextCompat.getDrawable(view.getContext(),R.drawable.btnpincho));
                        locationMarker.setPosition(actual);
                        locationMarker.setTitle("Ubicación actual");
                        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMap.getOverlays().add(locationMarker);
                        actualIndex = mMap.getOverlays().size()-1;
                    }else
                    {
                        mMap.getOverlays().remove(actualIndex);
                        actual.setLatitude(location.getLatitude());
                        actual.setLongitude(location.getLongitude());
                        Marker locationMarker = new Marker(mMap);
                        locationMarker.setIcon(view.getContext().getDrawable(R.drawable.btnpincho));
                        locationMarker.setPosition(actual);
                        locationMarker.setTitle("Ubicación actual");
                        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMap.getOverlays().add(locationMarker);
                        actualIndex = mMap.getOverlays().size()-1;
                    }
                }
            }
        };

        PermissionsManager.requestPermission(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION,"Es necesario para tener la ubicación del usuario",PermissionsManager.LOCATION_PERMISSION);
        usePermission();
        //this.mMap.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void usePermission()
    {
        if(ContextCompat.checkSelfPermission(this.view.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            checkSettings();
        }
    }

    private void checkSettings()
    {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client =  LocationServices.getSettingsClient(this.getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this.getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(act, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode){
                    case CommonStatusCodes.RESOLUTION_REQUIRED: {
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(act,PermissionsManager.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:{
                        Toast.makeText(view.getContext(),"No se pudo activar la localización",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PermissionsManager.REQUEST_CHECK_SETTINGS && resultCode==RESULT_OK){
            startLocationUpdates();
        }else
        {
            Toast.makeText(act, "Sin acceso a localización, hardware deshabilitado!", Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates()
    {
        if(ContextCompat.checkSelfPermission(this.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
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
                    Toast.makeText(act, "Sin acceso, mapa no disponible!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private void stopLocationUpdates()
    {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mFusedLocationProviderClient != null)
            stopLocationUpdates();
    }

}