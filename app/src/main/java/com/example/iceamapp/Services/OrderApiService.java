package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Order;
import com.example.iceamapp.entity.OrderDTO;
import com.example.iceamapp.entity.OrderDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderApiService {
    @GET("api/OrderApi")
    Call<List<Order>> getAllOrders();
    @GET("api/OrderApi/{orderId}")
    Call<OrderDTO> getOrderDetails(@Path("orderId") int orderId);
}