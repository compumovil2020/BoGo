package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Activities.MyRedeemActivity;
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
import java.util.Calendar;

public class MisCuponesAdapter extends ArrayAdapter<Cupon>
{
    private final Context context;
    private final ArrayList<Cupon> values;

    public MisCuponesAdapter(Context context, ArrayList<Cupon> values) {
        super(context, R.layout.mis_cupones_adapter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.mis_cupones_adapter, parent, false);

        TextView txtnombreCupon = rowView.findViewById(R.id.txtNombreCupon);
        TextView txtPuntos = rowView.findViewById(R.id.txtPuntos);
        TextView txtValidez = rowView.findViewById(R.id.txtValidez);

        txtnombreCupon.setText(values.get(position).getNombre());
        txtPuntos.setText(values.get(position).getValorEnPuntos() + " puntos");
        Long valMili = values.get(position).getFechaLimite();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(valMili);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        txtValidez.setText("Valido hasta: "+date);

        return rowView;
    }
}
