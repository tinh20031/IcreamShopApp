package com.example.iceamapp;

import com.example.iceamapp.Services.CartApiService; // Thêm import cho CartApiService
import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://10.0.2.2:7283/";
    private static Retrofit retrofit = null;

    // Phương thức khởi tạo Retrofit chung
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client;

            // Kiểm tra API có HTTPS hay không
            if (BASE_URL.startsWith("https")) {
                client = UnsafeOkHttpClient.getUnsafeOkHttpClient(); // Dùng UnsafeOkHttpClient cho HTTPS tự ký
            } else {
                client = new OkHttpClient.Builder().build();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    // Service cho IceCreamApiService
    public static IceCreamApiService getIceCreamApiService() {
        return getRetrofitInstance().create(IceCreamApiService.class);
    }

    // Service cho CartApiService
    public static CartApiService getCartApiService() {
        return getRetrofitInstance().create(CartApiService.class);
    }

    public static CategoryApiService getCategoryApiService() {
        return getRetrofitInstance().create(CategoryApiService.class);
    }
}