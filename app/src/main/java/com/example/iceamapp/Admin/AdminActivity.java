package com.example.iceamapp.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iceamapp.LoginActivity;
import com.example.iceamapp.R;

public class AdminActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserInfo;
    private Button btnLogout, btnManageProducts, btnManageCategories,btnManageOrders; // Thêm nút Quản lý sản phẩm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Khởi tạo các thành phần giao diện
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageProducts = findViewById(R.id.btnManageProducts); // Khởi tạo nút mới
        btnManageCategories = findViewById(R.id.btnManageCategories);
        btnManageOrders = findViewById(R.id.btnManageOrders);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "Không xác định");
        String email = sharedPreferences.getString("email", "Không xác định");
        String phone = sharedPreferences.getString("phone", "Không xác định");
        String address = sharedPreferences.getString("address", "Không xác định");
        String role = sharedPreferences.getString("role", "Không xác định");

        // Hiển thị thông tin chào mừng và thông tin người dùng
        tvWelcome.setText("Chào mừng Admin, " + fullName + "!");
        tvUserInfo.setText("Email: " + email + "\n" +
                "Số điện thoại: " + phone + "\n" +
                "Địa chỉ: " + address + "\n" +
                "Vai trò: " + role);

        // Xử lý sự kiện nút Quản lý sản phẩm
        btnManageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút Quản lý danh mục
        btnManageCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ManageCategoriesActivity.class);
                startActivity(intent);
            }
        });

        btnManageOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageOrdersActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nút Đăng xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa thông tin trong SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}