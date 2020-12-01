package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Activities.MyRedeemActivity;
import com.example.bogo.Activities.PlaceDescriptionActivity;
import com.example.bogo.Activities.RedeemPointsActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CuponRedimirAdapter extends ArrayAdapter<Cupon>
{
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private final Context context;
    private final ArrayList<Cupon> values;
    Usuario actual;

    public CuponRedimirAdapter(Context context, ArrayList<Cupon> values) {
        super(context, R.layout.adapter_cupon_redimir, values);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        this.context = context;
        this.values = values;
        obtenerUsuarioActual(mAuth.getUid());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_cupon_redimir, parent, false);

        TextView txtnombreCupon = rowView.findViewById(R.id.txtNombreCuponR);
        TextView txtPuntos = rowView.findViewById(R.id.txtPuntosR);
        TextView txtValidez = rowView.findViewById(R.id.txtValidezR);
        Button btnRedimir = rowView.findViewById(R.id.btnRedimir);

        txtnombreCupon.setText(values.get(position).getNombre());
        txtPuntos.setText(values.get(position).getValorEnPuntos() + " puntos");
        Long valMili = values.get(position).getFechaLimite();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(valMili);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        txtValidez.setText("Valido hasta: "+date);

        btnRedimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyCupon = values.get(position).getId();
                if(actual.getCupones().contains(keyCupon))
                {
                    Toast.makeText(getContext(),"Cupon ya redimido",Toast.LENGTH_LONG).show();
                }else if(actual.getPuntos() >= values.get(position).getValorEnPuntos())
                {
                    actual.setPuntos(actual.getPuntos()-values.get(position).getValorEnPuntos());
                    actual.getCupones().add(keyCupon);
                    cargarUsuario();
                    Intent intent = new Intent(getContext(), MyRedeemActivity.class);
                    getContext().startActivity(intent);
                }else
                {
                    Toast.makeText(getContext(),"No tienes puntos suficientes",Toast.LENGTH_LONG).show();
                }

            }
        });



        return rowView;
    }

    public void cargarUsuario()
    {
        myRef = database.getReference(Utils.PATH_USUARIOS+mAuth.getUid());
        myRef.setValue(actual);

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
