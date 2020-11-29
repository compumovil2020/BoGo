package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bogo.Adapters.PlaceAdapter;
import com.example.bogo.Entidades.Lugar;
import com.example.bogo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {

    public static final String PLACES = "lugares/";
    DatabaseReference myRef;
    FirebaseDatabase database;
    ListView listaDisponibles;
    TextView txtPlaces;
    String tipo = getIntent().getStringExtra("tipo");
    ArrayList<Lugar> lugares = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        database = FirebaseDatabase.getInstance();
        listaDisponibles = findViewById(R.id.listPlaceList);
        txtPlaces = findViewById(R.id.txtPlaces);
        loadPlaces();
    }

    public void loadPlaces(){
        txtPlaces.setText(tipo);
        myRef = database.getReference(PLACES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lugar place = singleSnapshot.getValue(Lugar.class);
                    if(place.getTipo().equals(tipo))
                    {
                        lugares.add(place);
                    }

                    PlaceAdapter adapter = new PlaceAdapter(getBaseContext(), lugares);
                    listaDisponibles.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}