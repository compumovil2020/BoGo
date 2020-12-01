package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bogo.Activities.PlaceDescriptionActivity;
import com.example.bogo.Activities.SeeReview;
import com.example.bogo.Entidades.Resenia;
import com.example.bogo.R;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<PlaceDescriptionActivity.listaReviews> {
    private final Context context;
    private final ArrayList<PlaceDescriptionActivity.listaReviews> values;

    public ReviewAdapter(@NonNull Context context, ArrayList<PlaceDescriptionActivity.listaReviews> values) {
        super(context, R.layout.review_layout, values);
        this.context = context;
        this.values = values;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.review_layout, parent, false);

        TextView txtReviewTitle = rowView.findViewById(R.id.txtReviewTitle);
        TextView txtReviewAuthor =  rowView.findViewById(R.id.txtReviewAuthor);
        TextView txtDescription= rowView.findViewById(R.id.txtDescription);
        TextView textView4= rowView.findViewById(R.id.textView4);
        Button button = rowView.findViewById(R.id.buttonVerMas);

        Resenia mueche = values.get(position).getRese();

        String calificacion = mueche.getCalificacion()+" / 5.0";

        txtReviewTitle.setText(mueche.getTitulo());
        txtReviewAuthor.setText("Por: "+mueche.getUsername());
        txtDescription.setText(mueche.getTexto());
        textView4.setText(calificacion);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rowView.getContext(), SeeReview.class);
                i.putExtra("keyResenia",values.get(position).getKey());
                i.putExtra("lugar", values.get(position).getLieu());
                i.putExtra("titulo", values.get(position).getRese().getTitulo());
                i.putExtra("autor", values.get(position).getRese().getUsername());
                i.putExtra("descripcion", values.get(position).getRese().getTexto());
                Log.i("Numero: ", ""+values.get(position).getRese().getCalificacion());
                i.putExtra("cal", values.get(position).getRese().getCalificacion());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rowView.getContext().startActivity(i);
            }
        });

        return rowView;
    }
}
