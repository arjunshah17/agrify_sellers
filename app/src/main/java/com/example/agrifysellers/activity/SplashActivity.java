package com.example.agrifysellers.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agrifysellers.activity.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            signIn();
        } else {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    private void signIn() {
        startActivity(new Intent(this, LoginActivity.class));
    }


}
