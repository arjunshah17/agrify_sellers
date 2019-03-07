package com.example.agrifysellers.activity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.sellerProduct.SellerProductActivity;
import com.example.agrifysellers.activity.viewHolder.SellerHolder;
import com.example.agrifysellers.databinding.ItemSellerBinding;
import com.google.firebase.firestore.Query;

public class SellerAdapter extends FirestoreAdapter<SellerHolder> {
    Activity activity;
    public SellerAdapter(Query query, Activity activity) {
        super(query);
        this.activity = activity;

    }


    @NonNull
    @Override
    public SellerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSellerBinding itemSellerBinding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.item_seller, parent, false);


        return new SellerHolder(itemSellerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerHolder holder, int position) {
        holder.bind(getSnapshot(position), activity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), "hi", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(holder.itemView.getContext(), SellerProductActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }
}
