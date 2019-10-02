package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


import com.example.agrifysellers.databinding.ProductDetailImageItemBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ProductImageFireStoreHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    ProductDetailImageItemBinding binding;

    boolean isEdited;
    public ProductImageFireStoreHolder(ProductDetailImageItemBinding binding) {
        super(binding.getRoot());
        this.binding=binding;

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(getAdapterPosition(),2,1,"remove");
    }

    public void bind(DocumentSnapshot snapshot, Activity activity) {
        String url=snapshot.getString("url");


        if (activity != null && url!=null) {
            Picasso.get()
                    .load(url)
                    .into(binding.productImageView);

        }
        itemView.setOnCreateContextMenuListener(this);

    }
}
