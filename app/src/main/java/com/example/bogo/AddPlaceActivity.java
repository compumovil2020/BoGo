package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bogo.Utils.PermissionsManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddPlaceActivity extends Fragment {

    Button btnGo;
    EditText editTextNombreLugar;
    Spinner spinnerTipo;
    EditText editTextRangoPrecio;
    EditText editTextTimeHoraApertura;
    EditText editTextTimeHoraCierre;
    EditText editTextDireccion;
    Button btnAddFoto, btnAddCamera;
    TextView textFotosAgregadas;
    CustomMapView mMap;
    IMapController mapController;
    GeoPoint locationSelected = null;
    View view = null;
    ImageView imgResultado;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_place, container, false);
        imgResultado = view.findViewById(R.id.imgAddPlace);
        btnGo = view.findViewById(R.id.btnGo);
        editTextNombreLugar = view.findViewById(R.id.editTextNombreLugar);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        editTextRangoPrecio = view.findViewById(R.id.editTextRangoPrecio);
        editTextTimeHoraApertura = view.findViewById(R.id.editTextTimeHoraApertura);
        editTextTimeHoraCierre = view.findViewById(R.id.editTextTimeHoraCierre);
        editTextDireccion = view.findViewById(R.id.editTextDireccion);
        btnAddFoto = view.findViewById(R.id.btnAddFoto);
        btnAddCamera = view.findViewById(R.id.btnAddCamera);
        textFotosAgregadas = view.findViewById(R.id.textFotosAgregadas);

        final Context ctx = view.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        PermissionsManager.requestPermission( this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder cargar el mapa", PermissionsManager.READ_STORAGE_PERMISSION);

        if(ContextCompat.checkSelfPermission( this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            initializeMap();
        }

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),DropMenuActivity.class);
                startActivity(i);
            }
        });

        btnAddFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoFromGallery();
            }
        });

        btnAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoFromCamera();
            }
        });

        return view;
    }

    private void addPhotoFromCamera()
    {
        PermissionsManager.requestPermission(this.getActivity(), Manifest.permission.CAMERA,
                "Para poder mostrar fotos tomadas desde su cámara", PermissionsManager.CAMERA_PERMISSION );
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PermissionsManager.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void addPhotoFromGallery()
    {
        PermissionsManager.requestPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder mostrar fotos que ya tenga guardadas", PermissionsManager.READ_STORAGE_PERMISSION);
        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, PermissionsManager.IMAGE_PICKER_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionsManager.READ_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addPhotoFromGallery();
                } else {
                    //imgResultado.setImageResource(R.drawable.deniedpermission);
                }
                return;
            }
            case PermissionsManager.CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    addPhotoFromCamera();
                }
                else
                {
                    //imgResultado.setImageResource(R.drawable.deniedpermission);
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case PermissionsManager.REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    textFotosAgregadas.setText("Imagen agregada!");
                    imgResultado.setImageBitmap(imageBitmap);
                    break;
                case PermissionsManager.IMAGE_PICKER_REQUEST:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = this.getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgResultado.setImageBitmap(selectedImage);
                        textFotosAgregadas.setText("Imagen agregada!");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void initializeMap()
    {
        GeoPoint bogota = new GeoPoint(4.6471425,-74.0917152);
        mMap =  this.view.findViewById(R.id.addPlaceMapa);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mapController = mMap.getController();
        mMap.setMultiTouchControls(false);

        mapController.setZoom(13.0);
        mapController.setCenter(bogota);

        final MapEventsReceiver mReceive = new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                if(locationSelected != null)
                    mMap.getOverlays().remove(mMap.getOverlays().size()-1);
                locationSelected = p;
                Marker placeMarker = new Marker(mMap);
                placeMarker.setIcon(view.getContext().getDrawable(R.drawable.btnpincho));
                placeMarker.setPosition(p);
                placeMarker.setTitle("Lugar seleccionado");
                placeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mMap.getOverlays().add(placeMarker);
                return false;
            }
        };
        mMap.getOverlays().add(new MapEventsOverlay(mReceive));
    }
}