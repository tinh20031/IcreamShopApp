package com.example.iceamapp;

import android.content.Intent; // Thêm import này
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.adapter.CartAdapter;
import com.example.iceamapp.entity.Cart;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ nút Back và xử lý sự kiện click để chuyển về HomeActivity
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Tạo Intent để chuyển về HomeActivity
            Intent intent = new Intent(CartActivity.this, Fragment_homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Tùy chọn để tránh tạo mới nếu HomeActivity đã tồn tại
            startActivity(intent);
            finish(); // Đóng CartActivity sau khi chuyển
        });

        // Ánh xạ TextView tổng tiền
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gắn adapter với danh sách rỗng ban đầu
        cartAdapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(cartAdapter);

        // Tải dữ liệu giỏ hàng
        loadCartData();
    }

    private void loadCartData() {
        CartApiService apiService = RetrofitClient.getCartApiService();
        apiService.getAllCarts().enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> carts = response.body();
                    Log.d("CartActivity", "API Success: " + carts.size() + " items");

                    for (Cart cart : carts) {
                        Log.d("CartActivity", "Cart Item - Name: " + cart.getIceCreamName() +
                                ", Image: " + cart.getImage() +
                                ", Price: " + cart.getPrice() +
                                ", Quantity: " + cart.getQuantity());
                    }

                    // Cập nhật adapter với danh sách carts
                    cartAdapter = new CartAdapter(carts);
                    recyclerView.setAdapter(cartAdapter);

                    // Tính và hiển thị tổng tiền
                    double totalPrice = calculateTotalPrice(carts);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.## VND");
                    txtTotalPrice.setText(decimalFormat.format(totalPrice));
                } else {
                    Log.e("CartActivity", "Response Error: " + response.code() + " - " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("CartActivity", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("CartActivity", "Error parsing error body", e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("CartActivity", "API Error: " + t.getMessage(), t);
            }
        });
    }

    // Phương thức tính tổng tiền
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