package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bogo.Adapters.FriendAdapter;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFriendsActivity extends AppCompatActivity {

    ListView listFriends;
    Button btnAddFriend;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private String UidCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_my_friends);
        currentUser = mAuth.getCurrentUser();
        UidCurrent = currentUser.getUid();
        listFriends = findViewById(R.id.listFriends);
        btnAddFriend = findViewById(R.id.btnAddFriend);

        final ArrayList<MyFriendsActivity.ComponentesUsuario> contenido = new ArrayList<>();

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),AddFriendActivity.class);
                startActivity(intent);
            }
        });

        listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nombre = view.findViewById(R.id.txtFriendName);
                TextView key = view.findViewById(R.id.txtKEY);
                Intent intent = new Intent(getBaseContext(),ChatActivity.class);
                intent.putExtra("idAmigo", key.getText());
                intent.putExtra("nombreAmigo", nombre.getText());
                startActivity(intent);
            }
        });

        verAmigos(contenido, UidCurrent);

    }

    void verAmigos(final ArrayList<MyFriendsActivity.ComponentesUsuario> contenido, String UidCurrent) {
        myRef = database.getReference(Utils.PATH_SEGUIDORES + UidCurrent);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> llaveAmigo = new ArrayList<>();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String key= dataSnap.getKey();
                    llaveAmigo.add(key);
                }
                obtenerAmigos(contenido,llaveAmigo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void obtenerAmigos(final ArrayList<MyFriendsActivity.ComponentesUsuario> contenido, final ArrayList<String> llaveAmigo) {
        myRef = database.getReference(Utils.PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String key = dataSnap.getKey();
                    for (String k : llaveAmigo){
                        if(k.equals(key)){
                            Usuario usuario = dataSnap.getValue(Usuario.class);
                            MyFriendsActivity.ComponentesUsuario nuevo = new MyFriendsActivity.ComponentesUsuario(usuario, key);
                            contenido.add(nuevo);

                        }
                    }

                }
                FriendAdapter adapter = new FriendAdapter(getBaseContext(), contenido);
                listFriends.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public class ComponentesUsuario{
        Usuario usuario;
        String key;

        public ComponentesUsuario(Usuario usuario, String key) {
            this.usuario = usuario;
            this.key = key;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario visitado) {
            this.usuario = visitado;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}