package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class infor_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_user);

        // Ánh xạ icon Back
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Quay lại trang trước
        });

        // Ánh xạ các mục
        LinearLayout userInfoItem = findViewById(R.id.userInfoItem);
        LinearLayout ordersItem = findViewById(R.id.ordersItem);
        LinearLayout logoutItem = findViewById(R.id.logoutItem);

        // Xử lý sự kiện cho từng mục
//        userInfoItem.setOnClickListener(v -> {
//            Log.d("Settings", "Thông tin người dùng clicked");
//            // Chuyển sang trang Thông tin người dùng
//            Intent intent = new Intent(infor_user.this, UserInfoActivity.class);
//            startActivity(intent);
//        });

//        ordersItem.setOnClickListener(v -> {
//            Log.d("Settings", "Đơn hàng clicked");
//            // Chuyển sang trang Đơn hàng
//            Intent intent = new Intent(infor_user.this, OrdersActivity.class);
//            startActivity(intent);
//        });

        logoutItem.setOnClickListener(v -> {
            Log.d("Settings", "Đăng xuất clicked");
            // Chuyển sang trang Đăng xuất (hoặc xử lý logout)
            Intent intent = new Intent(infor_user.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}