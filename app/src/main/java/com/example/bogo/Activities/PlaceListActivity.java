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
import com.example.bogo.Entidades.LugarLista;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase database;
    ListView listaLugares;
    TextView txtPlaces;
    String tipo;
    ArrayList<LugarLista> lugares = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        database = FirebaseDatabase.getInstance();
        listaLugares = findViewById(R.id.listPlaceList);
        txtPlaces = findViewById(R.id.txtPlaces);
        tipo = getIntent().getStringExtra("tipo");
        loadPlaces();
    }

    public void loadPlaces(){
        txtPlaces.setText(tipo);
        myRef = database.getReference(Utils.PATH_LUGARES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    LugarLista place = singleSnapshot.getValue(LugarLista.class);
                    if(place.getLugar().getTipo().equals(tipo))
                    {
                        lugares.add(place);
                    }
                }
                PlaceAdapter adapter = new PlaceAdapter(getBaseContext(), lugares);
                listaLugares.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}