package com.example.iceamapp.Services;

import com.example.iceamapp.entity.User;
import com.example.iceamapp.entity.LoginRequest;
import com.example.iceamapp.entity.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    // API Đăng Nhập
    @POST("api/Auth/login")
    Call<User> login(@Body LoginRequest loginRequest);

    // API Đăng Ký
    /*@POST("api/Auth/register")
    Call<User> register(@Body RegisterRequest registerRequest);*/
    @POST("api/Auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);

}


