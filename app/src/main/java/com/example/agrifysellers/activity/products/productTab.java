package com.example.agrifysellers.activity.products;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.agrifysellers.R;
import com.example.agrifysellers.activity.adapter.TabAdapter;
import com.example.agrifysellers.activity.order.OrderListFragment;
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
OrderListFragment order;
productListFragment product;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order=new OrderListFragment();
        product=new productListFragment();
        adapter = new TabAdapter(getChildFragmentManager());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_product_tab, container, false);


        adapter.addFragment(product, "products");
        adapter.addFragment(order, "orders");
        binding.productViewPager.setAdapter(adapter);
        binding.productTabLayout.setupWithViewPager(binding.productViewPager);


        return binding.getRoot();
    }

}
