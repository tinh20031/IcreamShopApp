package com.example.iceamapp;

import com.example.iceamapp.Services.IceCreamApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://10.0.2.2:7283/";
    private static Retrofit retrofit = null;

    public static IceCreamApiService getIceCreamApiService() {
        if (retrofit == null) {
            OkHttpClient client;

            // Kiểm tra API có HTTPS hay không
            if (BASE_URL.startsWith("https")) {
                client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            } else {
                client = new OkHttpClient.Builder().build();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(IceCreamApiService.class);
    }
}
