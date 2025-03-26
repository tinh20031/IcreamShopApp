package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryId")
    private int categoryId;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    // Constructor mặc định (cần cho Retrofit)
    public Category() {}

    // Constructor đầy đủ
    public Category(int categoryId, String name, String image) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
    }

    // Getters và Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}