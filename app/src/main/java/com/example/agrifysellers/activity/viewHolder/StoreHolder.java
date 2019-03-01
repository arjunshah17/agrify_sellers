package com.example.agrifysellers.activity.viewHolder;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.adapter.StoreAdapter;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ItemStoreProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.varunest.sparkbutton.SparkEventListener;

import es.dmoral.toasty.Toasty;


public class StoreHolder extends RecyclerView.ViewHolder {
    public ItemStoreProductBinding binding;
    public FirebaseFirestore db;
    public FirebaseAuth auth;

    public StoreHolder(@NonNull ItemStoreProductBinding item) {
        super(item.getRoot());
        binding = item;
    }


    public void bind(final DocumentSnapshot snapshot,
                     final StoreAdapter.OnStoreSelectedListener listener, final Activity activity) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final Store store = snapshot.toObject(Store.class);
        Resources resources = itemView.getResources();

        binding.setStore(store);

        // Load image
        if (activity != null) {
            GlideApp.with(activity)
                    .load(store.getProductImageUrl())
                    .into(binding.productImage);
        }
        binding.wiseButton.setEventListener(new SparkEventListener() {


            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    Toasty.success(activity, store.getName() + " added to wishlist", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.info(activity, store.getName() + " remove from wishlist", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        // Click listener

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