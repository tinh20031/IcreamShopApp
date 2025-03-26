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
    private List<Cart> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, Fragment_homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(new ArrayList<>());
        recyclerView.setAdapter(cartAdapter);

        loadCartDataByUserId();

        // Khi nhấn nút "Order Now", chuyển sang SelectAddressActivity
        btnOrderNow.setOnClickListener(v -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                Intent intent = new Intent(CartActivity.this, SelectAddressActivity.class);
                intent.putExtra("cartItems", new ArrayList<>(cartItems));
                startActivity(intent);
            } else {
                Toast.makeText(CartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartDataByUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        Log.d("CartActivity", "User ID: " + userId);

        if (userId == -1) {
            Log.e("CartActivity", "User not logged in or userId not found");
            txtTotalPrice.setText("Vui lòng đăng nhập để xem giỏ hàng");
            return;
        }

        CartApiService apiService = RetrofitClient.getCartApiService();
        apiService.getCartsByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                Log.d("CartActivity", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    cartItems = response.body();
                    Log.d("CartActivity", "Cart Items Retrieved: " + cartItems.size());

                    for (Cart cart : cartItems) {
                        Log.d("CartActivity", "Item: " + cart.getIceCreamName() +
                                " | Price: " + cart.getPrice() +
                                " | Quantity: " + cart.getQuantity());
                    }

                    cartAdapter = new CartAdapter(cartItems);
                    recyclerView.setAdapter(cartAdapter);

                    double totalPrice = calculateTotalPrice(cartItems);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.## VND");
                    txtTotalPrice.setText(decimalFormat.format(totalPrice));

                } else {
                    Log.e("CartActivity", "Response Error: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("CartActivity", "Error Body: " + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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