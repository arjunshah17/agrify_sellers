package com.example.agrifysellers.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ActivityProductBinding;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import io.opencensus.tags.Tag;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;
    FirebaseUser firebaseUser;
    Store store;
    String TAG="ProductActivity";
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Uri mainImageURI = null;
    private boolean isLoaded = false;
    private StorageReference storageReference;
    private Bitmap compressedImageFile;
    private FirebaseAuth firebaseAuth;
Boolean isChanged=false;
      Query mQuery;

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
        storageReference = FirebaseStorage.getInstance().getReference();
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

              DocumentReference defRef  =firebaseFirestore.collection("store").document(binding.productName.getText().toString());
               defRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toasty.error(ProductActivity.this,"data already exists",Toasty.LENGTH_LONG).show();
                            } else {
                                uploadData();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


            }


        });

        binding.productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(ProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(ProductActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                         Toasty.error(ProductActivity.this,"permission not granted",Toasty.LENGTH_LONG).show();

                    }
                    BringImagePicker();


                } else {

                    BringImagePicker();

                }
            }
        });





    }

    private void uploadData() {

        store.setCategory(binding.catSpinner.getSelectedItem().toString());
        store.setName(binding.productName.getText().toString());




        if (isChanged ) {
dataLoading(true);

            File newImageFile = new File(mainImageURI.getPath());
            try {

                compressedImageFile = new Compressor(ProductActivity.this)
                        .setMaxHeight(125)
                        .setMaxWidth(125)                                             //compressing image
                        .setQuality(100)
                        .compressToBitmap(newImageFile);

            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);   //converting into bitmap
            byte[] thumbData = baos.toByteArray();

            final StorageReference ref = storageReference.child("storeProductImage").child(store.getName() + ".jpg");
            UploadTask image_path = ref.putBytes(thumbData);                                                         //uploading image

            Task<Uri> urlTask = image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                //getting image reference

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        store.setProductImageUrl(downloadUri.toString());
                        store.setCategory(binding.catSpinner.getSelectedItem().toString());
                        store.setName(binding.productName.getText().toString());
                        store.setDes(binding.productdesTextview.getText().toString());

                        firebaseFirestore.collection("store").document(store.getName()).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dataLoading(false);
                                    Toasty.success(ProductActivity.this, "product added", Toasty.LENGTH_LONG).show();
                                    startActivity(new Intent(ProductActivity.this, MainActivity.class));
                                } else {
                                    Toasty.error(ProductActivity.this, "error in uploading data", Toasty.LENGTH_LONG).show();
                                }
                            }
                        });


                    } else {
                        // Handle failures
                        Toast.makeText(ProductActivity.this, "error on uploading image", Toast.LENGTH_LONG).show();

                        // ...
                    }
                }
            });


        }

    }


    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(ProductActivity.this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                Log.i("onactresultresult", "mainurl:" + mainImageURI.toString());

              binding.productImageView.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(ProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }

        }
        void dataLoading(boolean state)
        {
            if (state)
            {
                binding.loaderLayout.setVisibility(View.GONE);
                binding.loaderProgressBar.setVisibility(View.VISIBLE);
            }
            else {
                binding.loaderProgressBar.setVisibility(View.GONE);
                binding.loaderLayout.setVisibility(View.VISIBLE);

            }
        }

    }

