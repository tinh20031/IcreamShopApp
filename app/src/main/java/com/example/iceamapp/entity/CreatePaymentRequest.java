package com.example.iceamapp.entity;

public class CreatePaymentRequest {
    private String shippingAddress;

    public CreatePaymentRequest(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}