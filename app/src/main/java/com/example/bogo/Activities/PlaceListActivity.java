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
    ValueEventListener usuario;
    private FirebaseAuth mAuth;
    ArrayList<Lugar> lugares = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        listaDisponibles = findViewById(R.id.listPlaceList);
        //loadPlaces();
    }
/*
    public void loadPlaces(){
        myRef = database.getReference(PLACES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lugar place = singleSnapshot.getValue(Lugar.class);
                    Log.i(TAG, "Encontró usuario: " + myUser.getName());
                    String name = myUser.getName();
                    int age = myUser.getAge();
                    Toast.makeText(MapHomeActivity.this, name + ":" + age, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "error en la consulta", databaseError.toException());
            }
        });
    }*/
}