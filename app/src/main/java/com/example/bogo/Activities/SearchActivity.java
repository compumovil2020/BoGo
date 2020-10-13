package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bogo.R;

public class SearchActivity extends AppCompatActivity {
    EditText edtSearchBar;
    CheckBox chkSitioTuristico, chkParques, chkEventos, chkRestaurantes, chkCafes, chkRumba;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearchBar = findViewById(R.id.edtSearchBar);
        chkSitioTuristico = findViewById(R.id.chkSitioTuristico);
        chkParques = findViewById(R.id.chkParques);
        chkEventos = findViewById(R.id.chkEventos);
        chkRestaurantes = findViewById(R.id.chkRestaurantes);
        chkCafes = findViewById(R.id.chkCafes);
        chkRumba = findViewById(R.id.chkRumba);
        btnBuscar = findViewById(R.id.btnBuscar);


        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getBaseContext(), PlaceList.class);
                startActivity(intent);
            }
        });
    }
}