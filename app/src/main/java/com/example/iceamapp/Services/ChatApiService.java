package com.example.iceamapp.Services;

import com.example.iceamapp.entity.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatApiService {
    @POST("api/chat/send")
    Call<Void> sendMessage(@Body ChatMessage message);

    @GET("api/chat/messages")
    Call<List<ChatMessage>> getMessages();

}
