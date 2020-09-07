package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends Fragment {

    ListView listTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_time_line, container, false);

        listTime = view.findViewById(R.id.listTime);
        listTime.setDividerHeight(0);
        listTime.setDivider(null);
        ArrayList<ComponentesLista> contenido = new ArrayList<>();
        ComponentesLista c1 = new ComponentesLista("El Cielo Restaurante","17 Nov","Restaurante");
        ComponentesLista c2 = new ComponentesLista("Museo del Oro","21 Oct","Sitio turístico");
        ComponentesLista c3 = new ComponentesLista("Varietale Candelaria","31 Ago","Café");
        ComponentesLista c4 = new ComponentesLista("Mundo Aventura","03 Jun","Parque");
        ComponentesLista c5 = new ComponentesLista("Home Burgers","18 May","Restaurante");
        ComponentesLista c6 = new ComponentesLista("Estéreo Picnic","15 Abr","Evento");
        ComponentesLista c7 = new ComponentesLista("Buffalo Wings","21 Mar","Restaurante");
        ComponentesLista c8 = new ComponentesLista("Planetario de Bogotá","09 Mar","Sitio turístico");
        ComponentesLista c9 = new ComponentesLista("Parque de los Novios","26 Feb","Parque");
        ComponentesLista c10 = new ComponentesLista("Templo del Té","08 Feb","Restaurante");
        ComponentesLista c11 = new ComponentesLista("Theatron","30 Ene","Rumba");
        ComponentesLista c12 = new ComponentesLista("Pinky Restaurante","11 Ene","Restaurante");
        ComponentesLista c13 = new ComponentesLista("La Fragata Giratoria","5 Ene","Restaurante");

        contenido.add(c1);
        contenido.add(c2);
        contenido.add(c3);
        contenido.add(c4);
        contenido.add(c5);
        contenido.add(c6);
        contenido.add(c7);
        contenido.add(c8);
        contenido.add(c9);
        contenido.add(c10);
        contenido.add(c11);
        contenido.add(c12);
        contenido.add(c13);



        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getContext(), contenido);
        listTime.setAdapter(adapter);

        return view;
    }



    public class MySimpleArrayAdapter extends ArrayAdapter<ComponentesLista> {
        private final Context context;
        private final ArrayList<ComponentesLista> values;

        public MySimpleArrayAdapter(Context context, ArrayList<ComponentesLista> values) {
            super(context, R.layout.adapter_time_line,values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.adapter_time_line, parent, false);
            TextView textNombre = rowView.findViewById(R.id.txtNombreSItio);
            TextView textTipo =  rowView.findViewById(R.id.txtTipo);
            TextView textFecha= rowView.findViewById(R.id.txtDate);
            textNombre.setText(values.get(position).getNombre());
            textTipo.setText(values.get(position).getTipo());
            textFecha.setText(values.get(position).getDate());
            return rowView;
        }
    }

    public class ComponentesLista{
        String nombre;
        String date;
        String tipo;
        public ComponentesLista(String nombre, String date, String tipo)
        {
            this.nombre = nombre;
            this.date = date;
            this.tipo = tipo;
        }

        public String getTipo() {
            return tipo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getDate() {
            return date;
        }
    }
}