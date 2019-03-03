package com.example.agrifysellers.activity.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Seller {
    String name, profilePhotoUrl, phone;
    DocumentReference userId;
    Timestamp timestamp;
    float price;


    public Seller() {
    }

    public Seller(String name, String profilePhotoUrl, DocumentReference userId, Timestamp timestamp, float price, String phone) {
        this.name = name;
        this.profilePhotoUrl = profilePhotoUrl;
        this.userId = userId;
        this.timestamp = timestamp;
        this.price = price;
        this.phone = phone;
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
