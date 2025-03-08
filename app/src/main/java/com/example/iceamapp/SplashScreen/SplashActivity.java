package com.example.iceamapp.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.iceamapp.CartActivity;
import com.example.iceamapp.infor_user;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chuyển sang HomeActivity sau 2 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, infor_user.class));
            finish(); // Đóng SplashActivity
        }, 2000);
    } }