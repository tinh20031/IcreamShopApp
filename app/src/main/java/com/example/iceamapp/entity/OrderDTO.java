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

    @SerializedName("shippingAddress")
    private String shippingAddress;

    @SerializedName("orderDetails")
    private List<OrderDetailDTO> orderDetails;

    // Getters
    public int getOrderId() { return orderId; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getOrderDate() { return orderDate; }
    public String getShippingAddress() { return shippingAddress; }
    public List<OrderDetailDTO> getOrderDetails() { return orderDetails; }

    // Setters (nếu cần)
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setOrderDetails(List<OrderDetailDTO> orderDetails) { this.orderDetails = orderDetails; }
}