package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userId")  // Ánh xạ với trường JSON của API trả về
    private int userId;

    @SerializedName("fullName")  // Ánh xạ với trường JSON của API trả về
    private String fullName;

    @SerializedName("email")  // Ánh xạ với trường JSON của API trả về
    private String email;

    @SerializedName("passwordHash")  // Ánh xạ với trường JSON của API trả về
    private String passwordHash;

    @SerializedName("phoneNumber")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("role")
    private String role;



    // Constructor
    public User(int userId, String fullName, String email, String passwordHash, String phone, String address, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.address = address;
        this.role = role;

    }

    // Getter và Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }


}
