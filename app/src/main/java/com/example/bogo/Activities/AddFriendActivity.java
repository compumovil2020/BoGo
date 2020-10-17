package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.bogo.Adapters.AddFriendAdapter;
import com.example.bogo.Adapters.FriendAdapter;
import com.example.bogo.R;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    ListView lstNewFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        lstNewFriends = findViewById(R.id.lstFindFriends);

        ArrayList<AddFriendActivity.NewFriend> contenido = new ArrayList<>();
        AddFriendActivity.NewFriend f1 = new AddFriendActivity.NewFriend("Pepe Pérez", "ppperez12");
        AddFriendActivity.NewFriend f2 = new AddFriendActivity.NewFriend("Julian Parada", "JulianParada");
        AddFriendActivity.NewFriend f3 = new AddFriendActivity.NewFriend("Sebastián Gutiérrez", "sebasgut1");
        AddFriendActivity.NewFriend f4 = new AddFriendActivity.NewFriend("Mónica Álvarez", "monicaleja99");
        AddFriendActivity.NewFriend f5 = new AddFriendActivity.NewFriend("Laura Jiménez", "LauraMJimenezx2");
        AddFriendActivity.NewFriend f6 = new AddFriendActivity.NewFriend("Sergio Mejia", "iSergioMejia");
        contenido.add(f1); contenido.add(f2); contenido.add(f3);
        contenido.add(f4); contenido.add(f5); contenido.add(f6);

        AddFriendAdapter adapter = new AddFriendAdapter(getBaseContext(), contenido);
        lstNewFriends.setAdapter(adapter);


    }

    public class NewFriend{ //ESTO DEBE REEMPLAZARSE POR UNA CLASE DEL MODELO
        String nombre;
        String user;

        public NewFriend(String nombre, String user) {
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