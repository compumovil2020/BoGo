package com.example.bogo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bogo.Adapters.MensajeAdapter;
import com.example.bogo.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView lstMensajes;
    ImageButton btnSend;
    ImageView seguimiento;
    EditText edtSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        seguimiento = findViewById(R.id.btnSeguimiento);

        lstMensajes = findViewById(R.id.lstMensajes);
        btnSend = findViewById(R.id.btnSendMessage);
        edtSend = findViewById(R.id.edtMessageInput);

        final ArrayList<Mensaje> contenido = new ArrayList<>();
        String mensaje1 = "Hola, ¿Cómo vas?";
        String mensaje2 = "Bien ¿Y tú qué tal?";
        Mensaje nuevo1 = new Mensaje(mensaje1,true);
        Mensaje nuevo2 = new Mensaje(mensaje2,false);
        contenido.add(nuevo1);
        contenido.add(nuevo2);

        MensajeAdapter adapter = new MensajeAdapter(getBaseContext(),contenido);
        lstMensajes.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensaje = edtSend.getText().toString();
                if(!mensaje.isEmpty())
                {
                    Mensaje enviar =  new Mensaje(mensaje,true);
                    contenido.add(enviar);
                    MensajeAdapter adapNuevo = new MensajeAdapter(getBaseContext(),contenido);
                    lstMensajes.setAdapter(adapNuevo);
                    edtSend.setText("");
                }
            }
        });

        seguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"Abriendo el seguimiento de mi amigo", Toast.LENGTH_LONG).show();
                Intent i = new Intent(view.getContext(),FriendMapActivity.class);
                startActivity(i);
            }
        });
    }

    public class Mensaje{ //ESTO DEBE REEMPLAZARSE POR UNA CLASE DEL MODELO
        String mensaje;
        Boolean tipo; //False Recibido - True Enviado

        public Mensaje(String mensaje, Boolean tipo) {
            this.mensaje = mensaje;
            this.tipo = tipo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public Boolean getTipo() {
            return tipo;
        }
    }

}