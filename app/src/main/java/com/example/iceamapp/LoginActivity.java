//package com.example.iceamapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class LoginActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login); // Đảm bảo bạn đang dùng đúng file XML
//
//        // Tìm nút btnLoginPassword trong layout
//        TextView tvSignupText = findViewById(R.id.tvSignupText);
//
//        if (tvSignupText != null){
//            tvSignupText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//    }
//}
package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iceamapp.Services.AuthApiService;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.entity.User;
import com.example.iceamapp.entity.LoginRequest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView tvSignupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Đảm bảo bạn đang dùng đúng file XML

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        tvSignupText = findViewById(R.id.tvSignupText);

        // Khi người dùng click vào "Đăng ký"
        tvSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Activity đăng ký
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrofit instance
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AuthApiService authApiService = retrofit.create(AuthApiService.class);

        // Gọi API login
        LoginRequest loginRequest = new LoginRequest(email, password);
        authApiService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        // Lưu thông tin người dùng vào SharedPreferences hoặc session
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Đóng Activity Login
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log phản hồi từ server để kiểm tra lý do lỗi
                    Log.e("LoginError", "Code: " + response.code() + " Message: " + response.message());
                    try {
                        Log.e("LoginError", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
