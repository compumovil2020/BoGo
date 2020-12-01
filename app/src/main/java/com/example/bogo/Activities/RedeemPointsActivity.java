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
import android.widget.Toast;

import com.example.bogo.Adapters.CuponRedimirAdapter;
import com.example.bogo.Entidades.Cupon;
import com.example.bogo.Entidades.LugarLista;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RedeemPointsActivity extends Fragment {
    DatabaseReference myRef;
    FirebaseDatabase database;
    ListView lstCupon;
    ArrayList<Cupon> cupones;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_redeem_points, container, false);

        database = FirebaseDatabase.getInstance();
        lstCupon = view.findViewById(R.id.lstRedimir);
        cupones = new ArrayList<>();
        loadCupones();
        return view;
    }

    public void loadCupones() {
        myRef = database.getReference(Utils.PATH_CUPONES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Cupon nuevo = singleSnapshot.getValue(Cupon.class);
                    cupones.add(nuevo);
                }
                CuponRedimirAdapter adapter =  new CuponRedimirAdapter(getContext(),cupones);
                lstCupon.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}