package com.example.iceamapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.iceamapp.CartActivity;
import com.example.iceamapp.R;
import androidx.appcompat.app.AppCompatActivity;

public class HeaderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_layout); // Đặt layout là header_layout.xml

        // Tìm và xử lý sự kiện click icon giỏ hàng
        ImageView cartIcon = findViewById(R.id.cartIcon);
        if (cartIcon != null) {
            cartIcon.setOnClickListener(v -> {
                Log.d("HeaderActivity", "Cart icon clicked!");
                Intent intent = new Intent(HeaderActivity.this, CartActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("HeaderActivity", "cartIcon not found!");
        }
    }
}
