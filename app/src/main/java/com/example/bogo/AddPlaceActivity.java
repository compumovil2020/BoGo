package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddPlaceActivity extends Fragment {

    Button btnGo;
    EditText editTextNombreLugar;
    Spinner spinnerTipo;
    EditText editTextRangoPrecio;
    EditText editTextTimeHoraApertura;
    EditText editTextTimeHoraCierre;
    EditText editTextDireccion;
    Button buttonAddFoto;
    TextView textFotosAgregadas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_place, container, false);

        btnGo = view.findViewById(R.id.btnGo);
        editTextNombreLugar = view.findViewById(R.id.editTextNombreLugar);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        editTextRangoPrecio = view.findViewById(R.id.editTextRangoPrecio);
        editTextTimeHoraApertura = view.findViewById(R.id.editTextTimeHoraApertura);
        editTextTimeHoraCierre = view.findViewById(R.id.editTextTimeHoraCierre);
        editTextDireccion = view.findViewById(R.id.editTextDireccion);
        buttonAddFoto = view.findViewById(R.id.buttonAddFoto);
        textFotosAgregadas = view.findViewById(R.id.textFotosAgregadas);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),MainMenu.class);
                startActivity(i);
            }
        });

        return view;
    }
}