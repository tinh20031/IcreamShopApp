package com.example.iceamapp;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://10.0.2.2:7283/"; // API URL
    private static Retrofit retrofit = null;

    // ✅ Đổi từ private → public để có thể gọi từ các class khác
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client;

            // Nếu API là HTTPS, kiểm tra chứng chỉ
            if (BASE_URL.startsWith("https")) {
                client = UnsafeOkHttpClient.getUnsafeOkHttpClient(); // ⚠️ Chỉ dùng cho DEV
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

    // ✅ Thay vì gọi `getRetrofitInstance()` nhiều lần, dùng biến static để tránh tạo lại Retrofit
    private static final IceCreamApiService iceCreamApiService = getRetrofitInstance().create(IceCreamApiService.class);
    private static final CartApiService cartApiService = getRetrofitInstance().create(CartApiService.class);
    private static final CategoryApiService categoryApiService = getRetrofitInstance().create(CategoryApiService.class);

    public static IceCreamApiService getIceCreamApiService() {
        return iceCreamApiService;
    }

    public static CartApiService getCartApiService() {
        return cartApiService;
    }

    public static CategoryApiService getCategoryApiService() {
        return categoryApiService;
    }
}
