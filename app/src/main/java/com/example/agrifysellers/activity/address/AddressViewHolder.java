package com.example.agrifysellers.activity.address;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.agrifysellers.activity.address.model.Address;
import com.example.agrifysellers.databinding.AddressItemBinding;
import com.google.firebase.firestore.DocumentSnapshot;

public class AddressViewHolder extends RecyclerView.ViewHolder {
    AddressItemBinding binding;
    public AddressViewHolder( AddressItemBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
    }

    public void bind(DocumentSnapshot snapshot, Activity activity) {
        Address address=snapshot.toObject(Address.class);
        binding.setAddress(address);
        String comAdress=address.getHouseNum()+address.getLocation();
        binding.addressLocation.setText(comAdress);
    }
}
