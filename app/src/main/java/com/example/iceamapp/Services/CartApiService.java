package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Cart;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CartApiService {

    // Lấy danh sách giỏ hàng
    @GET("api/CartApi")
    Call<List<Cart>> getAllCarts();

    // Thêm sản phẩm vào giỏ hàng
    @POST("api/CartAPI")
    Call<Void> addToCart(@Body Cart cart);
}
