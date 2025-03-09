package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDTO {
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("status")
    private String status;

    @SerializedName("orderDate")
    private String orderDate;

    @SerializedName("orderDetails")
    private List<OrderDetailDTO> orderDetails;

    // Getters
    public int getOrderId() { return orderId; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getOrderDate() { return orderDate; }
    public List<OrderDetailDTO> getOrderDetails() { return orderDetails; }
}