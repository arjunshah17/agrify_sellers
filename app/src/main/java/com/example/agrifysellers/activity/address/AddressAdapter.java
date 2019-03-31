package com.example.agrifysellers.activity.address;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.FirestoreAdapter;
import com.example.agrifysellers.databinding.AddressItemBinding;
import com.google.firebase.firestore.Query;

public class AddressAdapter extends FirestoreAdapter<AddressViewHolder> {
    Activity activity;
    public AddressAdapter(Query query,Activity activity) {
        super(query);
        this.activity=activity;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressItemBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.address_item, parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
     holder.bind(getSnapshot(position),activity);
    }
}
