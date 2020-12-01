package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoritesList extends Fragment {
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ListView listFav;
    TextView txtNoFav;
    ArrayList<LugarLista> lugares;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_favorites_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        listFav = view.findViewById(R.id.lstFav);
        lugares = new ArrayList<>();
        txtNoFav = view.findViewById(R.id.txtNoFav);
        loadMisFav(mAuth.getUid());
        return view;
    }

    public void loadMisFav(String uid){
        myRef = database.getReference(Utils.PATH_FAVORITOS+uid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> keyLugares = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    keyLugares.add(singleSnapshot.getValue(String.class));
                }
                loadPlaces(keyLugares);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void loadPlaces(final ArrayList<String> keyLugares) {
        myRef = database.getReference(Utils.PATH_LUGARES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lugar lugar = singleSnapshot.getValue(Lugar.class);
                    LugarLista fav = new LugarLista(lugar,singleSnapshot.getKey());
                    for (String lug:keyLugares)
                    {
                        if(fav.getId().equals(lug))
                        {
                            lugares.add(fav);
                        }
                    }
                }
                if(lugares.isEmpty())
                {
                    txtNoFav.setVisibility(View.VISIBLE);
                }
                PlaceAdapter adapter = new PlaceAdapter(getContext(), lugares);
                listFav.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}