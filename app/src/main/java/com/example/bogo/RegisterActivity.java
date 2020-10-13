package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.file.Files;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "REGISTER";
    EditText edtNombreRegister, edtUserRegister, edtEmailRegister, edtPasswordRegister, edtConfirmPassword;
    Button btnRegister, btnFBRegister, btnGoogleRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtNombreRegister = findViewById(R.id.edtNombreRegister);
        edtUserRegister = findViewById(R.id.edtUserRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnFBRegister = findViewById(R.id.btnFBRegister);
        btnGoogleRegister = findViewById(R.id.btnGoogleRegister);
        mAuth = FirebaseAuth.getInstance();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        btnFBRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

        btnGoogleRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signUp() {
        String email = this.edtEmailRegister.getText().toString();
        String password = this.edtPasswordRegister.getText().toString();

        boolean validEmail = Utils.validateEmail(email);
        boolean validPass = Utils.validatePassword(password);
        boolean validFields = true;

        if (!validEmail) {
            if (email.isEmpty()) {
                edtEmailRegister.setError("Requerido");
            } else {
                edtEmailRegister.setError("E-mail no válido");
            }
        } else edtEmailRegister.setError(null);
        if (!validPass) {
            if (password.isEmpty()) {
                edtPasswordRegister.setError("Requerido");
            } else {
                edtPasswordRegister.setError("6 caracteres mínimos");
            }

        } else
        {
            if(password.equals(edtConfirmPassword.getText().toString()))
            edtPasswordRegister.setError(null);
            else
            {
                edtConfirmPassword.setError("Las contraseñas no son iguales");
                validPass = false;
            }
        }

        if(edtNombreRegister.getText().toString().isEmpty())
        {
            validFields = false;
            edtNombreRegister.setError("Requerido");
        }
        if(edtUserRegister.getText().toString().isEmpty())
        {
            validFields = false;
            edtUserRegister.setError("Requerido");
        }

        if (validEmail && validPass && validFields)
        {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser mUser) {
        if (mUser != null) {
            Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    "No se pudo registrar el usuario nuevo",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}