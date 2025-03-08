package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iceamapp.Activity.HeaderActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mở HeaderActivity để xử lý các sự kiện trong header
        startActivity(new Intent(MainActivity.this, HeaderActivity.class));
    }
}
