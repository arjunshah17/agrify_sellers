package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.FirestoreAdapter;
import com.example.agrifysellers.databinding.ProductDetailImageItemBinding;
import com.google.firebase.firestore.Query;

public class ProductImageFireStoreAdapter extends FirestoreAdapter<ProductImageFireStoreHolder> {


ProductDetailImageItemBinding binding;
Activity activity;

    public ProductImageFireStoreAdapter(Query query, Activity activity) {

        super(query);

        this.activity=activity;

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    void removeImage(int pos)
    {

    }


    @NonNull
    @Override
    public ProductImageFireStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_detail_image_item,parent,false);
        return new ProductImageFireStoreHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageFireStoreHolder holder, int position) {

        holder.bind(getSnapshot(position),activity);



    }
}
