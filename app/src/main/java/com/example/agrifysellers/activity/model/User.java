package com.example.agrifysellers.activity.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name, email, phone, profilePhotoUrl;
boolean isAdmin;
    public User() {
    }

    public User(String name, String email, String phone, String profilePhotoUrl,boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isAdmin=isAdmin;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Map<String, String> toUserMap()
   {
       Map<String,String> user=new HashMap<>();
       user.put("name",name);
       user.put("phone",phone);
       return user;

   }
}
