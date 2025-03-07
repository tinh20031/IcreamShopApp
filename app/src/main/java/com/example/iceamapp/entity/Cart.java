package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("cartId")
    private int cartId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("iceCreamId")
    private int iceCreamId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("iceCreamName")
    private String iceCreamName;

    @SerializedName("image")
    private String image;

    @SerializedName("price")
    private float price;

    public Cart(int userId, int iceCreamId, int quantity) {
        this.userId = userId;
        this.iceCreamId = iceCreamId;
        this.quantity = quantity;
    }

    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getIceCreamId() { return iceCreamId; }
    public int getQuantity() { return quantity; }
    public String getCreatedAt() { return createdAt; }
    public String getIceCreamName() { return iceCreamName; }
    public String getImage() { return image; }
    public float getPrice() { return price; }
}