package com.example.agrifysellers.activity.products.model;

import com.google.firebase.firestore.DocumentReference;

public class Product {

    private String name, des, productImageUrl,unit,imageCount;
    private DocumentReference id;
    private int stock;
    private  int orderNum,pendingOrderNum,totalSelling;
    float price;

    public Product(String name, String des, String productImageUrl, String unit, int stock, int orderNum, int pendingOrderNum, int totalSelling, float price,DocumentReference id) {
        this.name = name;
        this.des = des;
        this.productImageUrl = productImageUrl;
        this.unit = unit;
        this.stock = stock;
        this.orderNum = orderNum;
        this.pendingOrderNum = pendingOrderNum;
        this.totalSelling = totalSelling;
        this.price = price;
        this.id=id;
    }

    public Product() {

    }

    public DocumentReference getId() {
        return id;
    }

    public void setId(DocumentReference id) {
        this.id = id;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getPendingOrderNum() {
        return pendingOrderNum;
    }

    public void setPendingOrderNum(int pendingOrderNum) {
        this.pendingOrderNum = pendingOrderNum;
    }

    public int getTotalSelling() {
        return totalSelling;
    }

    public void setTotalSelling(int totalSelling) {
        this.totalSelling = totalSelling;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
