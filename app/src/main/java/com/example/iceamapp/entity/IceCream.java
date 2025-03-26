package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class IceCream {
    @SerializedName("iceCreamId")
    private int iceCreamId;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("stock")
    private int stock;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("categoryId")
    private int categoryId;

    private Category category;

    // Constructor mặc định cần thiết nếu sử dụng Retrofit
    public IceCream() {}

    // Constructor đầy đủ
    public IceCream(int iceCreamId, String name, String description, double price, int stock,
                    String imageUrl, String createdAt, int categoryId, Category category) {
        this.iceCreamId = iceCreamId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.category = category;
    }

    // Getters
    public int getIceCreamId() {
        return iceCreamId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Category getCategory() {
        return category; // Trả về null nếu category == null
    }

    // Setters (Thêm vào để khắc phục lỗi)
    public void setIceCreamId(int iceCreamId) {
        this.iceCreamId = iceCreamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}