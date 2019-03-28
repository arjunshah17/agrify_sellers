package com.example.agrifysellers.activity.products;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.FragmentProductListBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class productListFragment extends Fragment {


    public productListFragment() {
        // Required empty public constructor
    }
FragmentProductListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
        binding.foldingCell.setOnClickListener(v -> {
            binding.foldingCell.toggle(false);

        });

        return binding.getRoot();
    }

}
