package com.example.iceamapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.Services.OrderApiService;
import com.example.iceamapp.entity.Order;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private MaterialButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        btnBack = findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadOrders() {
        OrderApiService apiService = RetrofitClient.getRetrofitInstance().create(OrderApiService.class);
        Call<List<Order>> call = apiService.getAllOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orderList = response.body();
                    orderAdapter = new OrderAdapter(orderList, order -> {
                        // Khi nhấn vào đơn hàng, mở OrderDetailActivity
                        Intent intent = new Intent(ManageOrdersActivity.this, OrderDetailActivity.class);
                        intent.putExtra("ORDER_ID", order.getOrderId());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(ManageOrdersActivity.this, "Không thể tải danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("ManageOrders", "Lỗi: " + t.getMessage());
                Toast.makeText(ManageOrdersActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}