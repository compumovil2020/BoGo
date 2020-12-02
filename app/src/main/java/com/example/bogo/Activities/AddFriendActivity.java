package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bogo.Adapters.FriendAdapter;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.Adapters.AddFriendAdapter;
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

public class AddFriendActivity extends AppCompatActivity {

    ListView lstNewFriends;
    EditText edtSearchFriend;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<AddFriendActivity.ComponentesUsuario> contenido = new ArrayList<>();
    ArrayList<String> seguidores = new ArrayList<>();
    ArrayList<AddFriendActivity.ComponentesUsuario> contenidoAux = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        lstNewFriends = findViewById(R.id.lstFindFriends);
        edtSearchFriend = findViewById(R.id.edtSearchFriend);

        edtSearchFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String nameFriend = edtSearchFriend.getText().toString();

                    if (!nameFriend.isEmpty()) {
                        ArrayList<AddFriendActivity.ComponentesUsuario> contenidoAct = new ArrayList<>();
                        for (AddFriendActivity.ComponentesUsuario friend : contenido) {
                            if ((friend.getUsuario().getNombre().contains(nameFriend.toLowerCase())) || (friend.getUsuario().getNombre().toLowerCase().contains(nameFriend.toLowerCase()))) {
                                contenidoAct.add(friend);
                            }
                        }
                        AddFriendAdapter newadapter = new AddFriendAdapter(getBaseContext(), contenidoAct);
                        lstNewFriends.setAdapter(newadapter);
                        onStop();
                    } else {
                        edtSearchFriend.setError("Required");
                    }
                }
                return false;
            }

        });

        validarAmistad();

    }
    public void validar(){

        for(AddFriendActivity.ComponentesUsuario u : contenido){
            Log.i("LISTA CONTENIDO", "U: " + u.getKey());
        }

        for(String u : seguidores){
            Log.i("LISTA SEGUIDORES", "U: " + u);
        }

        for (AddFriendActivity.ComponentesUsuario u : contenido ){
            if(!seguidores.contains(u.getKey())){
                contenidoAux.add(u);
            }
        }

        for(AddFriendActivity.ComponentesUsuario u : contenidoAux){
            Log.i("LISTA AUX", "U: " + u.getKey());
        }

        AddFriendAdapter adapter = new AddFriendAdapter(getBaseContext(), contenidoAux);
        lstNewFriends.setAdapter(adapter);
    }

    public void obtenerUsuarios() {
        myRef = database.getReference(Utils.PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String key = dataSnap.getKey();
                    if(!key.equals(currentUser.getUid())){
                        Usuario usuario = dataSnap.getValue(Usuario.class);
                        AddFriendActivity.ComponentesUsuario nuevo = new AddFriendActivity.ComponentesUsuario(usuario, key);
                        contenido.add(nuevo);
                    }
                }
                Log.i("USUARIOS", "tengo " + contenido.size());
                validar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void validarAmistad(){
        myRef = database.getReference(Utils.PATH_SEGUIDORES + "/" + currentUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    String key = dataSnap.getKey();
                    seguidores.add(key);
                }
                Log.i("SEGUIDORES", "sigo a " + seguidores.size() + "personas");
                obtenerUsuarios();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}

