package com.example.iceamapp.Services;

import com.example.iceamapp.entity.User;
import com.example.iceamapp.entity.LoginRequest;
import com.example.iceamapp.entity.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthApiService {

    // API Đăng Nhập
    @POST("api/Auth/login")
    Call<User> login(@Body LoginRequest loginRequest);

    // API Đăng Ký
    @POST("api/Auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);

    @GET("api/UserApi/{userId}")
    Call<User> getUserDetails(@Path("userId") int userId);

    @PUT("api/UserApi/{userId}")
    Call<User> updateUserDetails(@Path("userId") int userId, @Body User updatedUser);

}


