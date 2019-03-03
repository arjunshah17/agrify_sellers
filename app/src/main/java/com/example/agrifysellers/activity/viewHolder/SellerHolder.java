package com.example.agrifysellers.activity.viewHolder;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.GlideApp;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.databinding.ItemSellerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SellerHolder extends RecyclerView.ViewHolder {
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    Seller seller;
    ItemSellerBinding binding;
    private String phoneNumber;
    public SellerHolder(@NonNull ItemSellerBinding item) {
        super(item.getRoot());
        binding = item;
        seller = new Seller();
    }
    public void bind(final DocumentSnapshot snapshot, final Activity activity) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference reference = snapshot.getDocumentReference("userId");
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                seller = documentSnapshot.toObject(Seller.class);
                binding.setSeller(seller);
                phoneNumber = seller.getPhone();
                // Load image
                if (activity != null) {
                    GlideApp.with(activity)
                            .load(seller.getProfilePhotoUrl())
                            .into(binding.profilePhoto);
                }
            }
        });

        Resources resources = itemView.getResources();


        binding.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                v.getContext().startActivity(intent);
            }
        });
    }


}
