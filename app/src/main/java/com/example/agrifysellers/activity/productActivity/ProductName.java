package com.example.agrifysellers.activity.productActivity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.example.agrifysellers.R;
import com.example.agrifysellers.databinding.ProductNameStepperBinding;
import com.google.android.material.button.MaterialButton;

import ernestoyaquello.com.verticalstepperform.Step;

public class ProductName extends Step<String> {
   ProductNameStepperBinding binding;
    FragmentManager fragmentManager;



    protected ProductName(String title, FragmentManager fragmentManager) {
        super(title);


    }

    @Override
    public String getStepData() {
        return binding.productName.getText().toString();
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {
        binding.productName.setText(data);
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        boolean isProdValid = true;
        String errorMessage;
        if (stepData.equals("select product")) {
            isProdValid = false;
        }
        errorMessage = !isProdValid ? "select correct ProductName" : "";


        return new IsDataValid(isProdValid, errorMessage);

    }

    @Override
    protected View createStepContentLayout() {


binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.product_name_stepper,null,false);


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
