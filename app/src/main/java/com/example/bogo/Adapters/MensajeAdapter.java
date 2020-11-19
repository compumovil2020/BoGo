package com.example.bogo.Adapters;

import android.content.Context;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bogo.Activities.ChatActivity;
import com.example.bogo.Activities.MyFriendsActivity;
import com.example.bogo.Entidades.Mensaje;
import com.example.bogo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class MensajeAdapter extends ArrayAdapter<Mensaje> {
    private final Context context;
    private final ArrayList<Mensaje> values;
    FirebaseAuth mAuth;

    public MensajeAdapter(Context context, ArrayList<Mensaje> values) {
        super(context, R.layout.adapter_messages,values);
        this.context = context;
        this.values = values;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_messages, parent, false);
        LinearLayout layRecibido = rowView.findViewById(R.id.layReciever);
        LinearLayout layEnviado = rowView.findViewById(R.id.laySent);
        TextView recibido = rowView.findViewById(R.id.txtRecieved);
        TextView enviado =  rowView.findViewById(R.id.txtSent);
        TextView timeRecibido = rowView.findViewById(R.id.txtRecievedTime);
        TextView timeEnviado = rowView.findViewById(R.id.txtSentTime);

        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(values.get(position).getFechaHoraEnviado());
        String hora = DateFormat.format("HH:mm", cal).toString();

        if(values.get(position).getRemitente().equals(mAuth.getUid())) {
            enviado.setText(this.values.get(position).getTexto());
            timeEnviado.setText(hora);
            layRecibido.setVisibility(View.INVISIBLE);
        }else
        {
            recibido.setText(this.values.get(position).getTexto());
            timeRecibido.setText(hora);
            layEnviado.setVisibility(View.INVISIBLE);
        }
        return rowView;
    }
}
