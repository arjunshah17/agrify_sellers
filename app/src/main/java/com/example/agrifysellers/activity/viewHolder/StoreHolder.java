package com.example.agrifysellers.activity.viewHolder;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.adapter.StoreAdapter;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ItemStoreProductBinding;
import com.google.firebase.firestore.DocumentSnapshot;


public class StoreHolder extends RecyclerView.ViewHolder {
    public ItemStoreProductBinding binding;


    public StoreHolder(@NonNull ItemStoreProductBinding item) {
        super(item.getRoot());
        binding = item;
    }


    public void bind(final DocumentSnapshot snapshot,
                     final StoreAdapter.OnStoreSelectedListener listener, Activity activity) {

        Store store = snapshot.toObject(Store.class);
        Resources resources = itemView.getResources();

        binding.setStore(store);

        // Load image
        if(activity!=null) {
            GlideApp.with(activity)
                    .load(store.getProductImageUrl())
                    .into(binding.productImage);
        }
        // Click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onStoreSelected(snapshot);
                }
            }
        });
    }
}