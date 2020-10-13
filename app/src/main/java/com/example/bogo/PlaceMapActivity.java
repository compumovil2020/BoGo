package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PlaceMapActivity extends AppCompatActivity {
    Button btnCarOption, btnBusOption, btnWalkOption, btnGo;
    TextView txtRouteInfo;
    LinearLayout layRuta;
    MapView mMap;
    IMapController mapController;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private LocationRequest mLocationRequest = null;
    private LocationCallback mLocationCallback = null;
    GeoPoint actual, ubicacion;
    int actualIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);

        btnCarOption = findViewById(R.id.btnCarOption);
        btnBusOption = findViewById(R.id.btnBusOption);
        btnWalkOption = findViewById(R.id.btnWalOption);
        btnGo = findViewById(R.id.btnGoPlaceMap);
        txtRouteInfo = findViewById(R.id.txtRouteInfo);
        layRuta = findViewById(R.id.layRuta);


        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        PermissionsManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder cargar el mapa", PermissionsManager.READ_STORAGE_PERMISSION);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            initMap();
        }

    }

    private void initMap() {
        ubicacion = new GeoPoint(4.618534, -74.068002);
        mMap = findViewById(R.id.openmapview);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mapController = mMap.getController();
        mMap.setMultiTouchControls(true);
        Marker placeMarker = new Marker(mMap);
        placeMarker.setIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.btnpincho));
        placeMarker.setPosition(ubicacion);
        placeMarker.setTitle("Lugar seleccionado");
        placeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMap.getOverlays().add(placeMarker);
        mapController.setZoom(20.0);
        mapController.setCenter(ubicacion);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                        locationMarker.setIcon(ContextCompat.getDrawable(getBaseContext(),R.drawable.btnpincho));
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
                        locationMarker.setIcon(getDrawable(R.drawable.btnpincho));
                        locationMarker.setPosition(actual);
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

        final RouteManager routeManager = new RouteManager();

        btnCarOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeManager.getRoad(RouteManager.CAR_OPTION);
            }
        });

        btnBusOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeManager.getRoad(RouteManager.BIKE_OPTION);
            }
        });

        btnWalkOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeManager.getRoad(RouteManager.WALK_OPTION);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeManager.drawMap();
            }
        });
    }

    public void buildMap(Road road)
    {
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        roadOverlay.setWidth(14);
        roadOverlay.setColor(Color.argb(255, 0, 0, 255));
        BoundingBox box = computeArea(new GeoPoint[]{ubicacion, actual});
        mMap.zoomToBoundingBox(box, true, 200);
        mMap.getOverlays().add(roadOverlay);
        mMap.invalidate();
    }

    public BoundingBox computeArea(GeoPoint[] points) {

        double north = 0, south = 0, west = 0, east = 0;

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) continue;

            double lat = points[i].getLatitude();
            double lon = points[i].getLongitude();

            if ((i == 0) || (lat > north)) north = lat;
            if ((i == 0) || (lat < south)) south = lat;
            if ((i == 0) || (lon < west)) west = lon;
            if ((i == 0) || (lon > east)) east = lon;

        }

        return new BoundingBox(north, east, south, west);
    }

    private void usePermission()
    {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            checkSettings();
        }
    }

    private void checkSettings()
    {
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
                            resolvable.startResolutionForResult(PlaceMapActivity.this ,PermissionsManager.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:{
                        Toast.makeText(getBaseContext(),"No se pudo activar la localización",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PermissionsManager.REQUEST_CHECK_SETTINGS && resultCode==RESULT_OK){
            startLocationUpdates();
        }else
        {
            Toast.makeText(this, "Sin acceso a localización, hardware deshabilitado!", Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates()
    {
        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
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

    private void stopLocationUpdates()
    {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mFusedLocationProviderClient != null)
            stopLocationUpdates();
    }

    class RouteManager
    {
        public static final int CAR_OPTION = 1;
        public static final int BIKE_OPTION = 2;
        public static final int WALK_OPTION = 3;

        Road roadFinal;
        GetRoad roadGetter;
        ArrayList<GeoPoint> waypoints;

        public RouteManager()
        {
            roadFinal = null;
            roadGetter = null;
        }

        public void getRoad(int option)
        {
            switch (option)
            {
                case CAR_OPTION:
                {
                    if(actual != null) {
                        RoadManager roadManager = new MapQuestRoadManager("RuY1fPXtqJ54yszAjRfkGYBaAAYaaPwk");
                        roadManager.addRequestOption("unit=k");

                        waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(actual);
                        waypoints.add(ubicacion);

                        roadGetter = new GetRoad();
                        roadGetter.execute(roadManager);
                    } else Toast.makeText(getBaseContext(),"Ubicación del usuario desconocida",Toast.LENGTH_LONG).show();
                    break;
                }
                case BIKE_OPTION:
                {
                    if(actual != null) {
                        RoadManager roadManager = new MapQuestRoadManager("RuY1fPXtqJ54yszAjRfkGYBaAAYaaPwk");
                        roadManager.addRequestOption("routeType=bicycle");
                        roadManager.addRequestOption("unit=k");

                        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(actual);
                        waypoints.add(ubicacion);

                        roadGetter = new GetRoad();
                        roadGetter.execute(roadManager);
                    } else Toast.makeText(getBaseContext(),"Ubicación del usuario desconocida",Toast.LENGTH_LONG).show();
                    break;
                }
                case WALK_OPTION:
                {
                    if(actual != null) {
                        RoadManager roadManager = new MapQuestRoadManager("RuY1fPXtqJ54yszAjRfkGYBaAAYaaPwk");
                        roadManager.addRequestOption("routeType=pedestrian");
                        roadManager.addRequestOption("unit=k");

                        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(actual);
                        waypoints.add(ubicacion);

                        roadGetter = new GetRoad();
                        roadGetter.execute(roadManager);
                    } else Toast.makeText(getBaseContext(),"Ubicación del usuario desconocida",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

        public void drawMap()
        {
            if(this.roadGetter != null)
            {
                if(this.roadFinal != null)
                {
                    layRuta.setVisibility(View.INVISIBLE);
                    buildMap(this.roadFinal);
                }
                else roadGetter.drawMap = true;
            }
            else Toast.makeText(getBaseContext(),"Seleccione algún medio para transportarse",Toast.LENGTH_LONG).show();
        }

        class GetRoad extends AsyncTask<RoadManager, Void, Road>
        {
            private Exception exception;
            boolean drawMap = false;

            protected Road doInBackground(RoadManager... roadManagers) {
                Road road = null;
                try {
                    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                    waypoints.add(actual);
                    waypoints.add(ubicacion);

                    road = roadManagers[0].getRoad(waypoints);
                } catch (Exception e) {
                    exception = e;
                    e.printStackTrace();
                }
                return road;
            }

            protected void onPostExecute(Road road) {
                roadFinal = road;
                int dur = (int) Math.ceil(road.mDuration/60);
                BigDecimal dis = BigDecimal.valueOf(road.mLength);
                dis = dis.setScale(2, RoundingMode.HALF_UP);
                int h = 0, m = 0;
                if(dur >= 60)
                {
                    h = dur/60;
                    m = dur%60;
                }
                String info = "";
                if(h > 0)
                    info = h + " h " + m + " min" + " (" + dis + " km"+")";
                else
                    info = dur + " min" + " (" + dis + " km"+")";
                txtRouteInfo.setText(info);
                if(drawMap)
                    drawMap();
            }
        }
    }
}