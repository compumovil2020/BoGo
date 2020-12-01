package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
                Intent intent = new Intent (getBaseContext(), PlaceListActivity.class);
                intent.putExtra("caller","search");
                intent.putExtra("busqueda",edtSearchBar.getText().toString());
                String categoria="categoria";
                if(chkCafes.isChecked())
                {
                    categoria+=","+ chkCafes.getText().toString();
                }
                if(chkEventos.isChecked())
                {
                    categoria+=","+ chkSitioTuristico.getText().toString();
                }
                if(chkParques.isChecked())
                {
                    categoria+=","+ chkParques.getText().toString();
                }
                if(chkRestaurantes.isChecked())
                {
                    categoria+=","+ chkRestaurantes.getText().toString();
                }
                if (chkRumba.isChecked())
                {
                    categoria+=","+ chkRumba.getText().toString();
                }
                if(chkSitioTuristico.isChecked())
                {
                    categoria+=","+ chkSitioTuristico.getText().toString();
                }

                intent.putExtra("categorias",categoria);
                if(edtSearchBar.getText().toString().isEmpty())
                {
                    edtSearchBar.setError("Requerido");
                }else{
                    startActivity(intent);
                }

            }
        });
    }
}