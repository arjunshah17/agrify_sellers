package com.example.agrifysellers.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.viewHolder.ProductListHolder;
import com.example.agrifysellers.databinding.ItemProductListBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class ProductListAdapter extends FirestoreAdapter<ProductListHolder> {
    Activity activity;
    OnProductSelectedListener listener;

    public ProductListAdapter(Query query, Activity activity, OnProductSelectedListener listener) {
        super(query);
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product_list, parent, false);
        return new ProductListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHolder holder, int position) {
        holder.bind(getSnapshot(position), activity, listener);
    }

    public interface OnProductSelectedListener {

        void onProductSelected(DocumentSnapshot store);

    }

}
