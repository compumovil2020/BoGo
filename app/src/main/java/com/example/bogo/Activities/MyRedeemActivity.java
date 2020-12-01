package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.bogo.Adapters.CuponRedimirAdapter;
import com.example.bogo.Adapters.MisCuponesAdapter;
import com.example.bogo.Entidades.Cupon;
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

public class MyRedeemActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ListView lstCupon;
    ArrayList<Cupon> cupones;
    Usuario actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_redeem);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        lstCupon = findViewById(R.id.lstMisCupones);
        cupones = new ArrayList<>();
        obtenerUsuarioActual(mAuth.getUid());
        loadMisCupones();
    }
    public void loadMisCupones()
    {
        myRef = database.getReference(Utils.PATH_CUPONES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if(actual.getCupones().contains(singleSnapshot.getKey()))
                    {
                        Cupon nuevo = singleSnapshot.getValue(Cupon.class);
                        cupones.add(nuevo);
                    }
                }
                MisCuponesAdapter adapter =  new MisCuponesAdapter(getBaseContext(),cupones);
                lstCupon.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void obtenerUsuarioActual(String uid)
    {
        myRef = database.getReference(Utils.PATH_USUARIOS+uid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actual = dataSnapshot.getValue(Usuario.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}