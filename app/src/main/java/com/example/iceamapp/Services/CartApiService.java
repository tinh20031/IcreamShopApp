package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.Order;
import com.example.iceamapp.entity.OrderDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface  CartApiService {

    @GET("api/CartApi")
    Call<List<Cart>> getAllCarts();
    @GET("api/CartApi/user/{userId}")
    Call<List<Cart>> getCartsByUserId(@Path("userId") int userId);

    @POST("api/CartApi/create-order/{userId}")
    Call<Order> createOrderFromCart(@Path("userId") int userId);

    @GET("api/OrderApi/user/{userId}")
    Call<List<OrderDTO>> getOrdersByUser(@Path("userId") int userId);

    @POST("api/CartAPI")
    Call<Void> addToCart(@Body Cart cart);
}
