package com.example.iceamapp.entity;



public class IceCream {
    private int iceCreamId;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String createdAt;
    private int categoryId;
    private Category category;

    // Constructor
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

    // Getters và Setters
    public int getIceCreamId() { return iceCreamId; }
    public void setIceCreamId(int iceCreamId) { this.iceCreamId = iceCreamId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}