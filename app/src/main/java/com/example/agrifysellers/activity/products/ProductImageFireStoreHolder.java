package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.databinding.ProductDetailImageItemBinding;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProductImageFireStoreHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    ProductDetailImageItemBinding binding;

    boolean isEdited;
    public ProductImageFireStoreHolder(ProductDetailImageItemBinding binding, boolean isEdited) {
        super(binding.getRoot());
        this.isEdited=isEdited;
        this.binding=binding;

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(getAdapterPosition(),1,1,"remove");
    }

    public void bind(DocumentSnapshot snapshot, Activity activity) {
        String url=snapshot.getString("url");

        if (activity != null && url!=null) {
            GlideApp.with(activity)
                    .load(url)
                    .into(binding.productImageView);

        }

    }
}
