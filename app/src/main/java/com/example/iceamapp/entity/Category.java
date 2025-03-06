package com.example.iceamapp.entity;

public class Category {
    private int categoryId;
    private String name;
    private String image;

    // Constructor
    public Category(int categoryId, String name, String image) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
    }

    // Getters và Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }

    public String getImage() {
        return image;
    }

    public void setName(String name) { this.name = name; }
}