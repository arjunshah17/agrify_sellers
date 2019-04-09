package com.example.agrifysellers.activity.products;

import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ProductAddressStepperBinding;

import ernestoyaquello.com.verticalstepperform.Step;

public class ProductAddress extends Step<String> {
    ProductAddressStepperBinding binding;
    protected ProductAddress(String title) {
        super(title);
    }

    @Override
    public String getStepData() {

        return binding.productAddress.getText().toString();
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {

    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {

        boolean isProdValid = true;
        String errorMessage;
        if (stepData.equals("select address")) {
            isProdValid = false;
        }
        errorMessage = !isProdValid ? "select correct ProductName" : "";


        return new IsDataValid(isProdValid, errorMessage);
    }

    @Override
    protected View createStepContentLayout() {
        binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.product_address_stepper,null,false);

        return binding.getRoot();
    }

    @Override
    protected void onStepOpened(boolean animated) {

    }

    @Override
    protected void onStepClosed(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {

    }
}
