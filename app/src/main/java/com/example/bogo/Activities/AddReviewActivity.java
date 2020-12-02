package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Entidades.Lugar;
import com.example.bogo.Entidades.Resenia;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.Utils.PermissionsManager;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.bogo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import okhttp3.internal.Util;

public class AddReviewActivity extends AppCompatActivity {

    Button buttonEnviar, buttonAddPhoto, buttonAddCamera;
    TextView txtFotosAgregadas, txtNombre;
    EditText edtTitulo, edtComentario;
    ImageView imgAddPlace;
    Spinner spnCalificacion;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String keyLugar, keyUser;
    Usuario user;
    Lugar lugar;
    private StorageReference mStorageRef;
    Uri miImagenUri;
    byte[] miImagenBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        buttonEnviar = findViewById(R.id.buttonEnviar);
        buttonAddPhoto = findViewById(R.id.btnAddFoto);
        buttonAddCamera = findViewById(R.id.btnAddCamera);
        txtFotosAgregadas = findViewById(R.id.textFotosAgregadas);
        imgAddPlace = findViewById(R.id.imgAddPlace);
        edtComentario = findViewById(R.id.edtComentario);
        edtTitulo = findViewById(R.id.edtTituloResenia);
        txtNombre = findViewById(R.id.textNombreRes);
        spnCalificacion = findViewById(R.id.spnCal);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final Intent intent = getIntent();
        keyLugar = intent.getStringExtra("keyLugar");
        keyUser = intent.getStringExtra("keyUser");

        loadLugar();
        loadUsuario();

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    addResenia();
                    Intent intent = new Intent(getBaseContext(), PlaceDescriptionActivity.class);
                    intent.putExtra("keyLugar", keyLugar);
                    startActivity(intent);
                }
            }
        });

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoFromGallery();
            }
        });

        buttonAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoFromCamera();
            }
        });
    }

    public void loadLugar(){
        myRef = database.getReference(Utils.PATH_LUGARES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapsot : dataSnapshot.getChildren()){
                    if(singleSnapsot.getKey().equals(keyLugar)){
                        lugar = singleSnapsot.getValue(Lugar.class);
                        txtNombre.setText(lugar.getNombre());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddReviewActivity.this, "error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadUsuario(){
        myRef = database.getReference(Utils.PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapsot : dataSnapshot.getChildren()){
                    if(singleSnapsot.getKey().equals(keyUser)){
                        user = singleSnapsot.getValue(Usuario.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddReviewActivity.this, "error" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addResenia(){
        String c = spnCalificacion.getSelectedItem().toString();
        double cal = Double.parseDouble(c);
        Resenia res = new Resenia(cal,
                edtComentario.getText().toString(),
                keyUser,
                user.getNombreUsuario(),
                edtTitulo.getText().toString());
        if(res.equals(null)){
            Log.i("RESEÑA", "Mi reseña está nula :(");
        }

        myRef = database.getReference(Utils.PATH_RESENIAS + "/");
        String keyRes = myRef.push().getKey();
        myRef = database.getReference(Utils.PATH_RESENIAS + "/"+ keyRes);
        Log.i("RESEÑA AGREAGADA","agregué la reseña con ID: " + keyRes);
        myRef.setValue(res);

        ArrayList<String> a = lugar.getResenias();
        a.add(keyRes);
        int n = a.size();
        double prom = (n * lugar.getPromedio() + cal)/(n+1);
        //newAvg = (size * average + value) / (size + 1);
        lugar.setPromedio(prom);
        lugar.setResenias(a);
        myRef = database.getReference(Utils.PATH_LUGARES + "/" + keyLugar);
        myRef.setValue(lugar);

        uploadFile(keyRes);
    }

    private boolean validateForm()
    {
        boolean valid = true;
        String message = "";
        if(edtComentario.getText().toString().isEmpty()) {
            valid = false;
            message = "Es necesario agregar una pequeña reseña del lugar";
            edtComentario.setError("Required");
        }
        if(spnCalificacion.getSelectedItem().equals(null)){
            valid = false;
            message = "Es necesario calificar el lugar";
        }
        if(edtTitulo.getText().toString().isEmpty()){
            valid = false;
            message = "Es necesario agregar un título a la reseña del lugar";
            edtTitulo.setError("Required");
        }
        if(!valid) {
            View parentLayout = this.findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    message,
                    Snackbar.LENGTH_LONG).show();
        }
        return valid;
    }

    private void uploadFile(String key) {
        StorageReference userRef = mStorageRef.child(Utils.PATH_RESENIAS+ key +"/review.jpg");
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

    private void addPhotoFromCamera()
    {
        PermissionsManager.requestPermission((Activity) this, Manifest.permission.CAMERA,
                "Para poder mostrar fotos tomadas desde su cámara", PermissionsManager.CAMERA_PERMISSION );
        if (ContextCompat.checkSelfPermission(getBaseContext() , Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PermissionsManager.REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private void addPhotoFromGallery()
    {
        PermissionsManager.requestPermission((Activity)this, Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder mostrar fotos que ya tenga guardadas", PermissionsManager.READ_STORAGE_PERMISSION);
        if(ContextCompat.checkSelfPermission((Activity)this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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
                    txtFotosAgregadas.setText("Permiso denegado");
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
                    txtFotosAgregadas.setText("Permiso denegado");
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
                    txtFotosAgregadas.setText("Imagen agregada!");
                    imgAddPlace.setImageBitmap(imageBitmap);
                    imgAddPlace.requestLayout();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    miImagenBit = baos.toByteArray();
                    break;
                case PermissionsManager.IMAGE_PICKER_REQUEST:
                    try {
                        miImagenUri = data.getData();
                        final InputStream imageStream = this.getContentResolver().openInputStream(miImagenUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgAddPlace.setImageBitmap(selectedImage);
                        imgAddPlace.setScaleType(ImageView.ScaleType.FIT_XY);
                        imgAddPlace.getLayoutParams().height = (int) (100.0 * getBaseContext().getResources().getDisplayMetrics().density);
                        imgAddPlace.getLayoutParams().width = (int) (100.0 * getBaseContext().getResources().getDisplayMetrics().density);
                        imgAddPlace.requestLayout();
                        txtFotosAgregadas.setText("Imagen agregada!");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}