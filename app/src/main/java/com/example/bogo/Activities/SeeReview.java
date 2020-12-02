package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class SeeReview extends AppCompatActivity {

    TextView textNombre, textTitulo, textAutor, textComentario;
    LinearLayout imageStarts;
    ImageView imageReview;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_review);

        textNombre = findViewById(R.id.textNombreRes);
        textTitulo = findViewById(R.id.textTitulo);
        textAutor = findViewById(R.id.textAutor);
        textComentario = findViewById(R.id.textComentario);
        imageStarts = findViewById(R.id.imageStarts);
        imageReview = findViewById(R.id.imageReview);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        textNombre.setText(getIntent().getStringExtra("lugar"));
        textTitulo.setText(getIntent().getStringExtra("titulo"));
        textAutor.setText("Por: "+getIntent().getStringExtra("autor"));
        textComentario.setText(getIntent().getStringExtra("descripcion"));
        imageStarts.addView(Utils.getStarRate(getIntent().getDoubleExtra("cal", 0), getBaseContext()));
        try {
            downloadPhoto(Utils.PATH_RESENIAS+getIntent().getStringExtra("keyResenia")+"/review.jpg");
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
                imageReview.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}