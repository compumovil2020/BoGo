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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bogo.Entidades.Lugar;
import com.example.bogo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlaceAdapter extends ArrayAdapter<Lugar> {

    private final Context context;
    private final ArrayList<Lugar> values;
    public final String IMAGE = "lugares/";
    private StorageReference mStorageRef;

    public PlaceAdapter(Context context, ArrayList<Lugar> values) {
        super(context, R.layout.adapter_place, values);
        this.context = context;
        this.values = values;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_place, parent, false);

        TextView txtNamePlace = rowView.findViewById(R.id.textNamePlace);
        TextView txtTypePlace = rowView.findViewById(R.id.txtTypePlace);
        ImageView imgPlace = rowView.findViewById(R.id.imgPlace);
        //Button btnPosition = rowView.findViewById(R.id.btnUbicacion);


        txtNamePlace.setText(this.values.get(position).getNombre());
        txtTypePlace.setText(this.values.get(position).getTipo());
        try {
            downloadFile(this.values.get(position).getId(), imgPlace);
        } catch (IOException e) {
            e.printStackTrace();
        }

           return rowView;
    }

    private void downloadFile(String id, final ImageView imgUser) throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        Log.i("TAG ID", id);
        StorageReference imageRef = mStorageRef.child(IMAGE + id + "/place.jpg");
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                imgUser.setImageURI(Uri.fromFile(localFile));
                Log.i("FBApp", "succesfully downloaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("FBApp", "unsuccesfully downloaded");
            }
        });
    }
}
