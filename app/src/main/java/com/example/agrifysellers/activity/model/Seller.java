package com.example.agrifysellers.activity.model;

import com.google.firebase.firestore.DocumentReference;

public class Seller {
    private String name, profilePhotoUrl, phone,email,info,productId,sellerId;
    private DocumentReference StoreProductRef,SellerProductRef,AddressRef;
private boolean avalibity;
    private float price,ratingCount;
    private int stock,minQuantity,maxQuantity,imageCount,orderCount;



    public Seller() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DocumentReference getStoreProductRef() {
        return StoreProductRef;
    }

    public void setStoreProductRef(DocumentReference storeProductRef) {
        StoreProductRef = storeProductRef;
    }

    public DocumentReference getSellerProductRef() {
        return SellerProductRef;
    }

    public void setSellerProductRef(DocumentReference sellerProductRef) {
        SellerProductRef = sellerProductRef;
    }

    public DocumentReference getAddressRef() {
        return AddressRef;
    }

    public void setAddressRef(DocumentReference addressRef) {
        AddressRef = addressRef;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public boolean isAvalibity() {
        return avalibity;
    }

    public void setAvalibity(boolean avalibity) {
        this.avalibity = avalibity;
    }

    public Seller(String name, String profilePhotoUrl, String phone, String email, DocumentReference storeProductRef, DocumentReference sellerProductRef, DocumentReference addressRef, float price, int stock, int minQuantity, int maxQuantity, int imageCount, String info, String productId, float ratingCount, int orderCount, String sellerId, boolean avalibity) {
        this.name = name;
        this.profilePhotoUrl = profilePhotoUrl;
        this.phone = phone;
        this.email = email;
        StoreProductRef = storeProductRef;
        SellerProductRef = sellerProductRef;
        AddressRef = addressRef;
this.info=info;
        this.price = price;
        this.stock = stock;
        this.minQuantity = minQuantity;
        this.avalibity=avalibity;
        this.maxQuantity = maxQuantity;
        this.imageCount = imageCount;
        this.productId=productId;
        this.ratingCount=ratingCount;
        this.orderCount=orderCount;
        this.sellerId=sellerId;


    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
