package com.example.agrifysellers.activity.model;

import android.content.Context;

import com.example.agrifysellers.R;


public class Store {
    private String name, des, productImageUrl, category;
  // private  String lowPrice;
public Store(){}

    public Store(String name, String des, String productImageUrl, String category) {
        this.name = name;
        this.des = des;
        this.productImageUrl = productImageUrl;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getValFromCategory(Context context) {
        String[] arr = context.getResources().getStringArray(R.array.categories_names);
        for (int i = 0; i < arr.length; i++) {
            if (category.equals(arr[i])) {
                return i;
            }
        }
        return 0;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
