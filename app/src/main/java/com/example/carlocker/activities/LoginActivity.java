package com.example.carlocker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.carlocker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inizializzo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser()
    {
        String newMail = email.getText().toString().trim();
        String newPassword = password.getText().toString().trim();

        if (newMail.isEmpty()) {
            email.setError("Email required");
            email.requestFocus();
            return;
        }

        if(newPassword.isEmpty()) {
            password.setError("Password required");
            password.requestFocus();
            return;
        }

        if(newPassword.length() < 6) {
            password.setError("Password should be at least 6 char long!");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(newMail, newPassword)
                // Questo metodo verrà eseguito al completamento dell'autenticazione
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Se viene eseguito con successo il login, bisogna far startare una
                            // nuova activity
                            startMainActivity();
                        }
                        else {
                            // Se il task non è eseguito con successo, vedo qual è il problema.
                            // Questo primo caso avviene quando l'email inserita esiste già nel db
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // Se la mail esiste di già, vuol dire che possiamo loggare l'user
                                userLogin(newMail, newPassword);
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainActivity();
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        // Vogliamo che la nuova activity venga startata come fresh activity, quindi schiacciando
        // il tasto indietro non deve tornare al login. PEr farlo bisogna aggiundere delle flags
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}