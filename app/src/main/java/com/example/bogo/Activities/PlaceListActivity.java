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
import java.util.Collections;

public class PlaceListActivity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase database;
    ListView listaLugares;
    TextView txtPlaces;
    TextView txtNoResults;
    String caller;
    String tipo;
    String busqueda;
    String categorias;
    ArrayList<LugarLista> lugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        database = FirebaseDatabase.getInstance();
        listaLugares = findViewById(R.id.listPlaceList);
        txtPlaces = findViewById(R.id.txtPlaces);
        txtNoResults = findViewById(R.id.txtNoResults);

        lugares = new ArrayList<>();

        caller = getIntent().getStringExtra("caller");
        if(caller.equals("main"))
        {
            tipo = getIntent().getStringExtra("tipo");
            txtPlaces.setText(tipo);
            loadPlaces();
        }
        if(caller.equals("search"))
        {
            busqueda = getIntent().getStringExtra("busqueda");
            categorias = getIntent().getStringExtra("categorias");
            txtPlaces.setText("Resultados de búsqueda");
            searchPlaces();
        }

    }
public void searchPlaces()
{
    myRef = database.getReference(Utils.PATH_LUGARES);
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                Lugar place = singleSnapshot.getValue(Lugar.class);
                LugarLista nuevo = new LugarLista(place, singleSnapshot.getKey());
                if (categorias.toLowerCase().contains(place.getTipo().toLowerCase()) && (place.getDescripcion().toLowerCase().contains(busqueda)||place.getNombre().toLowerCase().contains(busqueda))){
                    lugares.add(nuevo);
                }
                PlaceAdapter adapter = new PlaceAdapter(getBaseContext(), lugares);
                listaLugares.setAdapter(adapter);
            }
            if (lugares.size() ==0)
            {
                txtNoResults.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    });
}
    public void loadPlaces(){
        myRef = database.getReference(Utils.PATH_LUGARES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lugar place = singleSnapshot.getValue(Lugar.class);
                    LugarLista nuevo = new LugarLista(place,singleSnapshot.getKey());
                    if(!tipo.equals("Recomendados"))
                    {
                        if(nuevo.getLugar().getTipo().equals(tipo))
                        {
                            Log.i("LugarLista" + nuevo.getLugar().getTipo(), "Tipo" + tipo);
                            lugares.add(nuevo);
                        }
                    }else
                    {
                        lugares.add(nuevo);
                    }
                }
                if(tipo.equals("Recomendados"))
                {
                    Collections.shuffle(lugares);
                    ArrayList<LugarLista> recomendados = new ArrayList<>();
                    for(int i = 0; i<10;i++)
                        recomendados.add(lugares.get(i));
                    PlaceAdapter adapter = new PlaceAdapter(getBaseContext(), recomendados);
                    listaLugares.setAdapter(adapter);
                }else {
                    PlaceAdapter adapter = new PlaceAdapter(getBaseContext(), lugares);
                    listaLugares.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}