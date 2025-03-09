package com.example.iceamapp;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.view.View;
import com.example.iceamapp.Services.AuthApiService;
import com.example.iceamapp.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailInfoUserActivity extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, passwordEditText, phoneEditText, addressEditText, roleEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;
    private AuthApiService authApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_user);

        // Khởi tạo các view
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        roleEditText = findViewById(R.id.roleEditText);
        saveButton = findViewById(R.id.saveButton);

        // Ánh xạ nút Back
        ImageView backButton = findViewById(R.id.backButton);

        // Thêm sự kiện click để quay lại màn hình trước đó
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại
            }
        });

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        authApiService = retrofit.create(AuthApiService.class);

        // Lưu thông tin khi nhấn nút Lưu
        saveButton.setOnClickListener(v -> updateUserInfo());

        // Tải thông tin ban đầu
        loadUserInfoFromServer(); // Tải từ server thay vì chỉ từ SharedPreferences
    }


    private void loadUserInfoFromServer() {
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy userId", Toast.LENGTH_SHORT).show();
            return;
        }

        authApiService.getUserDetails(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    updateSharedPreferences(user); // Cập nhật SharedPreferences
                    displayUserInfo(user); // Hiển thị lên giao diện
                } else {
                    Log.e("LoadUserInfo", "Lỗi khi tải thông tin: " + response.code());
                    loadUserInfoFromPrefs(); // Fallback về SharedPreferences nếu server lỗi
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("LoadUserInfo", "Lỗi kết nối: " + t.getMessage());
                loadUserInfoFromPrefs(); // Fallback về SharedPreferences nếu lỗi
            }
        });
    }

    private void loadUserInfoFromPrefs() {
        fullNameEditText.setText(sharedPreferences.getString("fullName", ""));
        emailEditText.setText(sharedPreferences.getString("email", ""));
        passwordEditText.setText(sharedPreferences.getString("passwordHash", ""));
        phoneEditText.setText(sharedPreferences.getString("phone", ""));
        addressEditText.setText(sharedPreferences.getString("address", ""));
        roleEditText.setText(sharedPreferences.getString("role", ""));
    }

    private void displayUserInfo(User user) {
        fullNameEditText.setText(user.getFullName() != null ? user.getFullName() : "");
        emailEditText.setText(user.getEmail() != null ? user.getEmail() : "");
        passwordEditText.setText(user.getPasswordHash() != null ? user.getPasswordHash() : "");
        phoneEditText.setText(user.getPhone() != null ? user.getPhone() : "");
        addressEditText.setText(user.getAddress() != null ? user.getAddress() : "");
        roleEditText.setText(user.getRole() != null ? user.getRole() : "");
    }

    private void updateSharedPreferences(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", user.getFullName());
        editor.putString("email", user.getEmail());
        editor.putString("passwordHash", user.getPasswordHash());
        editor.putString("phone", user.getPhone());
        editor.putString("address", user.getAddress());
        editor.putString("role", user.getRole());
        editor.apply();
    }

    private void updateUserInfo() {
        String fullName = fullNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String passwordHash = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String originalPassword = sharedPreferences.getString("passwordHash", "");
        String originalRole = sharedPreferences.getString("role", "");
        if (passwordHash.isEmpty()) {
            passwordHash = originalPassword;
        }

        int userId = sharedPreferences.getInt("userId", -1);
        User updatedUser = new User(userId, fullName, email, passwordHash, phone, address, originalRole);

        authApiService.updateUserDetails(userId, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    updateSharedPreferences(user); // Cập nhật SharedPreferences
                    displayUserInfo(user); // Cập nhật giao diện ngay lập tức
                    Toast.makeText(DetailInfoUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailInfoUserActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(DetailInfoUserActivity.this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfoFromServer(); // Tải lại từ server khi quay lại
    }
}