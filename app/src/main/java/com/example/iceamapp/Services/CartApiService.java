package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface  CartApiService {

    @GET("api/CartApi")
    Call<List<Cart>> getAllCarts();




}
