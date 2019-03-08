package com.example.agrifysellers.activity.productActivity;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

import ernestoyaquello.com.verticalstepperform.Step;

public class ProductName extends Step<String> {
    MaterialButton productButton;
    FragmentManager fragmentManager;



    protected ProductName(String title, FragmentManager fragmentManager) {
        super(title);


    }

    @Override
    public String getStepData() {
        return productButton.getText().toString();
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {
        productButton.setText(data);
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        boolean isProdValid = true;
        String errorMessage;
        if (stepData.equals("select productName")) {
            isProdValid = false;
        }
        errorMessage = !isProdValid ? "select correct ProductName" : "";


        return new IsDataValid(isProdValid, errorMessage);

    }

    @Override
    protected View createStepContentLayout() {
        productButton = new MaterialButton(getContext());


        productButton.setText("select productName");

        return productButton;

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
