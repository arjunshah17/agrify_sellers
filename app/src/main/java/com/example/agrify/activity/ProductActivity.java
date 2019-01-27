package com.example.agrify.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.agrify.R;
import com.example.agrify.activity.model.Store;
import com.example.agrify.databinding.ActivityProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import es.dmoral.toasty.Toasty;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;
    FirebaseUser firebaseUser;
    Store store;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Uri mainImageURI = null;
    private boolean isLoaded = false;
    private StorageReference storageReference;
    private Bitmap compressedImageFile;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            binding.productLinearLayout.setBackground(this.getDrawable(R.drawable.store_item_background));//set curve background
        }
        store = new Store();
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser = firebaseAuth.getCurrentUser();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store.setCategory(binding.catSpinner.getSelectedItem().toString());
                store.setName(binding.productName.getText().toString());
                store.setDes(binding.productdesTextview.getText().toString());

                firebaseFirestore.collection("store").add(store).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(ProductActivity.this, "product added", Toasty.LENGTH_LONG).show();
                            startActivity(new Intent(ProductActivity.this, MainActivity.class));
                        } else {
                            Toasty.error(ProductActivity.this, "error in uploading data", Toasty.LENGTH_LONG).show();
                        }
                    }
                });
            }


        });
    }
}
