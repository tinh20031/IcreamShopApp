package com.example.iceamapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.iceamapp.adapter.ChatAdapter;
import com.example.iceamapp.ChatbotManager;
import com.example.iceamapp.R;

public class ChatActivity extends AppCompatActivity {
    private ChatbotManager chatbotManager;
    private ChatAdapter chatAdapter;
    private List<String> chatMessages;
    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private ImageButton sendButton;
    private ImageView backButton; // Nút quay lại

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton); // Ánh xạ nút back

        chatbotManager = new ChatbotManager();
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String userMessage = inputMessage.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                inputMessage.setText("");
                chatbotManager.sendMessage(userMessage, response -> runOnUiThread(() -> {
                    chatMessages.add(response);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                }));
            }
        });

        // Xử lý sự kiện quay lại khi nhấn vào nút back
        backButton.setOnClickListener(v -> finish());
    }
}
