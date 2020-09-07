package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPasswordLogin;
    Button btnLogin, btnFBLogin, btnGoogleLogin, btnRegisterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnFBLogin = findViewById(R.id.btnFBLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnRegisterLogin = findViewById(R.id.btnRegisterLogin);
        edtUser = findViewById(R.id.edtUserLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });
        btnFBLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent (getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}