package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bogo.Utils.PermissionsManager;

import org.osmdroid.config.Configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.example.bogo.R;

public class AddReviewActivity extends AppCompatActivity {

    Button buttonEnviar, buttonAddPhoto, buttonAddCamera;
    TextView txtFotosAgregadas;
    ImageView imgAddPlace;
    View view = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_add_review, container, false);
        buttonEnviar = view.findViewById(R.id.buttonEnviar);
        buttonAddPhoto = view.findViewById(R.id.btnAddFoto);
        buttonAddCamera = view.findViewById(R.id.btnAddCamera);
        txtFotosAgregadas = view.findViewById(R.id.textFotosAgregadas);
        imgAddPlace = view.findViewById(R.id.imgAddPlace);

        final Context ctx = view.getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PlaceDescriptionActivity.class);
                startActivity(intent);
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

        return view;
    }

    private void addPhotoFromCamera()
    {
        PermissionsManager.requestPermission(this, Manifest.permission.CAMERA,
                "Para poder mostrar fotos tomadas desde su cámara", PermissionsManager.CAMERA_PERMISSION );
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PermissionsManager.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void addPhotoFromGallery()
    {
        PermissionsManager.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                "Para poder mostrar fotos que ya tenga guardadas", PermissionsManager.READ_STORAGE_PERMISSION);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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
                    imgAddPlace.getLayoutParams().height = (int) (100.0 * view.getContext().getResources().getDisplayMetrics().density);
                    imgAddPlace.requestLayout();
                    break;
                case PermissionsManager.IMAGE_PICKER_REQUEST:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgAddPlace.setImageBitmap(selectedImage);
                        imgAddPlace.getLayoutParams().height = (int) (100.0 * view.getContext().getResources().getDisplayMetrics().density);
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