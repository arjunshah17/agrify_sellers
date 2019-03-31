package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.activity.products.model.Product;
import com.example.agrifysellers.databinding.ProductListItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public FirebaseFirestore db;
    public FirebaseAuth auth;
    ProductListItemBinding binding;
    public ProductListViewHolder(@NonNull ProductListItemBinding binding) {


        super(binding.getRoot());
        this.binding=binding;
        itemView.setOnCreateContextMenuListener(this);
    }
    Product product;
    public void bind(final DocumentSnapshot snapshot,
                     final ProductListAdapter.OnProductSelectedListner listener, final Activity activity, String TAG)
    {

        itemView.setOnClickListener(v -> {
   binding.productFoldingCell.toggle(false);
        });

        product=snapshot.toObject(Product.class);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("store").document(snapshot.getId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Store store = documentSnapshot.toObject(Store.class);
              product.setName(store.getName());
              product.setProductImageUrl(store.getProductImageUrl());

               binding.productTitleLayout.setProduct(product);
               binding.productContentLayout.setProduct(product);
               binding.productTitleLayout.productPrice.setText("₹"+String.valueOf( product.getPrice()));
                if (activity != null && store.getProductImageUrl()!=null) {
                    GlideApp.with(activity)
                            .load(product.getProductImageUrl())
                            .into(binding.productTitleLayout.productImage);
                    GlideApp.with(activity)
                            .load(product.getProductImageUrl())
                            .into(binding.productContentLayout.productImageContent);

                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists())
                {
                    product=snapshot.toObject(Product.class);
                    auth = FirebaseAuth.getInstance();
                    db = FirebaseFirestore.getInstance();

                    DocumentReference docRef = db.collection("store").document(snapshot.getId());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Store store = documentSnapshot.toObject(Store.class);
                            product.setName(store.getName());
                            product.setProductImageUrl(store.getProductImageUrl());
                            binding.productTitleLayout.setProduct(product);
                            binding.productContentLayout.setProduct(product);

                            if (activity != null && store.getProductImageUrl()!=null) {
                                GlideApp.with(activity)
                                        .load(product.getProductImageUrl())
                                        .into(binding.productTitleLayout.productImage);
                            }
                        }
                    });

                }
            }
        });



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("select an option");

        menu.add(getAdapterPosition(),1,1,"edit");
        menu.add(getAdapterPosition(),2,2,"remove");


    }
}

