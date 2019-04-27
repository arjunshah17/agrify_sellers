package com.example.agrifysellers.activity.products;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.model.Store;
import com.example.agrifysellers.databinding.ProductListItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.NumberFormat;

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


    public void bind(final DocumentSnapshot snapshot,
                     final ProductListAdapter.OnProductSelectedListner listener, final Activity activity, String TAG)
    {
try {
    Seller seller = snapshot.toObject(Seller.class);


    auth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    DocumentReference docRef = db.collection("store").document(snapshot.getId());
    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot snap, @Nullable FirebaseFirestoreException e) {
            Store store = snap.toObject(Store.class);
            binding.productName.setText(store.getName());
            if (activity != null && store.getProductImageUrl() != null) {
                GlideApp.with(activity)
                        .load(store.getProductImageUrl())
                        .into(binding.productImage);
            }
            binding.productOrder.setText(String.valueOf(seller.getOrderCount()));
            binding.productQuantity.setText(String.valueOf(seller.getStock()));

            binding.productPrice.setText("₹" + NumberFormat.getInstance().format(seller.getPrice()) + "/" + store.getUnit());
            if (seller.isAvalibity()) {
                binding.avalibityTextview.setVisibility(View.GONE);
            } else {
                binding.avalibityTextview.setVisibility(View.VISIBLE);
            }
        }

    });
}
catch (Exception ex)
{
    ex.printStackTrace();
}

itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        View SharedView = binding.productImage;
        listener.onProductSelected(snapshot,SharedView);
    }
});






    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("select an option");
        menu.add(getAdapterPosition(),1,1,"edit");
        menu.add(getAdapterPosition(),2,2,"manage");


    }
}

