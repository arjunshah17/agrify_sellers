package com.example.agrifysellers.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.viewHolder.StoreHolder;
import com.example.agrifysellers.databinding.ItemStoreProductBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

public class StoreAdapter extends FirestoreAdapter<StoreHolder> {
    Activity activity;

    WriteBatch batch;
    FirebaseFirestore firebaseFirestore;

    public StoreAdapter(Query query, OnStoreSelectedListener listener, Activity activity) {
        super(query);
        mListener = listener;
        this.activity = activity;
    }

    private OnStoreSelectedListener mListener;

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemStoreProductBinding itemStoreProductBinding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.item_store_product, parent, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        batch = firebaseFirestore.batch();
        return new StoreHolder(itemStoreProductBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {

        holder.bind(getSnapshot(position), mListener, activity);
    }

    public interface OnStoreSelectedListener {

        void onStoreSelected(DocumentSnapshot store, View SharedView);

    }


}
