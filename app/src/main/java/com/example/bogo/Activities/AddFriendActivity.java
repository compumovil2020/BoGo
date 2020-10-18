package com.example.bogo.Activities;

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

import com.example.bogo.Adapters.AddFriendAdapter;
import com.example.bogo.R;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    ListView lstNewFriends;
    EditText edtSearchFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        lstNewFriends = findViewById(R.id.lstFindFriends);
        edtSearchFriend = findViewById(R.id.edtSearchFriend);

        final ArrayList<AddFriendActivity.NewFriend> contenido = new ArrayList<>();
        AddFriendActivity.NewFriend f1 = new AddFriendActivity.NewFriend("Aldemar Ramirez", "aldemar_avila");
        AddFriendActivity.NewFriend f2 = new AddFriendActivity.NewFriend("Sebastián Balaguera", "sebasbala");
        AddFriendActivity.NewFriend f3 = new AddFriendActivity.NewFriend("Héctor Rodriguez", "hectoren96");
        AddFriendActivity.NewFriend f4 = new AddFriendActivity.NewFriend("Santiago Palacios", "santiagopalaciosl");
        AddFriendActivity.NewFriend f5 = new AddFriendActivity.NewFriend("Juan Camacho", "thejuanshow");
        AddFriendActivity.NewFriend f6 = new AddFriendActivity.NewFriend("Johan Ortegon", "ortegonjohan");
        AddFriendActivity.NewFriend f7 = new AddFriendActivity.NewFriend("David Halaby", "dmhalaby");
        AddFriendActivity.NewFriend f8 = new AddFriendActivity.NewFriend("Camilo Moreno", "camilomoreno13");
        AddFriendActivity.NewFriend f9 = new AddFriendActivity.NewFriend("Camilo Cruz", "crz");
        AddFriendActivity.NewFriend f10 = new AddFriendActivity.NewFriend("Camilo Ruiz", "stickyruiz");
        contenido.add(f1);
        contenido.add(f2);
        contenido.add(f3);
        contenido.add(f4);
        contenido.add(f5);
        contenido.add(f6);
        contenido.add(f7);
        contenido.add(f8);
        contenido.add(f9);
        contenido.add(f10);

        AddFriendAdapter adapter = new AddFriendAdapter(getBaseContext(), contenido);
        lstNewFriends.setAdapter(adapter);

        edtSearchFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH)
                {
                    String nameFriend = edtSearchFriend.getText().toString();

                    if(!nameFriend.isEmpty())
                    {
                        ArrayList<AddFriendActivity.NewFriend> contenidoAct = new ArrayList<>();
                        for (AddFriendActivity.NewFriend friend: contenido) {
                            if((friend.user.contains(nameFriend.toLowerCase()))||(friend.nombre.toLowerCase().contains(nameFriend.toLowerCase())))
                            {
                                contenidoAct.add(friend);
                            }
                        }
                        AddFriendAdapter newadapter = new AddFriendAdapter(getBaseContext(), contenidoAct);
                        lstNewFriends.setAdapter(newadapter);
                        onStop();
                    }
                    else
                    {
                        edtSearchFriend.setError("Required");
                    }
                }
                    return false;
            }

        });
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