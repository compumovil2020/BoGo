package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Entidades.Lugar;
import com.example.bogo.R;
import com.example.bogo.Utils.PermissionsManager;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceDescriptionActivity extends AppCompatActivity
{

    LinearLayout llResenas;
    Button btnVerMapa, btnCalificar, btnAddFavorite, btnAddWish, btnAddVisited,
           btnCorreo, btnTelefono;
    ImageView imgPlaceDescription;
    TextView txtPlaceTitle, txtPlaceDescription, txtHorario, txtPrecios, txtDireccion,
             txtContacto, txtTelefono, txtCorreo;

    String keyLugar;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Lugar lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

        btnVerMapa = findViewById(R.id.btnVerEnMapa);
        btnCalificar = findViewById(R.id.btnCalificar);
        btnAddFavorite = findViewById(R.id.btnAddFavourite);
        btnAddWish = findViewById(R.id.btnAddWishlist);
        btnAddVisited = findViewById(R.id.btnAddVisited);
        btnCorreo = findViewById(R.id.btnCorreo);
        btnTelefono = findViewById(R.id.btnTelefono);

        llResenas = findViewById(R.id.llOtrasResenas);

        imgPlaceDescription = findViewById(R.id.imgPlaceDescription);

        txtPlaceTitle = findViewById(R.id.txtPlaceTitle);
        txtPlaceDescription = findViewById(R.id.txtPlaceDescription);
        txtHorario = findViewById(R.id.txtHorario);
        txtPrecios = findViewById(R.id.txtPrecios);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtContacto = findViewById(R.id.txtContacto);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);

        keyLugar = getIntent().getStringExtra("key");
        //Toast.makeText(getBaseContext(), "Mostrar: "+keyLugar, Toast.LENGTH_LONG).show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PlaceMapActivity.class);
                startActivity(intent);
            }
        });

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddReviewActivity.class);
                startActivity(intent);
            }
        });

        btnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Agregado a Favoritos!", Toast.LENGTH_LONG).show();
            }
        });

        btnAddWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Agregado a Lista de Deseos!", Toast.LENGTH_LONG).show();
            }
        });

        btnAddVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Agregado a Historial!", Toast.LENGTH_LONG).show();
            }
        });

        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Accediendo al correo...", Toast.LENGTH_LONG).show();
                Intent correo = new Intent(Intent.ACTION_SENDTO);
                correo.setType("text/plain");
                correo.putExtra(Intent.EXTRA_EMAIL  , new String[]{"buffalo@wings.com"});
                startActivity(correo);
            }
        });

        btnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionsManager.requestPermission( PlaceDescriptionActivity.this, Manifest.permission.CALL_PHONE,
                        "Para poder llamar al lugar", PermissionsManager.PHONE_PERMISSION);
                if(ContextCompat.checkSelfPermission( PlaceDescriptionActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "3108105207"));
                    startActivity(intent);
                }
            }
        });

        for(int i = 0; i < 10; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.review_layout, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 50);
            child.setLayoutParams(params);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), SeeReview.class);
                    startActivity(intent);
                }
            });
            llResenas.addView(child);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionsManager.PHONE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "3108105207"));
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(),"No se puede acceder al teléfono", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    void obtenerLugares()
    {
        myRef = database.getReference(Utils.PATH_LUGARES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    if(dataSnap.getKey().equals(keyLugar)){
                        lugar = dataSnap.getValue(Lugar.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}