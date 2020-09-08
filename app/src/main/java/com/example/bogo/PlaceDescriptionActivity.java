package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDescriptionActivity extends AppCompatActivity {

    LinearLayout llResenas;
    Button btnVerMapa, btnCalificar, btnAddFavorite, btnAddWish, btnAddVisited,
           btnCorreo, btnTelefono;
    ImageView imgPlaceDescription;
    TextView txtPlaceTitle, txtPlaceDescription, txtHorario, txtPrecios, txtDireccion,
             txtContacto, txtTelefono, txtCorreo;


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
                Toast.makeText(getBaseContext(), "Accediendo al correo... (placeholder)", Toast.LENGTH_LONG).show();
            }
        });

        btnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Accediendo al telefono... (placeholder)", Toast.LENGTH_LONG).show();
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
}