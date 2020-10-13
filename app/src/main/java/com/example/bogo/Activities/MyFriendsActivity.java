package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Adapters.FriendAdapter;
import com.example.bogo.R;

import java.util.ArrayList;

public class MyFriendsActivity extends AppCompatActivity {

    ListView listFriends;
    Button btnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        listFriends = findViewById(R.id.listFriends);
        btnAddFriend = findViewById(R.id.btnAddFriend);

        ArrayList<Friend> contenido = new ArrayList<>();
        Friend f1 = new Friend("Pepe Pérez", "ppperez12");
        Friend f2 = new Friend("Julian Parada", "JulianParada");
        Friend f3 = new Friend("Sebastián Gutiérrez", "sebasgut1");
        Friend f4 = new Friend("Mónica Álvarez", "monicaleja99");
        Friend f5 = new Friend("Laura Jiménez", "LauraMJimenezx2");
        Friend f6 = new Friend("Sergio Mejia", "iSergioMejia");
        contenido.add(f1); contenido.add(f2); contenido.add(f3);
        contenido.add(f4); contenido.add(f5); contenido.add(f6);

        FriendAdapter adapter = new FriendAdapter(getBaseContext(), contenido);
        listFriends.setAdapter(adapter);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getBaseContext(),"Accediendo a AddFriendActivity!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public class Friend{ //ESTO DEBE REEMPLAZARSE POR UNA CLASE DEL MODELO
        String nombre;
        String user;

        public Friend(String nombre, String user) {
            this.nombre = nombre;
            this.user = user;
        }

        public String getNombre() {
            return nombre;
        }

        public String getUser() {
            return user;
        }
    }

}