package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bogo.Activities.AddFriendActivity;
import com.example.bogo.Activities.MyFriendsActivity;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddFriendAdapter extends ArrayAdapter<AddFriendActivity.ComponentesUsuario> {
    private final Context context;
    private final ArrayList<AddFriendActivity.ComponentesUsuario> values;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private StorageReference mStorageRef;


    public AddFriendAdapter(Context context, ArrayList<AddFriendActivity.ComponentesUsuario> values) {
        super(context, R.layout.adapter_add_friend,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_add_friend, parent, false);
        TextView txtFriendName = rowView.findViewById(R.id.txtFriendName);
        TextView txtFriendUser =  rowView.findViewById(R.id.txtFriendUser);
        TextView txtkey = rowView.findViewById(R.id.txtKeyAdd);
        ImageView imgFriend = rowView.findViewById(R.id.imgFotoAmigo);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        final ImageButton imgAdd = rowView.findViewById(R.id.imgAdd);

        txtFriendName.setText(this.values.get(position).getUsuario().getNombre());
        txtFriendUser.setText(this.values.get(position).getUsuario().getNombreUsuario());
        txtkey.setText(this.values.get(position).getKey());
        try {
            downloadFile(Utils.PATH_USUARIOS + this.values.get(position).getKey() + "/profile.jpg", imgFriend);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgAdd.setImageResource(R.drawable.ic_plus);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend(currentUser.getUid(), values.get(position).getKey());
                imgAdd.setImageResource(R.drawable.ic_minus);

            }
        });
        return rowView;
    }



    public void addFriend(String myUid, String newFriend){
        myRef = database.getReference(Utils.PATH_SEGUIDORES + myUid + "/" + newFriend);
        myRef.setValue(true);

        myRef = database.getReference(Utils.PATH_SEGUIDORES + newFriend + "/" + myUid);
        myRef.setValue(true);

        Toast.makeText(context, "Amigo agregado", Toast.LENGTH_LONG).show();

        //Crear chat
        myRef = database.getReference(Utils.PATH_CHATS + "/");
        String keyChat = myRef.push().getKey();
        myRef = database.getReference(Utils.PATH_CHATS + "/" + keyChat + "/" + myUid);
        myRef.setValue(true);
        myRef = database.getReference(Utils.PATH_CHATS + "/" + keyChat + "/" + newFriend);
        myRef.setValue(true);

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
