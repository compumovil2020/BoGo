package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bogo.Adapters.MensajeAdapter;
import com.example.bogo.Entidades.Mensaje;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    String chatId;
    String amigoId;
    ArrayList<Mensaje> contenido;

    ListView lstMensajes;
    ImageButton btnSend;
    ImageView seguimiento;
    EditText edtSend;
    TextView nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        seguimiento = findViewById(R.id.btnSeguimiento);

        lstMensajes = findViewById(R.id.lstMensajes);
        btnSend = findViewById(R.id.btnSendMessage);
        edtSend = findViewById(R.id.edtMessageInput);
        nombre = findViewById(R.id.txtNombreChat);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final Intent intent = getIntent();
        amigoId = intent.getStringExtra("idAmigo");
        nombre.setText(intent.getStringExtra("nombreAmigo"));

        obtenerChat(mAuth.getUid());


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = edtSend.getText().toString();
                if(!mensaje.isEmpty())
                {
                    Long hora = Calendar.getInstance().getTimeInMillis();
                    Mensaje enviar =  new Mensaje(mensaje,mAuth.getUid(),hora );
                    contenido.add(enviar);
                    MensajeAdapter adapNuevo = new MensajeAdapter(getBaseContext(),contenido);
                    lstMensajes.setAdapter(adapNuevo);
                    edtSend.setText("");
                    enviarMensaje(enviar);
                }
            }
        });

        seguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Abriendo el seguimiento de mi amigo", Toast.LENGTH_LONG).show();
                Intent i = new Intent(view.getContext(),FriendMapActivity.class);
                i.putExtra("idamigo",amigoId);
                startActivity(i);
            }
        });
    }

    void obtenerChat(final String uid)
    {
        myRef = database.getReference(Utils.PATH_CHATS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> ids = new ArrayList<>();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    for(DataSnapshot users : dataSnap.getChildren())
                    {
                        ids.add(users.getKey());
                    }
                    if(ids.contains(uid) && ids.contains(amigoId))
                    {
                        chatId = dataSnap.getKey();
                    }
                    ids.clear();
                }
                verMensajes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void verMensajes()
    {
        myRef = database.getReference(Utils.PATH_MENSAJES + chatId+"/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contenido = new ArrayList<>();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                   Mensaje mensaje = dataSnap.getValue(Mensaje.class);
                   contenido.add(mensaje);
                }
                Collections.sort(contenido,new ComparadorHoras());
                MensajeAdapter adapter = new MensajeAdapter(getBaseContext(),contenido);
                lstMensajes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   void enviarMensaje(Mensaje nuevo){
        myRef = database.getReference(Utils.PATH_MENSAJES + chatId+"/");
        String key = myRef.push().getKey();
        myRef = database.getReference(Utils.PATH_MENSAJES + chatId +"/"+key);
        myRef.setValue(nuevo);
    }

    public static class ComparadorHoras implements Comparator<Mensaje> {

        @Override
        public int compare(Mensaje m1, Mensaje m2) {
            // TODO Auto-generated method stub
            return  m1.getFechaHoraEnviado().compareTo(m2.getFechaHoraEnviado());
        }

    }

}