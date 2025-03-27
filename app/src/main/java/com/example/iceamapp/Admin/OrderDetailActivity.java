package com.example.iceamapp.Admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.Services.OrderApiService;
import com.example.iceamapp.adapter.OrderDetailAdapter;
import com.example.iceamapp.entity.OrderDTO;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderDetailAdapter orderDetailAdapter;
    private MaterialButton btnBack;
    private TextView tvOrderId, tvTotalPrice, tvStatus, tvOrderDate, tvShippingAddress;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        recyclerView = findViewById(R.id.recyclerViewOrderDetails);
        btnBack = findViewById(R.id.btnBack);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadOrderDetails(orderId);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadOrderDetails(int orderId) {
        OrderApiService apiService = RetrofitClient.getRetrofitInstance().create(OrderApiService.class);
        Call<OrderDTO> call = apiService.getOrderDetails(orderId);

        call.enqueue(new Callback<OrderDTO>() {
            @Override
            public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderDTO order = response.body();
                    tvOrderId.setText("Mã đơn hàng: " + order.getOrderId());
                    tvTotalPrice.setText(String.format("Tổng giá: %.2f VND", order.getTotalPrice()));
                    tvStatus.setText("Trạng thái: " + order.getStatus());
                    tvOrderDate.setText("Ngày đặt: " + order.getOrderDate());
                    tvShippingAddress.setText("Địa chỉ giao: " + order.getShippingAddress());

                    orderDetailAdapter = new OrderDetailAdapter(order.getOrderDetails());
                    recyclerView.setAdapter(orderDetailAdapter);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDTO> call, Throwable t) {
                Log.e("OrderDetail", "Lỗi: " + t.getMessage());
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}