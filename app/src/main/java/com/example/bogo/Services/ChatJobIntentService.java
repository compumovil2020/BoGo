package com.example.bogo.Services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bogo.Activities.ChatActivity;
import com.example.bogo.Activities.MainMenuActivity;
import com.example.bogo.Entidades.Mensaje;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Example implementation of a JobIntentService.
 */
public class ChatJobIntentService extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;
    Mensaje ultimo;
    ArrayList<String> mChats;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ChatJobIntentService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Calendar cal =Calendar.getInstance();
        ultimo = new Mensaje("ultimo","alguien",cal.getTimeInMillis());

        mChats = new ArrayList<>();
        obtenerChats(mAuth.getUid());

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        myRef = database.getReference(Utils.PATH_MENSAJES);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Mensaje> mensajesRecibidos = new ArrayList<>();
                for (String chat:mChats)
                {
                    if(dataSnapshot.child(chat)!= null)
                    {
                        for (DataSnapshot mensajes : dataSnapshot.child(chat).getChildren())
                        {
                            Mensaje mensaje = mensajes.getValue(Mensaje.class);
                            mensajesRecibidos.add(mensaje);

                        }
                    }
                }
                Collections.sort(mensajesRecibidos, new ChatActivity.ComparadorHoras());
                if(!mensajesRecibidos.isEmpty())
                {
                    Mensaje ultimoRecibido = mensajesRecibidos.get(mensajesRecibidos.size()-1);
                    if(!ultimoRecibido.getRemitente().equals(mAuth.getUid())) {
                        if (ultimoRecibido.getFechaHoraEnviado() != ultimo.getFechaHoraEnviado()) {
                            ultimo = ultimoRecibido;
                            obtenerUsuario(ultimoRecibido);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerUsuario(final Mensaje ultimoRecibido)
    {
        myRef = database.getReference(Utils.PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario remitente = dataSnapshot.child(ultimoRecibido.getRemitente()).getValue(Usuario.class);
                buildAndShowNotification(remitente,ultimoRecibido);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void obtenerChats(final String uid)
    {
        myRef = database.getReference(Utils.PATH_CHATS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                for (DataSnapshot chats : dataSnapshot.getChildren()) {
                    for(DataSnapshot users : chats.getChildren())
                    {
                        if(users.getKey().equals(uid))
                        {
                            mChats.add(chats.getKey());
                        }
                    }
                }
                getUltimoMensaje();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUltimoMensaje()
    {
        myRef = database.getReference(Utils.PATH_MENSAJES);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Mensaje> mensajesRecibidos = new ArrayList<>();
                for (String chat:mChats)
                {
                    if(dataSnapshot.child(chat)!= null)
                    {
                        for (DataSnapshot mensajes : dataSnapshot.child(chat).getChildren())
                        {
                            Mensaje mensaje = mensajes.getValue(Mensaje.class);
                            if(!mensaje.getRemitente().equals(mAuth.getUid()))
                            {
                                mensajesRecibidos.add(mensaje);
                            }

                        }
                    }
                }
                Collections.sort(mensajesRecibidos, new ChatActivity.ComparadorHoras());
                if(!mensajesRecibidos.isEmpty())
                {
                    ultimo = mensajesRecibidos.get(mensajesRecibidos.size()-1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void  buildAndShowNotification(Usuario remitente, Mensaje mensaje)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, MainMenuActivity.CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.bogoround);
        mBuilder.setContentTitle(remitente.getNombre());
        mBuilder.setContentText(mensaje.getTexto());
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this,ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("idAmigo",mensaje.getRemitente());
        intent.putExtra("nombreAmigo",remitente.getNombre());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        String latiud = String.valueOf(remitente.getLatitud());
        int notificationid = Integer.parseInt(latiud.substring(latiud.length()-4,latiud.length()-1));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationid,mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    final Handler mHandler = new Handler();

    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                Toast.makeText(ChatJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}