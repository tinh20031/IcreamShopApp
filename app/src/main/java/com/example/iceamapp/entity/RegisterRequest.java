package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phoneNumber")
    private String phoneNumber ;

    @SerializedName("address")
    private String address;

    @SerializedName("role")
    private String role;

    public RegisterRequest(String fullName, String email, String password, String phoneNumber , String address, String role){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber  = phoneNumber ;
        this.address=address;
        this.role = role;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public  String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String phoneNumber (){
        return phoneNumber ;
    }

    public void phoneNumber (String phone){
        this.phoneNumber  = phoneNumber ;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role= role;
    }
}
