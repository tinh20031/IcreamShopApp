package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")  // Ánh xạ đúng với tên trường mà API yêu cầu
    private String email;

    @SerializedName("password")  // Ánh xạ đúng với tên trường mà API yêu cầu
    private String password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    // Getter và Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
