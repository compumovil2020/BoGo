package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Adapters.TimelineAdapter;
import com.example.bogo.Entidades.Lugar;
import com.example.bogo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TimeLineActivity extends Fragment {

    private static final String PATH_VISITADOS = "visitados/";
    private static final String PATH_LUGARES = "lugares/";
    ListView listTime;
    TextView cargando;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_time_line, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        cargando = view.findViewById(R.id.txtCargandoTime);
        listTime = view.findViewById(R.id.listTime);
        listTime.setDividerHeight(0);
        listTime.setDivider(null);
        ArrayList<ComponentesLista> contenido = new ArrayList<>();
        verLugaresVisitados(contenido, auth.getUid());

        return view;
    }

    void verLugaresVisitados(final ArrayList<ComponentesLista> contenido, String uid)
    {
        myRef = database.getReference(PATH_VISITADOS +uid);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Long> llaveLugar = new HashMap<>();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String key= dataSnap.getKey();
                    Long fecha = dataSnap.getValue(Long.class);
                    llaveLugar.put(key,fecha);
                }
                obtenerLugares(contenido,llaveLugar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

void obtenerLugares(final ArrayList<ComponentesLista> contenido, final HashMap<String, Long> llaveLugar)
{
    myRef = database.getReference(PATH_LUGARES);
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                Lugar lugar = dataSnap.getValue(Lugar.class);
                Long fecha = llaveLugar.get(dataSnap.getKey());
                if(fecha!=null)
                {
                    ComponentesLista nuevo = new ComponentesLista(lugar,fecha,dataSnap.getKey());
                    contenido.add(nuevo);
                }
            }
            Collections.sort(contenido,Collections.reverseOrder(new ComparadorFechas()));
            TimelineAdapter adapter = new TimelineAdapter(getContext(), contenido);
            listTime.setAdapter(adapter);
            cargando.setVisibility(View.INVISIBLE);
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

    public class ComponentesLista{
        Lugar visitado;
        Long fecha;
        String key;

        public ComponentesLista(Lugar visitado, Long fecha, String key) {
            this.visitado = visitado;
            this.fecha = fecha;
            this.key = key;
        }

        public Lugar getVisitado() {
            return visitado;
        }

        public void setVisitado(Lugar visitado) {
            this.visitado = visitado;
        }

        public Long getFecha() {
            return fecha;
        }

        public void setFecha(Long fecha) {
            this.fecha = fecha;
        }

        public String getKey() {
            return key;
        }
    }

    public class ComparadorFechas implements Comparator<ComponentesLista> {

        @Override
        public int compare(ComponentesLista c1, ComponentesLista c2) {
            // TODO Auto-generated method stub
            return  c1.getFecha().compareTo(c2.getFecha());
        }

    }
}