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
import com.example.bogo.Entidades.Usuario;
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

        final ArrayList<Usuario> contenido = new ArrayList<>();

        AddFriendAdapter adapter = new AddFriendAdapter(getBaseContext(), contenido);
        lstNewFriends.setAdapter(adapter);

        edtSearchFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String nameFriend = edtSearchFriend.getText().toString();

                    if (!nameFriend.isEmpty()) {
                        ArrayList<Usuario> contenidoAct = new ArrayList<>();
                        for (Usuario friend : contenido) {
                            if ((friend.getNombreUsuario().contains(nameFriend.toLowerCase())) || (friend.getNombre().toLowerCase().contains(nameFriend.toLowerCase()))) {
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
    }
}

