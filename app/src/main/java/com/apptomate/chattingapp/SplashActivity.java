package com.apptomate.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apptomate.chattingapp.activities.home.MainActivity;
import com.apptomate.chattingapp.activities.login.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (auth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }

            }
        },  1000);
    }
}