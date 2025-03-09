package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class OrderDetailDTO {
    @SerializedName("iceCreamName")
    private String iceCreamName;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private double price;

    // Getters
    public String getIceCreamName() { return iceCreamName; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}