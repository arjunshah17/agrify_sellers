package com.example.agrifysellers.activity.viewHolder;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.adapter.ProductListAdapter;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ItemProductListBinding;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProductListHolder extends RecyclerView.ViewHolder {
    ItemProductListBinding binding;

    public ProductListHolder(@NonNull ItemProductListBinding item) {
        super(item.getRoot());
        binding = item;


    }

    public void bind(final DocumentSnapshot snapshot, Activity activity, final ProductListAdapter.OnProductSelectedListener listener) {

        Store store = snapshot.toObject(Store.class);
        binding.setStore(store);
        if (store.getProductImageUrl() != null && activity != null) {
            GlideApp.with(activity)
                    .load(store.getProductImageUrl())
                    .into(binding.productImageUrl);

        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {


                    listener.onProductSelected(snapshot);
                }
            }
        });
    }
}
