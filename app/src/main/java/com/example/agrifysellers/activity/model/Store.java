package com.example.agrifysellers.activity.model;

import java.util.HashMap;
import java.util.Map;

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

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> toStoreMap() {
        Map<String, String> product = new HashMap<>();
        product.put("name", name);
        product.put("productImageUrl", productImageUrl);
        product.put("category", category);
        return product;

    }
}
