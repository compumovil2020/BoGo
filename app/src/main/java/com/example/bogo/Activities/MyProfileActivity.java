package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bogo.R;

public class MyProfileActivity extends AppCompatActivity {

    Button btnMesAmies;
    Button btnAjuterAmies;
    Button btnRedimirPuntos;
    Button btnPuntosRedimidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        btnMesAmies = findViewById(R.id.btnMesAmies);
        btnAjuterAmies = findViewById(R.id.btnAjuterAmies);
        btnRedimirPuntos = findViewById(R.id.btnRedimirPuntos);
        btnPuntosRedimidos = findViewById(R.id.btnPuntosRedimidos);

        btnMesAmies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), MyFriendsActivity.class);
                startActivity(n);
            }
        });

        btnAjuterAmies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent n =  new Intent(getBaseContext(), MyFriendsActivity.class);
                //startActivity(n);
            }
        });

        btnRedimirPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), RedeemPointsActivity.class);
                startActivity(n);
            }
        });

        btnPuntosRedimidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), MyRedeemActivity.class);
                startActivity(n);
            }
        });

    }
}