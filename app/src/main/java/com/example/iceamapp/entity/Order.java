package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("status")
    private String status;

    @SerializedName("orderDate")
    private String orderDate;

    // Getters
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getOrderDate() { return orderDate; }
}