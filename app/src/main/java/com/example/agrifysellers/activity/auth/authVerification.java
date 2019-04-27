package com.example.agrifysellers.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ActivityAuthVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class authVerification extends AppCompatActivity {
    ActivityAuthVerificationBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_verification);

        firebaseAuth = FirebaseAuth.getInstance();
        initializeGUI();
        setSupportActionBar(binding.bgHeader);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initializeGUI() {
        binding.sendVerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressDialog(true);
                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            showProgressDialog(false);
                            if (task.isSuccessful()) {
                                Toasty.success(authVerification.this, "verification email send to " + firebaseAuth.getCurrentUser().getEmail(), Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(authVerification.this, LoginActivity.class));
                                Bungee.inAndOut(authVerification.this);
                            } else {
                                Toasty.error(authVerification.this, task.getException().getLocalizedMessage(), Toasty.LENGTH_SHORT).show();


                            }

                        }
                    });
                } else {
                    Toasty.error(authVerification.this, "sign in first to verify", Toasty.LENGTH_SHORT).show();
                    startActivity(new Intent(authVerification.this, LoginActivity.class));
                    Bungee.inAndOut(authVerification.this);
                }

            }
        });



    }

    private void showProgressDialog(Boolean state) {


        if (state) {

            binding.progressLoading.setVisibility(View.VISIBLE);
        } else {
            binding.progressLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(authVerification.this, LoginActivity.class));
        Bungee.inAndOut(authVerification.this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
