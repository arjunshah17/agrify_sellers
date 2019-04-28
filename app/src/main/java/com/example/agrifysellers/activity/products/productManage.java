package com.example.agrifysellers.activity.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.databinding.ActivityProductManageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;

import java.text.NumberFormat;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;

import static com.example.agrifysellers.activity.Utils.internetConnectionUtils.isInternetConnected;

public class productManage extends AppCompatActivity {
    String seller_id;
    String product_id;
    String TAG = "SellerProductActivity";
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    int stock;
    DocumentReference sellerRef;
    DocumentReference storeSellerRef;
    WriteBatch batch;
    Seller seller;
    ActivityProductManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_manage);

        if (getIntent().getStringExtra("seller_id") != null && getIntent().getStringExtra("product_id") != null) {
            product_id = getIntent().getStringExtra("product_id");
            seller_id = getIntent().getStringExtra("seller_id");
            Log.i("product_id", product_id);
            Log.i("seller_id", seller_id);

        }
        binding.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        batch=firebaseFirestore.batch();
        storeSellerRef=firebaseFirestore.collection("store").document(product_id).collection("sellerList").document(auth.getUid());
        sellerRef = firebaseFirestore.collection("Sellers").document(auth.getUid()).collection("productList").document(product_id);
        sellerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                seller = snapshot.toObject(Seller.class);
                binding.stockTextview.setText(NumberFormat.getInstance().format(seller.getStock()));
                binding.avalibitySwitch.setChecked(seller.isAvalibity());
            }
        });
        binding.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
if(isInternetConnected(getApplicationContext())) {
    batch=firebaseFirestore.batch();
    batch.update(sellerRef, "stock", FieldValue.increment(Integer.parseInt(binding.stockEditText.getText().toString())));
  batch.update(storeSellerRef,"stock", FieldValue.increment(Integer.parseInt(binding.stockEditText.getText().toString())));
  batch.commit();
}
                }
                catch (Exception e)
                {

                }
            }
        });
        binding.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

if(isInternetConnected(getApplicationContext()))
                    {
                        if (Integer.parseInt(binding.stockEditText.getText().toString()) < seller.getStock())
                        {batch=firebaseFirestore.batch();
                            batch.update(sellerRef, "stock", FieldValue.increment(- Integer.parseInt(binding.stockEditText.getText().toString())));
                            batch.update(storeSellerRef,"stock", FieldValue.increment(- Integer.parseInt(binding.stockEditText.getText().toString())));
                            batch.commit();
                        }
                        else Toasty.error(getApplicationContext(),"decremented value must be less stock");
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        binding.avalibitySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
if(isInternetConnected(getApplicationContext())) {
    batch=firebaseFirestore.batch();
    batch.update(storeSellerRef,"avalibity", binding.avalibitySwitch.isChecked());
    batch.update(sellerRef,"avalibity", binding.avalibitySwitch.isChecked());
    batch.commit();

}
else binding.avalibitySwitch.setChecked(!binding.avalibitySwitch.isChecked());

                }
                catch (Exception ex)
                {

                }
            }
        });


    }
}
