package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bogo.R;
import com.example.bogo.Services.ChatJobIntentService;
import com.example.bogo.Services.LocationJobIntentService;

public class MainMenuActivity extends Fragment {
    public static String CHANNEL_ID = "BoGo Chat";
    Button rumba;
    Button cafes;
    Button eventos;
    Button turisticos;
    Button restaurantes;
    Button parques;
    Button recomendados;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_menu, container, false);

        rumba = view.findViewById(R.id.btnRumba);
        cafes = view.findViewById(R.id.btnCafes);
        eventos = view.findViewById(R.id.btnEventos);
        turisticos = view.findViewById(R.id.btnTuristico);
        restaurantes = view.findViewById(R.id.btnRestaurantes);
        parques = view.findViewById(R.id.btnParque);
        recomendados = view.findViewById(R.id.btnRecomendados);

        createNotificationChannel();
        Intent intent = new Intent(view.getContext(), ChatJobIntentService.class);
        ChatJobIntentService.enqueueWork(view.getContext(),intent);

        Intent intent2 = new Intent(view.getContext(), LocationJobIntentService.class);
        LocationJobIntentService.enqueueWork(view.getContext(), intent2);

        rumba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Rumba");
                startActivity(i);
            }
        });

        cafes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Cafés");
                startActivity(i);
            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Sitio Turistico");
                startActivity(i);
            }
        });

        turisticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Sitio Turistico");
                startActivity(i);
            }
        });

        restaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Restaurante");
                startActivity(i);
            }
        });

        parques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Parque");
                startActivity(i);
            }
        });

        recomendados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PlaceListActivity.class);
                i.putExtra("tipo","Sitio Turistico");
                startActivity(i);
            }
        });

        return view;
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {

            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = view.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}