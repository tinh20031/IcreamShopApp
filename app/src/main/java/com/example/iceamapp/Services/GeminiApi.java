package com.example.iceamapp.Services;

import com.example.iceamapp.entity.GeminiRequest;
import com.example.iceamapp.entity.GeminiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeminiApi {
    @POST("models/{model}:generateContent")
    Call<GeminiResponse> getGeminiCompletion(
            @Path("model") String model,
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}
