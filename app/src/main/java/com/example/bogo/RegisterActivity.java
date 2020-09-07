package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.file.Files;

public class RegisterActivity extends AppCompatActivity {
    EditText edtNombreRegister, edtUserRegister, edtEmailRegister, edtPasswordRegister, edtConfirmPassword;
    Button btnRegister, btnFBRegister, btnGoogleRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtNombreRegister = findViewById(R.id.edtNombreRegister);
        edtUserRegister = findViewById(R.id.edtUserRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtNombreRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnFBRegister = findViewById(R.id.btnFBRegister);
        btnGoogleRegister = findViewById(R.id.btnGoogleRegister);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

        btnFBRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

        btnGoogleRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

    }
}