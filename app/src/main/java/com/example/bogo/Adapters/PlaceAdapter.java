package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bogo.Activities.PlaceDescriptionActivity;
import com.example.bogo.Entidades.Lugar;
import com.example.bogo.Entidades.LugarLista;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlaceAdapter extends ArrayAdapter<LugarLista> {

    private final Context context;
    private final ArrayList<LugarLista> values;
    private StorageReference mStorageRef;
    String keyLugar;
    TextView txtNamePlace;
    TextView txtTypePlace;
    ImageView imgPlace;

    public PlaceAdapter(Context context, ArrayList<LugarLista> values) {
        super(context, R.layout.adapter_place, values);
        this.context = context;
        this.values = values;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_place, parent, false);

        txtNamePlace = rowView.findViewById(R.id.textNamePlace);
        txtTypePlace = rowView.findViewById(R.id.txtTypePlace);
        LinearLayout stars = rowView.findViewById(R.id.llstarsPlaceList);
        ImageView imgLugar = rowView.findViewById(R.id.imgPlace);
        TextView verMas = rowView.findViewById(R.id.txtVerMas);

        stars.addView(Utils.getStarRate(values.get(position).getLugar().getPromedio(), getContext()));
        txtNamePlace.setText(this.values.get(position).getLugar().getNombre());
        txtTypePlace.setText(this.values.get(position).getLugar().getTipo());
        keyLugar = this.values.get(position).getId();

        try {
            downloadFile(imgLugar,Utils.PATH_LUGARES+this.values.get(position).getId()+"/place.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        verMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlaceDescriptionActivity.class);
                intent.putExtra("keyLugar",values.get(position).getId());
                getContext().startActivity(intent);
            }
        });



           return rowView;
    }

    public void downloadFile(final ImageView imgLugar, String path) throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        StorageReference imageRef = mStorageRef.child(path);
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                imgLugar.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }}
