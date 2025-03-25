package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iceamapp.Services.AuthApiService;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.entity.User;
import com.example.iceamapp.entity.RegisterRequest;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.fullname);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registerButton);

        // Xử lý sự kiện khi người dùng nhấn nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }



    private void register() {
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String address = "Quy Nhơn";
        String phoneNumber = "54235325";
        String role = "user";

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo instance của Retrofit
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        AuthApiService authApiService = retrofit.create(AuthApiService.class);

        // Chuẩn bị request
        RegisterRequest registerRequest = new RegisterRequest(fullName, email, password, address, phoneNumber, role);

        authApiService.register(registerRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseString = response.body().string(); // Đọc body chỉ một lần
                        Log.d("RegisterResponse", "Response: " + responseString);

                        // Thông báo đăng ký thành công
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình đăng nhập
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (IOException e) {
                        Log.e("RegisterError", "Lỗi đọc response: " + e.getMessage());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có nội dung lỗi";
                        Log.e("RegisterError", "Đăng ký thất bại: " + response.code() + " - " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Lỗi: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("RegisterError", "Lỗi khi đọc errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RegisterError", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
