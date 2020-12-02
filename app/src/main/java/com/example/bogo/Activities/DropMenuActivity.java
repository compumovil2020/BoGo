package com.example.bogo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bogo.Entidades.Usuario;
import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class DropMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference mStorageRef;

    TextView name, username, points, txtWeather;
    ImageView profilePicture;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainMenu, R.id.timeLineActivity, R.id.calendarActivity,
                R.id.wishListActivity, R.id.favoritesListActivity, R.id.fullMapActivity2, R.id.addPlaceActivity,
                R.id.redeemPointsActivity
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Button btnSearch = findViewById(R.id.btnGotoSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ConstraintLayout clLogout = findViewById(R.id.cl_logout);
        clLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getBaseContext(), BienvenidoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        View clProfile = navigationView.getHeaderView(0);
        clProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        name = clProfile.findViewById(R.id.txtNameProfile);
        username = clProfile.findViewById(R.id.txtUsernameProfile);
        points = clProfile.findViewById(R.id.txtNumPuntosProfile);
        profilePicture = clProfile.findViewById(R.id.imgDropMenuFoto);
        txtWeather = findViewById(R.id.txtWeather);

        getUserInfo();
        getWeatherInfo();

    }

    private void getWeatherInfo()
    {
        Log.i("TEMPERATURA", "ENTRE A F");
        String requestURL = "http://api.weatherapi.com/v1/current.json?key=b0f85ca5e6d54127bd6134426203011&q=Bogota&lang=es";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if( response != null)
                        {
                            try {
                                double temp_c = response.getJSONObject("current").getDouble("temp_c");
                                String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");

                                String mensaje = "";
                                if(temp_c > 18)
                                {
                                    mensaje = "Estamos a "+((int)temp_c)+" °C! Mejor lleva algo fresco. El tiempo es "+condition;
                                }
                                else
                                {
                                    mensaje = "Estamos a "+((int)temp_c)+" °C! Mejor ponte un suéter. El tiempo es "+condition;
                                }
                                txtWeather.setText(mensaje);

                            } catch (JSONException e) {
                                Log.i("TEMPERATURA", "ERROR: "+e.getLocalizedMessage());
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TEMPERATURA", "Error handling rest invocation"+error.getCause());
                    }
                }
        );
        queue.add(req);
    }

    private void getUserInfo()
    {
        String UID = mAuth.getUid();
        myRef = database.getReference(Utils.PATH_USUARIOS+UID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                name.setText(usuario.getNombre());
                username.setText(usuario.getNombreUsuario());
                String p = ""+usuario.getPuntos();
                p = ( usuario.getPuntos() == 1 ) ? ( p + " punto" ) : ( p + " puntos");
                points.setText(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            downloadPhoto(Utils.PATH_USUARIOS+UID+"/profile.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drop_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void downloadPhoto(String path) throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        StorageReference imageRef = mStorageRef.child(path);
        imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                profilePicture.setImageURI(Uri.fromFile(localFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}