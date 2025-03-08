package com.example.iceamapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.adapter.CategoryAdapter;
import com.example.iceamapp.adapter.IceCreamAdapter;
import com.example.iceamapp.entity.Category;
import com.example.iceamapp.entity.IceCream;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_homeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    private IceCreamAdapter iceCreamAdapter;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Xử lý sự kiện click vào icon giỏ hàng
        View headerView = findViewById(R.id.headerLayout);
        if (headerView != null) {
            ImageView cartIcon = headerView.findViewById(R.id.cartIcon);
            if (cartIcon != null) {
                cartIcon.setOnClickListener(v -> {
                    Intent intent = new Intent(Fragment_homeActivity.this, CartActivity.class);
                    startActivity(intent);
                });
            }
        }

        // Ánh xạ RecyclerView kem
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ánh xạ RecyclerView danh mục
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Gọi API lấy danh sách kem
        loadIceCreams();

        // Gọi API lấy danh sách danh mục
        loadCategories();
    }

    private void loadIceCreams() {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        Call<List<IceCream>> call = apiService.getAllIceCreams();

        call.enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IceCream> iceCreams = response.body();
                    iceCreamAdapter = new IceCreamAdapter(iceCreams);
                    recyclerView.setAdapter(iceCreamAdapter);
                    Log.d("API", "Loaded " + iceCreams.size() + " ice creams");
                } else {
                    Log.e("API", "Error loading ice creams: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("API", "Failed to load ice creams: " + t.getMessage());
            }
        });
    }

    private void loadCategories() {
        CategoryApiService apiService = RetrofitClient.getCategoryApiService();
        Call<List<Category>> call = apiService.getAllCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    categoryAdapter = new CategoryAdapter(Fragment_homeActivity.this, categories);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                    Log.d("API", "Loaded " + categories.size() + " categories");
                } else {
                    Log.e("API", "Error loading categories: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API", "Failed to load categories: " + t.getMessage());
            }
        });
    }
}
