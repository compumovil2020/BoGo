package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WishList extends Fragment {
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ListView listWish;
    TextView txtNoDes;
    ArrayList<LugarLista> lugares;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wish_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        listWish = view.findViewById(R.id.lstWish);
        txtNoDes = view.findViewById(R.id.txtNoDeseos);
        lugares = new ArrayList<>();
        loadMisDes(mAuth.getUid());

        return view;
    }

    public void loadMisDes(String uid){
        myRef = database.getReference(Utils.PATH_DESEOS+uid);
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
                    LugarLista des = new LugarLista(lugar,singleSnapshot.getKey());
                    for (String lug:keyLugares)
                    {
                        if(des.getId().equals(lug))
                        {
                            lugares.add(des);
                        }
                    }
                }
                if(lugares.isEmpty())
                {
                    txtNoDes.setVisibility(View.VISIBLE);
                }
                PlaceAdapter adapter = new PlaceAdapter(getContext(), lugares);
                listWish.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}