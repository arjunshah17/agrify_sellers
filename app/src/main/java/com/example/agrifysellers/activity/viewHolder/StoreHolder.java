package com.example.agrifysellers.activity.viewHolder;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.adapter.StoreAdapter;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ItemStoreProductBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;


public class StoreHolder extends RecyclerView.ViewHolder {
    public ItemStoreProductBinding binding;
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    Store store;
    public StoreHolder(@NonNull ItemStoreProductBinding item) {
        super(item.getRoot());
        binding = item;

    }


    public void bind(final DocumentSnapshot snapshot,
                     final StoreAdapter.OnStoreSelectedListener listener, final Activity activity, String TAG) {
        store = new Store();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (TAG.equals("StoreFragment")) {
            store = snapshot.toObject(Store.class);


            binding.setStore(store);
            if(store.getPrice()==0)
            {
                binding.price.setText("no seller is selling");
            }
            else {
                binding.price.setText("start from ₹" + String.valueOf(store.getPrice()) + "/" + store.getUnit());
            }
            // Load image
            if (activity != null) {
                GlideApp.with(activity)
                        .load(store.getProductImageUrl())
                        .into(binding.productImage);
            }

        }
        else {

            DocumentReference docRef = db.collection("store").document(snapshot.getId());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    store = documentSnapshot.toObject(Store.class);
                    binding.setStore(store);
                    if(store.getPrice()==0)
                    {
                        binding.price.setText("no seller is selling");
                    }
                    else {
                        binding.price.setText("start from ₹" + String.valueOf(store.getPrice()) + "/" + store.getUnit());
                    }
                    if (activity != null && store.getProductImageUrl()!=null) {
                        GlideApp.with(activity)
                                .load(store.getProductImageUrl())
                                .into(binding.productImage);
                    }
                }
            });
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (snapshot != null && snapshot.exists()) {


                        store = documentSnapshot.toObject(Store.class);
                        binding.setStore(store);
                        if(store.getPrice()==0)
                        {
                            binding.price.setText("no seller is selling");
                        }
                        else {
                            binding.price.setText("start from ₹" + String.valueOf(store.getPrice()) + "/" + store.getUnit());
                        }

                        // Load image
                        if (store.getProductImageUrl() != null) {
                            if (activity != null) {
                                GlideApp.with(activity)
                                        .load(store.getProductImageUrl())
                                        .into(binding.productImage);
                            }
                        }
                    }
                }
            });
            // Click listener


        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    View SharedView = binding.productImage;

                    listener.onStoreSelected(snapshot, SharedView);
                }
            }
        });
    }

    }


