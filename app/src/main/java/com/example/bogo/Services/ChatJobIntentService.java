package com.example.bogo.Services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bogo.Activities.ChatActivity;
import com.example.bogo.Entidades.Mensaje;
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
                    Mensaje ultimoRecibido = mensajesRecibidos.get(mensajesRecibidos.size()-1);
                    if(ultimoRecibido.getFechaHoraEnviado() != ultimo.getFechaHoraEnviado())
                    {
                        ultimo = ultimoRecibido;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void obtenerChats(final String uid)
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

   /* public void  buildAndShowNotification(Usuario disponible)
    {
        Log.i("myLoc", disponible.getUI());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,HomeActivity.CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.comprobar);
        mBuilder.setContentTitle(disponible.getNombre() + " "+ disponible.getApellido() + " se encuentra disponible");
        mBuilder.setContentText("Oprime para verlo en el mapa");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this,FriendMapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("UID",disponible.getUI());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        int notificationid = Integer.parseInt(disponible.getIdentificacion().substring(0,4));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationid,mBuilder.build());
    }*/

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