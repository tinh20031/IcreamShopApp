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

    // Constructor mặc định (cần cho Retrofit)
    public Order() {}

    // Constructor đầy đủ
    public Order(int orderId, int userId, double totalPrice, String status, String orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getOrderDate() { return orderDate; }

    // Setters
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}