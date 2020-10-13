package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bogo.Activities.PlaceDescriptionActivity;
import com.example.bogo.R;
import com.example.bogo.Activities.TimeLineActivity;

import java.util.ArrayList;

public class TimelineAdapter extends ArrayAdapter<TimeLineActivity.ComponentesLista> {
    private final Context context;
    private final ArrayList<TimeLineActivity.ComponentesLista> values;

    public TimelineAdapter(Context context, ArrayList<TimeLineActivity.ComponentesLista> values) {
        super(context, R.layout.adapter_time_line,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_time_line, parent, false);
        TextView textNombre = rowView.findViewById(R.id.txtNombreSItio);
        TextView textTipo =  rowView.findViewById(R.id.txtTipo);
        TextView textFecha= rowView.findViewById(R.id.txtDate);
        Button vermas = rowView.findViewById(R.id.btnVer);
        textNombre.setText(values.get(position).getNombre());
        textTipo.setText(values.get(position).getTipo());
        textFecha.setText(values.get(position).getDate());
        vermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rowView.getContext(), PlaceDescriptionActivity.class);
                rowView.getContext().startActivity(i);
            }
        });

        return rowView;
    }
}
