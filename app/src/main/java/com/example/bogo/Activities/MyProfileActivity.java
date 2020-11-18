package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class MyProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;

    Button btnMyFriends;
    Button btnAddFriends;
    Button btnPuntosRedimidos;

    TextView name, username, email, points;
    ImageView profilePicture;
    Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        name = findViewById(R.id.txtNameProfile);
        username = findViewById(R.id.txtUsernameProfile);
        email = findViewById(R.id.txtEmailProfile);
        points = findViewById(R.id.txtNumPuntosProfile);
        profilePicture = findViewById(R.id.imgProfilePhoto);

        btnMyFriends = findViewById(R.id.btnMesAmies);
        btnAddFriends = findViewById(R.id.btnAjuterAmies);
        btnPuntosRedimidos = findViewById(R.id.btnPuntosRedimidos);


        btnMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), MyFriendsActivity.class);
                startActivity(n);
            }
        });

        btnAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), AddFriendActivity.class);
                startActivity(n);
            }
        });

        btnPuntosRedimidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n =  new Intent(getBaseContext(), MyRedeemActivity.class);
                startActivity(n);
            }
        });

        getUserInfo();

    }
    private void getUserInfo()
    {
        String UID = mAuth.getUid();
        myRef = database.getReference(Utils.PATH_USUARIOS+UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                name.setText(usuario.getNombre());
                username.setText(usuario.getNombreUsuario());
                String p = ""+usuario.getPuntos();
                p = ( usuario.getPuntos() == 1 ) ? ( p + " punto" ) : ( p + " puntos");
                points.setText(p);
                email.setText(mAuth.getCurrentUser().getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            downloadPhoto(Utils.PATH_USUARIOS+UID+"/profile.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadPhoto(String path) throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        StorageReference imageRef = mStorageRef.child(path);
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                profilePicture.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}