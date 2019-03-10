package com.example.agrifysellers.activity.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.SellerAdapter;
import com.example.agrifysellers.activity.model.Seller;
import com.example.agrifysellers.activity.sellerProduct.SellerProductActivity;
import com.example.agrifysellers.databinding.SellerlistfragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;

public class SellerListFragment extends BottomSheetDialogFragment implements SellerAdapter.OnSellerSelectedListener {
    String product_id;
  SellerlistfragmentBinding binding;
  FirebaseFirestore firebaseFirestore;
  Query mQuery;
  SellerAdapter mAdapter;

    public SellerListFragment(String product_id)
    {
        this.product_id=product_id;
    }


    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore=FirebaseFirestore.getInstance();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.sellerlistfragment, container,
                false);
getSellerList();
        return binding.getRoot();

    }
    void getSellerList() {

        mQuery = firebaseFirestore.collection("store").document(product_id).collection("sellerList");

        mAdapter = new SellerAdapter(mQuery, getActivity(),this::onSellerSelected);
        binding.sellerListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.sellerListRecycleView.setAdapter(mAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onSellerSelected(DocumentSnapshot seller, View SharedView) {
        Seller selectedSeller=seller.toObject(Seller.class);

      //  product_ref=firebaseFirestore.collection("store").document(product_id).collection("sellerList").document( seller.getId());
        Intent intent=new Intent(getContext(), SellerProductActivity.class);

          intent.putExtra("seller_id",seller.getId());
          intent.putExtra("product_id",product_id);
          startActivity(intent);
    }
}
