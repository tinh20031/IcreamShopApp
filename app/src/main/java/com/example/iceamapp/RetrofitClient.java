package com.example.iceamapp;

import android.util.Log;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.UnsafeOkHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://10.0.2.2:7283/";
    private static Retrofit retrofit = null;

    // ✅ Cấu hình OkHttpClient để tắt cache
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("Cache-Control", "no-cache") // ⚡ Tắt cache của Retrofit
                            .header("Pragma", "no-cache") // ⚡ Không cho phép lưu cache trên server
                            .build();
                    Response response = chain.proceed(request);
                    String rawResponse = response.body() != null ? response.body().string() : "No body";
                    Log.d("RetrofitClient", "Phản hồi thô từ server: " + rawResponse);
                    // Tái tạo response body vì body chỉ có thể đọc một lần
                    ResponseBody newBody = ResponseBody.create(response.body().contentType(), rawResponse);
                    return response.newBuilder().body(newBody).build();
//                    return chain.proceed(request);
                });

        // Nếu là HTTPS tự ký thì dùng UnsafeOkHttpClient
        if (BASE_URL.startsWith("https")) {
            return UnsafeOkHttpClient.getUnsafeOkHttpClient();
        }
        return builder.build();
    }

    // ✅ Khởi tạo Retrofit chỉ 1 lần duy nhất
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient()) // Dùng OkHttpClient mới
                    .build();
        }
        return retrofit;
    }

    // ✅ Các API service
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
