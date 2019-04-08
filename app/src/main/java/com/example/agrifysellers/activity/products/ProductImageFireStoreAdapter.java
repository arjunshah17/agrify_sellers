package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.FirestoreAdapter;
import com.example.agrifysellers.databinding.ProductDetailImageItemBinding;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ProductImageFireStoreAdapter extends FirestoreAdapter<ProductImageFireStoreHolder> {

boolean isEdited;
ProductDetailImageItemBinding binding;
Activity activity;
    private ArrayList<Uri> localAddedUrlList;
    public ProductImageFireStoreAdapter(Query query, boolean isEdited, Activity activity,ArrayList<Uri> localAddedUrlList) {

        super(query);
        isEdited=this.isEdited;
        this.activity=activity;
        this.localAddedUrlList=localAddedUrlList;
    }

    void removeImage(int pos)
    {

    }


    @NonNull
    @Override
    public ProductImageFireStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_detail_image_item,parent,false);
        return new ProductImageFireStoreHolder(binding,isEdited);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageFireStoreHolder holder, int position) {

        holder.bind(getSnapshot(position),activity);
      // binding.productImageView.setImageURI(localAddedUrlList.get(position));


    }
}
