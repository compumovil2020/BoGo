package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bogo.R;

public class MainMenu extends Fragment {
    Button rumba;
    Button cafes;
    Button eventos;
    Button turisticos;
    Button restaurantes;
    Button parques;
    Button recomendados;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main_menu, container, false);

        rumba = view.findViewById(R.id.btnRumba);
        cafes = view.findViewById(R.id.btnCafes);
        eventos = view.findViewById(R.id.btnEventos);
        turisticos = view.findViewById(R.id.btnTuristico);
        restaurantes = view.findViewById(R.id.btnRestaurantes);
        parques = view.findViewById(R.id.btnParque);
        recomendados = view.findViewById(R.id.btnRecomendados);

        rumba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        cafes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        turisticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        restaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        parques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });

        recomendados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceList.class);
                startActivity(i);
            }
        });




        return view;
    }
}