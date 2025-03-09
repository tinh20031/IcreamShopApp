package com.example.iceamapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailInfoUserActivity extends AppCompatActivity {

    private TextView fullNameTextView, emailTextView, phoneTextView, addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_user);

        // Ánh xạ các TextViews
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String fullName = sharedPreferences.getString("fullName", "Không xác định");
        String email = sharedPreferences.getString("email", "Không xác định");
        String phone = sharedPreferences.getString("phone", "Không xác định");
        String address = sharedPreferences.getString("address", "Không xác định");

// Hiển thị thông tin lên các TextView
        TextView fullNameTextView = findViewById(R.id.fullNameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);

        fullNameTextView.setText("Full Name: " + fullName);
        emailTextView.setText("Email: " + email);
        phoneTextView.setText("Phone: " + phone);
        addressTextView.setText("Address: " + address);

    }
}
