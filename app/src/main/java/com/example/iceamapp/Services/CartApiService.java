package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.CreatePaymentRequest;
import com.example.iceamapp.entity.Order;
import com.example.iceamapp.entity.OrderDTO;
import com.example.iceamapp.entity.PaymentResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartApiService {
    @GET("api/CartApi")
    Call<List<Cart>> getAllCarts();

    @GET("api/CartApi/user/{userId}")
    Call<List<Cart>> getCartsByUserId(@Path("userId") int userId);

    @POST("api/CartApi/create-order/{userId}")
    Call<Order> createOrderFromCart(@Path("userId") int userId);

    @GET("api/OrderApi/user/{userId}")
    Call<List<OrderDTO>> getOrdersByUser(@Path("userId") int userId);

    @POST("api/CartApi")
    Call<Void> addToCart(@Body Cart cart);

    // 🔥 API lấy số lượng sản phẩm trong giỏ hàng
    @GET("api/CartApi/count/{userId}")
    Call<Integer> getCartItemCount(@Path("userId") int userId);


    @POST("api/CartApi/create-payment-app/{userId}")
    Call<PaymentResponse> createOrderWithPaymentMethod(@Path("userId") int userId, @Body CreatePaymentRequest requestBody);

}
