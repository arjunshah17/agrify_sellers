package com.example.agrify.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrify.R;
import com.example.agrify.activity.viewHolder.StoreHolder;
import com.example.agrify.databinding.ItemStoreProductBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class StoreAdapter extends FirestoreAdapter<StoreHolder> {
    Activity activity;

    public StoreAdapter(Query query, OnStoreSelectedListener listener, Activity activity) {
        super(query);
        mListener = listener;
        this.activity = activity;
    }

    private OnStoreSelectedListener mListener;

    @Override
    public void onBindViewHolder(@NonNull StoreHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener, activity);
    }


    @NonNull
    @Override
    public StoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemStoreProductBinding itemStoreProductBinding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()), R.layout.item_store_product, parent, false);


        return new StoreHolder(itemStoreProductBinding);
    }

    public interface OnStoreSelectedListener {

        void onStoreSelected(DocumentSnapshot store);

    }
}
