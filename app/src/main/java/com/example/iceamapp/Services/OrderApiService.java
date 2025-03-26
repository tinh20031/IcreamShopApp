package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrderApiService {
    @GET("api/OrderApi")
    Call<List<Order>> getAllOrders();
}