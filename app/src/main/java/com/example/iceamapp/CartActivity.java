package com.example.iceamapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.adapter.CartAdapter;
import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.Order;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView txtTotalPrice;
    private Button btnOrderNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ nút Back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, Fragment_homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Ánh xạ TextView tổng tiền
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        // Ánh xạ nút Đặt Ngay
        btnOrderNow = findViewById(R.id.btnOrderNow);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gắn adapter với danh sách rỗng ban đầu
        cartAdapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(cartAdapter);

        // Tải dữ liệu giỏ hàng theo userId
        loadCartDataByUserId();

        // Xử lý sự kiện nhấn nút Đặt Ngay
        btnOrderNow.setOnClickListener(v -> {
            placeOrder();
        });
    }

    private void loadCartDataByUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Log.e("CartActivity", "User not logged in or userId not found");
            txtTotalPrice.setText("Vui lòng đăng nhập để xem giỏ hàng");
            return;
        }

        CartApiService apiService = RetrofitClient.getCartApiService();
        apiService.getCartsByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> carts = response.body();
                    Log.d("CartActivity", "API Success: " + carts.size() + " items for userId: " + userId);

                    for (Cart cart : carts) {
                        Log.d("CartActivity", "Cart Item - Name: " + cart.getIceCreamName() +
                                ", Image: " + cart.getImage() +
                                ", Price: " + cart.getPrice() +
                                ", Quantity: " + cart.getQuantity());
                    }

                    cartAdapter = new CartAdapter(carts);
                    recyclerView.setAdapter(cartAdapter);

                    double totalPrice = calculateTotalPrice(carts);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.## VND");
                    txtTotalPrice.setText(decimalFormat.format(totalPrice));
                } else {
                    Log.e("CartActivity", "Response Error: " + response.code() + " - " + response.message());
                    txtTotalPrice.setText("Không có sản phẩm trong giỏ hàng");
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("CartActivity", "API Error: " + t.getMessage(), t);
                txtTotalPrice.setText("Lỗi khi tải giỏ hàng");
            }
        });
    }

    private void placeOrder() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        CartApiService apiService = RetrofitClient.getCartApiService();
        apiService.createOrderFromCart(userId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Order order = response.body();
                    Log.d("CartActivity", "Order created: OrderId = " + order.getOrderId());
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công! Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show();

                    // Làm mới giao diện giỏ hàng
                    cartAdapter = new CartAdapter(new ArrayList<>());
                    recyclerView.setAdapter(cartAdapter);
                    txtTotalPrice.setText("0 VND");

                    // Chuyển hướng sang OrderHistoryActivity
                    Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc CartActivity để không quay lại khi nhấn Back
                } else {
                    Log.e("CartActivity", "Order Error: " + response.code() + " - " + response.message());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(CartActivity.this, "Lỗi khi đặt hàng: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("CartActivity", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("CartActivity", "Order API Error: " + t.getMessage(), t);
                Toast.makeText(CartActivity.this, "Lỗi khi đặt hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotalPrice(List<Cart> carts) {
        double total = 0;
        if (carts != null) {
            for (Cart cart : carts) {
                total += cart.getPrice() * cart.getQuantity();
            }
        }
        return total;
    }
}