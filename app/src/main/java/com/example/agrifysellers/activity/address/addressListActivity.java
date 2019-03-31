package com.example.agrifysellers.activity.address;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ActivityAddressListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class addressListActivity extends AppCompatActivity {
    ActivityAddressListBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    Query query;
    AddressAdapter addressAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding= DataBindingUtil. setContentView(this,R.layout.activity_address_list);
     INIT();
     FireStoreInit();

    }

    private void FireStoreInit() {
        query = firebaseFirestore.collection("Sellers").document(auth.getCurrentUser().getUid()).collection("addressList");
        addressAdapter = new AddressAdapter(query, this) {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                super.onEvent(documentSnapshots, e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
        };
        binding.addressListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.addressListRv.setAdapter(addressAdapter);


    }
    @Override
    public void onStop() {
        super.onStop();
        if (addressAdapter != null) {
            addressAdapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary

        // Start listening for Firestore updates
        if (addressAdapter!= null) {
            addressAdapter.startListening();
        }
    }



    private void INIT() {
        binding.addressButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),addressActivity.class));
        });
        binding.appBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }


}
