package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bogo.R;
import com.example.bogo.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN";
    EditText edtUser, edtPasswordLogin;
    Button btnLogin, btnFBLogin, btnGoogleLogin, btnRegisterLogin;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loginUser();
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

    private void loginUser()
    {
        String email = this.edtUser.getText().toString();
        String password = this.edtPasswordLogin.getText().toString();

        boolean validEmail = Utils.validateEmail(email);
        boolean validPass = Utils.validatePassword(password);

        if(!validEmail)
        {
            if(email.isEmpty())
            {
                edtUser.setError("Requerido");
            }else
            {
                edtUser.setError("E-mail no válido");
            }
        }
        else edtUser.setError(null);
        if(!validPass)
        {
            if(password.isEmpty())
            {
                edtPasswordLogin.setError("Requerido");
            }else
            {
                edtPasswordLogin.setError("6 caracteres mínimos");
            }

        }
        else edtPasswordLogin.setError(null);

        if(validEmail && validPass)
        {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
        }
    }

    private void updateUI(FirebaseUser mUser)
    {
        if(mUser != null)
        {
            Intent intent = new Intent(getBaseContext(), DropMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else
        {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,
                    "El correo electrónico o contraseña que ingresaste no coincide con ninguna cuenta.",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}