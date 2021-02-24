package com.example.carlocker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.carlocker.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();

                if(mAuth.getCurrentUser() == null) {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }, 2000);




        //FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        //    @Override
        //    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //        if(firebaseAuth != null)
        //        {
        //            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        //            startActivity(intent);
        //        }
        //        else
        //        {
        //            Toast.makeText(SplashActivity.this, "Non sei loggato", Toast.LENGTH_SHORT).show();
        //        }
        //    }
        //};

    }
}