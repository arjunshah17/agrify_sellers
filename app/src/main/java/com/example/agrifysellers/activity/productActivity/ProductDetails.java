package com.example.agrifysellers.activity.productActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ProductDetailsStepperBinding;
import com.google.android.material.textfield.TextInputEditText;

import ernestoyaquello.com.verticalstepperform.Step;

public class ProductDetails extends Step<Boolean> implements TextWatcher {
ProductDetailsStepperBinding binding;

    protected ProductDetails(String title) {
        super(title);
    }

    @Override
    public Boolean getStepData() {
        boolean flag=true;
        float price=0;
        int stock=0,minQuantity=0,maxQuantiy=0;
        try
        {
            price=Float.valueOf(binding.priceEditText.getText().toString());
            stock=Integer.valueOf(binding.stockEditText.getText().toString());
            minQuantity=Integer.valueOf(binding.minQuantityEditText.getText().toString());
            maxQuantiy=Integer.valueOf(binding.maxQuantityEditText.getText().toString());
        }
        catch (NumberFormatException e)
        {
            Log.e("e",e.toString());
        }
        if(TextUtils.isEmpty(binding.priceEditText.getText().toString().trim()) || price<=0)
        {
            binding.priceEditText.setError("price cannot empty or zero");
            flag=false;
        }
        if( TextUtils.isEmpty(binding.stockEditText.getText().toString().trim()) || stock<=0)
        {
            binding.stockEditText.setError("stock cannot empty or zero");
            flag=false;
        }
        if(minQuantity>stock)
        {
            binding.minQuantityEditText.setError("minimum quantity must be less then stock");
            flag=false;
        }
        if(maxQuantiy>=stock || maxQuantiy<minQuantity)
        {
            binding.maxQuantityEditText.setError("maximum quantity must be less then stock or greater then then minimum quantity");
            flag=false;
        }
        if(flag==true)
        {
            binding.priceEditText.setError(null);
            binding.stockEditText.setError(null);
            binding.minQuantityEditText.setError(null);
            binding.maxQuantityEditText.setError(null);

        }


        return flag;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(Boolean data) {

    }

    @Override
    protected IsDataValid isStepDataValid(Boolean stepData) {
        String message="enter correct data";
        if(stepData)
        {
            message="";
        }
        return new IsDataValid(stepData,message);

    }

    @Override
    protected View createStepContentLayout() {
        binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.product_details_stepper,null,false);
binding.priceEditText.addTextChangedListener(this);
binding.stockEditText.addTextChangedListener(this);
binding.minQuantityEditText.addTextChangedListener(this);
binding.maxQuantityEditText.addTextChangedListener(this);

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
markAsCompletedOrUncompleted(true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}