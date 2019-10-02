package com.example.agrifysellers.activity.order.viewHolder;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;


import com.example.agrifysellers.activity.order.model.Rating;
import com.example.agrifysellers.databinding.ItemRatingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class RatingHolder extends RecyclerView.ViewHolder {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy");
    ItemRatingBinding binding;
    FirebaseFirestore firebaseFirestore;

    public RatingHolder(ItemRatingBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void bind(DocumentSnapshot snapshot, Activity activity) {
        try {
            Rating rating = snapshot.toObject(Rating.class);
            binding.ratingBar.setRating(rating.getRating());
            binding.reviewText.setText(rating.getReviewText());
            binding.timeStamp.setText(FORMAT.format(rating.getTimestamp().toDate()));
            firebaseFirestore.collection("Users").document(rating.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    binding.name.setText(snapshot.getString("name"));
                    Picasso.get()
                            .load(snapshot.getString("profilePhotoUrl"))
                            .into(binding.userImage);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
