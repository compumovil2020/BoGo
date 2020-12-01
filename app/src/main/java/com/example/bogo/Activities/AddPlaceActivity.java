package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bogo.Adapters.AddFriendAdapter;
import com.example.bogo.BuildConfig;
import com.example.bogo.Entidades.Lugar;
import com.example.bogo.R;
import com.example.bogo.Utils.PermissionsManager;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddPlaceActivity extends Fragment {

    Button btnGo;
    EditText editTextNombreLugar;
    Spinner spinnerTipo;
    EditText editTextPrecioMinimo, editTextPrecioMaximo;
    TimePicker editTextTimeHoraApertura, editTextTimeHoraCierre;
    EditText editTextDireccion;
    EditText editTextDescripcion;
    EditText editTextTelefono;
    EditText editTextCorreo;
    Button btnAddFoto, btnAddCamera;
    TextView textFotosAgregadas;

    CustomMapView mMap;
    IMapController mapController;
    GeoPoint locationSelected = null;
    View view = null;
    ImageView imgResultado;

    Uri miImagenUri;
    byte[] miImagenBit;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_place, container, false);
        imgResultado = view.findViewById(R.id.imgAddPlace);
        btnGo = view.findViewById(R.id.btnGo);
        editTextNombreLugar = view.findViewById(R.id.editTextNombreLugar);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        editTextPrecioMinimo = view.findViewById(R.id.editTextPrecioMinimo);
        editTextPrecioMaximo = view.findViewById(R.id.editTextPrecioMaximo);
        editTextTimeHoraApertura = view.findViewById(R.id.editTextTimeHoraApertura);
        editTextTimeHoraCierre = view.findViewById(R.id.editTextTimeHoraCierre);
        editTextDireccion = view.findViewById(R.id.editTextDireccion);
        editTextDescripcion = view.findViewById(R.id.edtDescription);
        editTextTelefono = view.findViewById(R.id.edtTelefono);
        editTextCorreo = view.findViewById(R.id.edtCorreo);
        btnAddFoto = view.findViewById(R.id.btnAddFoto);
        btnAddCamera = view.findViewById(R.id.btnAddCamera);
        textFotosAgregadas = view.findViewById(R.id.textFotosAgregadas);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        editTextTimeHoraApertura.setIs24HourView(true);
        editTextTimeHoraCierre.setIs24HourView(true);

        final Context ctx = view.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        PermissionsManager.requestPermissionforFragment( this, this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder cargar el mapa", PermissionsManager.READ_STORAGE_PERMISSION_FOR_MAP);

        if(ContextCompat.checkSelfPermission( this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            initializeMap();
        }

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()) {
                    createPlace();
                }
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

        editTextDireccion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH)
                {
                    String direccion = editTextDireccion.getText().toString();

                    if(!direccion.isEmpty())
                    {
                        getDirectionFromGeocoderNominatim(direccion);
                    }
                    else
                    {
                        editTextDireccion.setError("Required");
                    }
                }
                return false;
            }
        });

        editTextNombreLugar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH)
                {
                    String direccion = editTextNombreLugar.getText().toString();

                    if(!direccion.isEmpty())
                    {
                        getDirectionFromGeocoderNominatim(direccion);
                    }
                    else
                    {
                        editTextNombreLugar.setError("Required");
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void createPlace()
    {
        Lugar lugar = new Lugar();
        lugar.setCorreoElectronico(editTextCorreo.getText().toString());
        lugar.setDescripcion(editTextDescripcion.getText().toString());
        lugar.setDireccion(editTextDireccion.getText().toString());
        Calendar calApertura = Calendar.getInstance();
        calApertura.set(0,0,0,editTextTimeHoraApertura.getHour(),editTextTimeHoraApertura.getMinute());
        String dateApertura = DateFormat.format("hh:mm a", calApertura).toString();
        lugar.setHoraApertura(dateApertura);
        Calendar calCierre = Calendar.getInstance();
        calCierre.set(0,0,0,editTextTimeHoraCierre.getHour(),editTextTimeHoraCierre.getMinute());
        String dateCierre = DateFormat.format("hh:mm a", calCierre).toString();
        lugar.setHoraCierre(dateCierre);
        lugar.setLatitud(locationSelected.getLatitude());
        lugar.setLongitud(locationSelected.getLongitude());
        lugar.setNombre(editTextNombreLugar.getText().toString());
        lugar.setPromedio(0);
        lugar.setTelefono(Long.parseLong(editTextTelefono.getText().toString()));
        lugar.setTipo(spinnerTipo.getSelectedItem().toString());
        lugar.setPrecioMinimo(Integer.parseInt(editTextPrecioMinimo.getText().toString()));
        lugar.setPrecioMaximo(Integer.parseInt(editTextPrecioMaximo.getText().toString()));

        myRef = database.getReference(Utils.PATH_LUGARES_PENDIENTES);
        String key = myRef.push().getKey();
        myRef = database.getReference(Utils.PATH_LUGARES_PENDIENTES+key);
        myRef.setValue(lugar);
        uploadFile(key);
        startActivity(new Intent(view.getContext(), DropMenuActivity.class));

    }

    private void getDirectionFromGeocoderNominatim(final String direccion)
    {
        Log.d("Test", "ENTRE");
        // Retreive Geocoding data (add this code to an event click listener on a button)
        new AsyncTask<Void, Void, Address>(){
            @Override
            protected Address doInBackground(Void... voids) {
                Log.d("Test", "ENTRE x2");
                // Reverse Geocoding
                GeocoderNominatim geocoder = new GeocoderNominatim(Configuration.getInstance().getUserAgentValue());
                String theAddress = null;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(direccion, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        return address;
                    } else {
                        theAddress = null;
                    }
                } catch (Exception e) {
                    theAddress = null;
                }
                if (theAddress != null) {
                    Log.d("Test", "Address: " + theAddress);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address) {
                super.onPostExecute(address);
                if(address != null)
                {
                    if(mMap != null)
                    {
                        Marker placeMarker = new Marker(mMap);
                        placeMarker.setIcon(view.getContext().getDrawable(R.drawable.btnpincho));
                        placeMarker.setPosition(new GeoPoint(address.getLatitude(), address.getLongitude()));
                        placeMarker.setTitle("Lugar encontrado");
                        placeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        mMap.getOverlays().add(placeMarker);
                    }
                }
                else
                {
                    Toast.makeText(view.getContext(), "Lugar no encontrado, seleccione la ubicación en el mapa de modo manual", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private boolean validateForm()
    {
        boolean valid = true;
        String message = "";
        if(editTextNombreLugar.getText().toString().isEmpty()) {
            valid = false;
            message = "Es necesario agregar el nombre del lugar";
            editTextNombreLugar.setError("Required");
        }
        if(editTextPrecioMinimo.getText().toString().isEmpty())
        {
            valid = false;
            message = (message.equals(""))?"Es necesario agregar el precio mínimo":"Es necesario llenar los campos requeridos";
            editTextPrecioMinimo.setError("Required");
        }
        if(editTextPrecioMaximo.getText().toString().isEmpty())
        {
            valid = false;
            message = (message.equals(""))?"Es necesario agregar el precio máximo":"Es necesario llenar los campos requeridos";
            editTextPrecioMaximo.setError("Required");
        }
        if(editTextDireccion.getText().toString().isEmpty())
        {
            valid = false;
            message = (message.equals(""))?"Es necesario agregar la dirección":"Es necesario llenar los campos requeridos";
            editTextDireccion.setError("Required");
        }
        if(locationSelected == null)
        {
            valid = false;
            message = (message.equals(""))?"Es necesario seleccionar una ubicación":"Es necesario llenar los campos requeridos";
        }

        if(!valid) {
            View parentLayout = getActivity().findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    message,
                    Snackbar.LENGTH_LONG).show();
        }

        return valid;
    }

    private void addPhotoFromCamera()
    {
        PermissionsManager.requestPermissionforFragment(this, this.getActivity(), Manifest.permission.CAMERA,
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
        PermissionsManager.requestPermissionforFragment(this, this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
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
                    textFotosAgregadas.setText("Permiso denegado");
                }
                return;
            }
            case PermissionsManager.READ_STORAGE_PERMISSION_FOR_MAP: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    initializeMap();
                }
                else
                {
                    textFotosAgregadas.setText("Permiso denegado");
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
                    textFotosAgregadas.setText("Permiso denegado");
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
                    imgResultado.getLayoutParams().height = (int) (100.0 * view.getContext().getResources().getDisplayMetrics().density);
                    imgResultado.requestLayout();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    miImagenBit = baos.toByteArray();

                    break;
                case PermissionsManager.IMAGE_PICKER_REQUEST:
                    try {
                        miImagenUri = data.getData();
                        final InputStream imageStream = this.getActivity().getContentResolver().openInputStream(miImagenUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgResultado.setImageBitmap(selectedImage);
                        imgResultado.getLayoutParams().height = (int) (100.0 * view.getContext().getResources().getDisplayMetrics().density);
                        imgResultado.requestLayout();
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

    private void uploadFile(String key) {
        StorageReference userRef = mStorageRef.child(Utils.PATH_LUGARES+key+"/place.jpg");
        if(miImagenUri != null)
        {
            userRef.putFile(miImagenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }else
        {
            userRef.putBytes(miImagenBit)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }

    }
}