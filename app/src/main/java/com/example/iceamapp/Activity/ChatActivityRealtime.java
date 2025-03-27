package com.example.iceamapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.adapter.ChatMessageAdapter;
import com.example.iceamapp.entity.ChatMessage;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.TransportEnum;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ChatActivityRealtime extends AppCompatActivity {
    private EditText editTextMessage;
    private Button btnSend;
    private RecyclerView recyclerViewChat;
    private ChatMessageAdapter chatMessageAdapter;
    private HubConnection hubConnection;
    private static final String TAG = "SignalR";
    private ImageView backhotro; // Nút quay lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backhotro = findViewById(R.id.backhotro); // Ánh xạ nút back

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);

        // Cấu hình RecyclerView
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        chatMessageAdapter = new ChatMessageAdapter("User123"); // Định danh User
        recyclerViewChat.setAdapter(chatMessageAdapter);

        // Kết nối SignalR
        connectToSignalR();

        btnSend.setOnClickListener(v -> sendMessage());
        // Xử lý sự kiện quay lại khi nhấn vào nút back
        backhotro.setOnClickListener(v -> finish());
    }

    private void connectToSignalR() {
        // Tạo OkHttpClient bỏ qua kiểm tra SSL
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        hubConnection = HubConnectionBuilder.create("https://10.0.2.2:7283/chatHub")
                .withTransport(TransportEnum.LONG_POLLING) // Sử dụng Long Polling cho HTTPS
                .build();

        // Ghi đè HttpClient nội bộ của SignalR (nếu cần thiết)
        try {
            java.lang.reflect.Field clientField = hubConnection.getClass().getDeclaredField("httpClient");
            clientField.setAccessible(true);
            Object defaultHttpClient = clientField.get(hubConnection);
            java.lang.reflect.Field okHttpClientField = defaultHttpClient.getClass().getDeclaredField("client");
            okHttpClientField.setAccessible(true);
            okHttpClientField.set(defaultHttpClient, okHttpClient);
        } catch (Exception e) {
            Log.e(TAG, "Không thể ghi đè OkHttpClient: " + e.getMessage());
        }

        // Xử lý sự kiện nhận tin nhắn
        hubConnection.on("ReceiveMessage", (user, message) -> {
            runOnUiThread(() -> {
                Log.d(TAG, "Nhận tin nhắn: " + user + ": " + message);
                ChatMessage chatMessage = new ChatMessage(user, message);
                chatMessageAdapter.addMessage(chatMessage);
                recyclerViewChat.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
            });
        }, String.class, String.class);

        // Bắt đầu kết nối SignalR và xử lý lỗi
        hubConnection.start()
                .subscribe(
                        () -> Log.d(TAG, "Kết nối SignalR thành công"),
                        throwable -> {
                            Log.e(TAG, "Lỗi kết nối SignalR: " + throwable.getMessage());
                            runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối đến server: " + throwable.getMessage(), Toast.LENGTH_LONG).show());
                        }
                );
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Nhập tin nhắn trước khi gửi!", Toast.LENGTH_SHORT).show();
            return;
        }

        hubConnection.send("SendMessage", "User123", messageText);
        editTextMessage.setText(""); // Xóa ô nhập sau khi gửi
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hubConnection != null) {
            hubConnection.stop();
        }
    }

    // Tạo OkHttpClient bỏ qua kiểm tra SSL (dùng trong phát triển)
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi cấu hình OkHttpClient: " + e.getMessage());
            return new OkHttpClient.Builder().build();
        }
    }
}