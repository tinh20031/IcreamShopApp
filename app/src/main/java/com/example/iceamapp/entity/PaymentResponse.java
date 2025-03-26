package com.example.iceamapp.entity;

import com.google.gson.annotations.SerializedName;

public class PaymentResponse {
    @SerializedName("orderUrl") // Sửa từ "OrderUrl" thành "orderUrl"
    private String orderUrl;

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }
}