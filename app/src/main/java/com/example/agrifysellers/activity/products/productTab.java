package com.example.agrifysellers.activity.products;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.TabAdapter;
import com.example.agrifysellers.databinding.FragmentProductTabBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class productTab extends Fragment {


    public productTab() {
        // Required empty public constructor
    }
    private TabAdapter adapter;
FragmentProductTabBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_product_tab, container, false);

        adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new productListFragment(), "products");
        adapter.addFragment(new orderFragment(), "orders");
        binding.productViewPager.setAdapter(adapter);
        binding.productTabLayout.setupWithViewPager(binding.productViewPager);

        return binding.getRoot();
    }

}
