package com.example.agrifysellers.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.SellerAdapter;
import com.example.agrifysellers.activity.fragments.SellerListFragment;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ActivityStoreDetailBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;



public class StoreDetailActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {
    private static final String TAG = "StoreDetail";
    public static final String KEY_STORE_ID = "key_store_id";


    private FirebaseFirestore mFirestore;
    private DocumentReference mStoreRef;
    private ListenerRegistration mStoreRegistration;
    ActivityStoreDetailBinding bind;
    Query sellerQuery;
    SellerAdapter mAdapter;
    SellerListFragment sellerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = DataBindingUtil.setContentView(this, R.layout.activity_store_detail);
        bind.productDes.setMovementMethod(new ScrollingMovementMethod());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            bind.acitvityDetailLayout.setBackground(this.getDrawable(R.drawable.store_item_background));//set curve background
        }

        String storeId = getIntent().getExtras().getString(KEY_STORE_ID);
        sellerListFragment=new SellerListFragment(storeId);
        if (storeId == null) {
            throw new IllegalArgumentException("Must pass extra " + storeId);
        }

        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the restaurant
        mStoreRef = mFirestore.collection("store").document(storeId);

   //     getSellerList();


        bind.appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        mStoreRegistration = mStoreRef.addSnapshotListener(this);
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

        StoreLoaded(snapshot.toObject(Store.class));
    }

    private void StoreLoaded(Store store) {
        bind.setStore(store);
        bind.sellerlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(store.getSellerCount()!=0)
                sellerListFragment.show(getSupportFragmentManager(),"seller list");
                else
                    Toasty.info(getApplicationContext(),"no farmers are selling",Toasty.LENGTH_SHORT).show();
            }
        });
        if(store.getSellerCount()==0)
        {
            bind.sellerlistButton.setText("no seller found");
        }
        else {
            bind.sellerlistButton.setText(store.getSellerCount()+" farmers are selling");
        }
        Picasso.get()
                .load(store.getProductImageUrl())
                .into(bind.productImageUrl);
        bind.appBar.setTitle(store.getName());
        stateLoading(false);

    }




    private void stateLoading(boolean stateStatus) {
        if (stateStatus) {
            bind.acitvityDetailLayout.setVisibility(View.GONE);
            bind.progressLoading.setVisibility(View.VISIBLE);
        } else {
            bind.acitvityDetailLayout.setVisibility(View.VISIBLE);
            bind.progressLoading.setVisibility(View.GONE);
        }
    }

}
