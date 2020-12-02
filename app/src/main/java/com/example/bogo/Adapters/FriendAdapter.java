package com.example.bogo.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bogo.Activities.MyFriendsActivity;
import com.example.bogo.Activities.TimeLineActivity;
import com.example.bogo.Entidades.Usuario;
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

public class FriendAdapter extends ArrayAdapter<MyFriendsActivity.ComponentesUsuario> {
    private final Context context;
    private final ArrayList<MyFriendsActivity.ComponentesUsuario> values;
    private StorageReference mStorageRef;

    public FriendAdapter(Context context, ArrayList<MyFriendsActivity.ComponentesUsuario> values) {
        super(context, R.layout.adapter_friend, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_friend, parent, false);
        TextView txtFriendName = rowView.findViewById(R.id.txtFriendName);
        TextView txtFriendUser = rowView.findViewById(R.id.txtFriendUser);
        TextView txtKey = rowView.findViewById(R.id.txtKEY);
        ImageView imgFriend = rowView.findViewById(R.id.imgFotoAmigo);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        txtFriendName.setText(this.values.get(position).getUsuario().getNombre());
        txtFriendUser.setText(this.values.get(position).getUsuario().getNombreUsuario());
        txtKey.setText(this.values.get(position).getKey());
        try {
            downloadFile(Utils.PATH_USUARIOS + this.values.get(position).getKey() + "/profile.jpg", imgFriend);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return rowView;
    }

    private void downloadFile(String id, final ImageView imgUser) throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        Log.i("TAG ID", id);
        StorageReference imageRef = mStorageRef.child(id);
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                imgUser.setImageURI(Uri.fromFile(localFile));
                Log.i("Friends", "succesfully downloaded friend img");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("Friends", "unsuccesfully downloaded friend img");
            }
        });
    }
}
