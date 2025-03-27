package com.example.iceamapp;

import android.util.Log;
import com.example.iceamapp.entity.GeminiRequest;
import com.example.iceamapp.entity.GeminiResponse;
import com.example.iceamapp.Services.GeminiApi;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotManager {
    private static final String TAG = "ChatbotManager";
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";
    private static final String API_KEY = "AIzaSyB_-lTBXF95L6zHKv32Tkyko3_pLE3-6kQ";
    private final String MODEL = "gemini-2.0-flash";

    private GeminiApi geminiApi;

    public ChatbotManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApi = retrofit.create(GeminiApi.class);
    }

    public void sendMessage(String message, ChatbotCallback callback) {
        Log.d(TAG, "Gửi tin nhắn: " + message);
        callback.onMessageUpdate("Bạn: " + message);

        GeminiRequest request = new GeminiRequest(message);
        Call<GeminiResponse> call = geminiApi.getGeminiCompletion(MODEL, API_KEY, request);
        call.enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GeminiResponse.Candidate> candidates = response.body().getCandidates();
                    if (candidates != null && !candidates.isEmpty()) {
                        String reply = candidates.get(0).getContent().getParts().get(0).getText();
                        Log.d(TAG, "Phản hồi từ AI: " + reply);
                        callback.onMessageUpdate("Bot: " + reply);
                    } else {
                        Log.e(TAG, "Bot không có phản hồi");
                        callback.onMessageUpdate("Bot: AI không có phản hồi.");
                    }
                } else {
                    int statusCode = response.code();
                    String errorMessage = getErrorMessage(response.errorBody());

                    Log.e(TAG, "Lỗi từ Gemini - Mã lỗi: " + statusCode + ", Nội dung: " + errorMessage);

                    if (statusCode == 503) {
                        callback.onMessageUpdate("Bot: Dịch vụ AI đang bảo trì, vui lòng thử lại sau.");
                    } else {
                        callback.onMessageUpdate("Bot: Lỗi phản hồi từ AI: " + errorMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối đến Gemini: " + t.getMessage(), t);
                callback.onMessageUpdate("Bot: Lỗi kết nối đến AI. Vui lòng thử lại.");
            }
        });
    }

    private String getErrorMessage(ResponseBody errorBody) {
        try {
            return errorBody != null ? errorBody.string() : "Không rõ lỗi.";
        } catch (IOException e) {
            return "Không thể đọc lỗi từ server.";
        }
    }

    public interface ChatbotCallback {
        void onMessageUpdate(String message);
    }
}
