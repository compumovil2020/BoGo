package com.example.bogo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bogo.Activities.ChatActivity;
import com.example.bogo.Activities.MyFriendsActivity;
import com.example.bogo.R;

import java.util.ArrayList;

public class MensajeAdapter extends ArrayAdapter<ChatActivity.Mensaje> {
    private final Context context;
    private final ArrayList<ChatActivity.Mensaje> values;

    public MensajeAdapter(Context context, ArrayList<ChatActivity.Mensaje> values) {
        super(context, R.layout.adapter_messages,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_messages, parent, false);
        TextView recibido = rowView.findViewById(R.id.txtRecieved);
        TextView enviado =  rowView.findViewById(R.id.txtSent);

        if(values.get(position).getTipo()) {
            enviado.setText(this.values.get(position).getMensaje());
            recibido.setVisibility(View.INVISIBLE);
        }else
        {
            recibido.setText(this.values.get(position).getMensaje());
            enviado.setVisibility(View.INVISIBLE);
        }
        return rowView;
    }
}
