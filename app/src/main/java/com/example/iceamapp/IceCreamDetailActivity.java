package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IceCreamDetailActivity extends AppCompatActivity {
    private ImageView iceCreamImage;
    private TextView iceCreamName, iceCreamDescription;
    private Button buyButton;
    private IceCreamApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_cream_detail);

        // Ánh xạ view
        iceCreamImage = findViewById(R.id.iceCreamImage);
        iceCreamName = findViewById(R.id.iceCreamName);
        iceCreamDescription = findViewById(R.id.iceCreamDescription);
        buyButton = findViewById(R.id.buyButton);

        // Ánh xạ nút Back và xử lý sự kiện click để chuyển về HomeActivity
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Tạo Intent để chuyển về HomeActivity
            Intent intent = new Intent(IceCreamDetailActivity.this, Fragment_homeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Tùy chọn để tránh tạo mới nếu HomeActivity đã tồn tại
            startActivity(intent);
            finish(); // Đóng CartActivity sau khi chuyển
        });

        // Nhận ID từ Intent
        Intent intent = getIntent();
        int iceCreamId = intent.getIntExtra("ICE_CREAM_ID", -1);

        // Gọi API để lấy chi tiết sản phẩm
        if (iceCreamId != -1) {
            fetchIceCreamDetails(iceCreamId);
        }
    }

    private void fetchIceCreamDetails(int id) {
        apiService = RetrofitClient.getRetrofitInstance().create(IceCreamApiService.class);
        apiService.getIceCreamById(id).enqueue(new Callback<IceCream>() {
            @Override
            public void onResponse(Call<IceCream> call, Response<IceCream> response) {
                if (response.isSuccessful() && response.body() != null) {
                    IceCream iceCream = response.body();
                    iceCreamName.setText(iceCream.getName());
                    iceCreamDescription.setText(iceCream.getDescription());
                    Glide.with(IceCreamDetailActivity.this)
                            .load(iceCream.getImageUrl())
                            .into(iceCreamImage);
                }
            }

            @Override
            public void onFailure(Call<IceCream> call, Throwable t) {
                iceCreamName.setText("Lỗi tải dữ liệu");
            }
        });
    }
}
