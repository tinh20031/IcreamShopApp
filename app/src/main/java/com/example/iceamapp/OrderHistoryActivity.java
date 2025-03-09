package com.example.iceamapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.adapter.OrderAdapter;
import com.example.iceamapp.entity.OrderDTO;
import com.example.iceamapp.entity.OrderDetailDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Ánh xạ nút Back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, Fragment_homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Khởi tạo RecyclerView
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Gắn adapter với danh sách rỗng ban đầu
        orderAdapter = new OrderAdapter(new ArrayList<>());
        recyclerViewOrders.setAdapter(orderAdapter);

        // Tải dữ liệu đơn hàng
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Log.e("OrderHistoryActivity", "User not logged in or userId not found");
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        CartApiService apiService = RetrofitClient.getCartApiService();
        apiService.getOrdersByUser(userId).enqueue(new Callback<List<OrderDTO>>() {
            @Override
            public void onResponse(Call<List<OrderDTO>> call, Response<List<OrderDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderDTO> orders = response.body();
                    Log.d("OrderHistoryActivity", "API Success: " + orders.size() + " orders for userId: " + userId);
                    for (OrderDTO order : orders) {
                        Log.d("OrderHistoryActivity", "Order ID: " + order.getOrderId());
                        for (OrderDetailDTO detail : order.getOrderDetails()) {
                            Log.d("OrderHistoryActivity", "IceCreamName: " + detail.getIceCreamName() + ", ImageUrl: " + detail.getImageUrl());
                        }
                    }
                    orderAdapter = new OrderAdapter(orders);
                    recyclerViewOrders.setAdapter(orderAdapter);
                } else {
                    // ...
                }
            }
            @Override
            public void onFailure(Call<List<OrderDTO>> call, Throwable t) {
                Log.e("OrderHistoryActivity", "API Error: " + t.getMessage(), t);
                Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải đơn hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}