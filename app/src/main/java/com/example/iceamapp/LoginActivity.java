package com.example.iceamapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iceamapp.Admin.AdminActivity;
import com.example.iceamapp.Services.AuthApiService;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.entity.User;
import com.example.iceamapp.entity.LoginRequest;
import com.google.gson.Gson;

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
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        tvSignupText = findViewById(R.id.tvSignupText);

        // Sự kiện khi nhấp vào "Đăng ký"
        tvSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện khi nhấp vào nút đăng nhập
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

        // Kiểm tra xem email hoặc mật khẩu có rỗng không
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AuthApiService authApiService = retrofit.create(AuthApiService.class);

        // Gửi yêu cầu đăng nhập
        LoginRequest loginRequest = new LoginRequest(email, password);
        authApiService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        Log.d("UserData", "Dữ liệu thô từ API: " + new Gson().toJson(user));

                        // Lưu userId vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("userId", user.getUserId());
                        editor.apply();

                        // Gọi API để lấy thông tin chi tiết người dùng
                        AuthApiService authApiService = RetrofitClient.getRetrofitInstance().create(AuthApiService.class);
                        authApiService.getUserDetails(user.getUserId()).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    User detailedUser = response.body();
                                    if (detailedUser != null) {
                                        Log.d("UserData", "Thông tin chi tiết: " + new Gson().toJson(detailedUser));
                                        // Lưu thông tin chi tiết vào SharedPreferences
                                        editor.putString("fullName", detailedUser.getFullName() != null ? detailedUser.getFullName() : "Không xác định");
                                        editor.putString("email", detailedUser.getEmail() != null ? detailedUser.getEmail() : "Không xác định");
                                        editor.putString("phone", detailedUser.getPhone() != null ? detailedUser.getPhone() : "Không xác định");
                                        editor.putString("address", detailedUser.getAddress() != null ? detailedUser.getAddress() : "Không xác định");
                                        String userRole = detailedUser.getRole() != null ? detailedUser.getRole() : "Không xác định";
                                        editor.putString("role", userRole);
                                        editor.apply();

                                        // Kiểm tra vai trò và chuyển hướng
                                        Intent intent;
                                        if ("admin".equalsIgnoreCase(userRole)) {
                                            intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        } else {
                                            intent = new Intent(LoginActivity.this, Fragment_homeActivity.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Log.e("UserData", "Lỗi khi lấy chi tiết: " + response.code());
                                    // Chuyển hướng mặc định đến Fragment_homeActivity nếu lỗi
                                    Intent intent = new Intent(LoginActivity.this, Fragment_homeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("UserData", "Lỗi kết nối: " + t.getMessage());
                                // Chuyển hướng mặc định đến Fragment_homeActivity nếu lỗi
                                Intent intent = new Intent(LoginActivity.this, Fragment_homeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginError", "Mã lỗi: " + response.code() + " Thông báo: " + response.message());
                    try {
                        Log.e("LoginError", "Nội dung lỗi: " + response.errorBody().string());
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