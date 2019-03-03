package com.example.agrifysellers.activity.productActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import ernestoyaquello.com.verticalstepperform.Step;

public class Price extends Step<Float> {
    TextInputEditText PriceTextview;

    protected Price(String title) {
        super(title);
    }

    @Override
    public Float getStepData() {
        try {


            return Float.valueOf(PriceTextview.getText().toString());
        } catch (Exception e) {
            return 0.0f;
        }
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(Float data) {
        PriceTextview.setText(data.toString());
    }

    @Override
    protected IsDataValid isStepDataValid(Float stepData) {
        boolean isPriceValid = true;
        if (stepData == 0.0f) {
            isPriceValid = false;
        }
        String errorMessage = !isPriceValid ? "price cannot be empty" : "";

        return new IsDataValid(isPriceValid, errorMessage);

    }

    @Override
    protected View createStepContentLayout() {
        PriceTextview = new TextInputEditText(getContext());
        PriceTextview.setHint("set price of product");
        PriceTextview.setInputType(InputType.TYPE_CLASS_NUMBER);
        PriceTextview.addTextChangedListener(new TextWatcher() {
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
        });
        return PriceTextview;
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
