package com.example.agrifysellers.activity.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Seller {
    private String name, profilePhotoUrl, phone;
    private DocumentReference userId;
    private Timestamp timestamp;
    private float price;
    private int stock,minQuantity,maxQuantity;



    public Seller() {
    }

    public Seller(String name, String profilePhotoUrl, DocumentReference userId, Timestamp timestamp, float price, String phone,int stock,int minQuantity,int maxQuantity) {
        this.name = name;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userId = userId;
        this.timestamp = timestamp;
        this.price = price;
        this.phone = phone;
        this.stock=stock;
        this.minQuantity=minQuantity;
        this.maxQuantity=maxQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public DocumentReference getUserId() {
        return userId;
    }

    public void setUserId(DocumentReference userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
