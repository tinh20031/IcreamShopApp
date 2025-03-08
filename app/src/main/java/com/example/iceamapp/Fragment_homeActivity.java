package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.adapter.CategoryAdapter;
import com.example.iceamapp.adapter.IceCreamAdapter;
import com.example.iceamapp.entity.Category;
import com.example.iceamapp.entity.IceCream;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_homeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    private IceCreamAdapter iceCreamAdapter;
    private CategoryAdapter categoryAdapter;
    private EditText searchEditText;
    private ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Ánh xạ RecyclerView kem
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ánh xạ RecyclerView danh mục
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Ánh xạ EditText và nút tìm kiếm
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // Khởi tạo adapter với danh sách rỗng ban đầu
        iceCreamAdapter = new IceCreamAdapter(new ArrayList<>());
        recyclerView.setAdapter(iceCreamAdapter);

        // Gọi API lấy danh sách kem mặc định
        loadIceCreams();

        // Gọi API lấy danh sách danh mục
        loadCategories();

        // Sự kiện khi nhấn nút tìm kiếm
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            Log.d("SearchDebug", "Search button clicked, query: " + query);
            if (!query.isEmpty()) {
                searchIceCreams(query);
            }
        });

        // Sự kiện khi nhấn Enter trên bàn phím
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String query = searchEditText.getText().toString().trim();
                Log.d("SearchDebug", "Enter pressed, query: " + query);
                if (!query.isEmpty()) {
                    searchIceCreams(query);
                }
                return true;
            }
            return false;
        });

        // Tìm kiếm theo thời gian thực khi người dùng nhập
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                Log.d("SearchDebug", "Text changed: " + query);
                if (!query.isEmpty()) {
                    searchIceCreams(query);
                } else {
                    loadIceCreams(); // Tải lại danh sách mặc định khi xóa hết
                }
            }
        });

        // Xử lý sự kiện nhấn cho các icon trong header
        View cartFrame = findViewById(R.id.cartFrame);
        if (cartFrame != null) {
            cartFrame.setOnClickListener(v -> {
                Log.d("HomeActivity", "Cart icon clicked!");
                Intent intent = new Intent(Fragment_homeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("HomeActivity", "cartFrame not found!");
        }

//        View notificationFrame = findViewById(R.id.notificationFrame);
//        if (notificationFrame != null) {
//            notificationFrame.setOnClickListener(v -> {
//                Log.d("HomeActivity", "Notification icon clicked!");
//                Intent intent = new Intent(Fragment_homeActivity.this, NotificationActivity.class);
//                startActivity(intent);
//            });
//        } else {
//            Log.e("HomeActivity", "notificationFrame not found!");
//        }

//        View userFrame = findViewById(R.id.userFrame);
//        if (userFrame != null) {
//            userFrame.setOnClickListener(v -> {
//                Log.d("HomeActivity", "User avatar clicked!");
//                Intent intent = new Intent(Fragment_homeActivity.this, UserProfileActivity.class);
//                startActivity(intent);
//            });
//        } else {
//            Log.e("HomeActivity", "userFrame not found!");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đảm bảo sự kiện nhấn hoạt động sau khi quay lại
        setupIconListeners();
    }

    private void setupIconListeners() {
        View cartFrame = findViewById(R.id.cartFrame);
        if (cartFrame != null) {
            cartFrame.setOnClickListener(v -> {
                Log.d("HomeActivity", "Cart icon clicked!");
                Intent intent = new Intent(Fragment_homeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

//        View notificationFrame = findViewById(R.id.notificationFrame);
//        if (notificationFrame != null) {
//            notificationFrame.setOnClickListener(v -> {
//                Log.d("HomeActivity", "Notification icon clicked!");
//                Intent intent = new Intent(Fragment_homeActivity.this, NotificationActivity.class);
//                startActivity(intent);
//            });
//        }

//        View userFrame = findViewById(R.id.userFrame);
//        if (userFrame != null) {
//            userFrame.setOnClickListener(v -> {
//                Log.d("HomeActivity", "User avatar clicked!");
//                Intent intent = new Intent(Fragment_homeActivity.this, UserProfileActivity.class);
//                startActivity(intent);
//            });
//        }
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

    private void searchIceCreams(String name) {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        apiService.searchIceCream(name).enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IceCream> iceCreams = response.body();
                    Log.d("SearchAPI", "Search Success: " + iceCreams.size() + " items");
                    iceCreamAdapter = new IceCreamAdapter(iceCreams);
                    recyclerView.setAdapter(iceCreamAdapter);
                    iceCreamAdapter.notifyDataSetChanged(); // Đảm bảo RecyclerView cập nhật
                } else {
                    Log.e("SearchAPI", "Search Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("SearchAPI", "API Error: " + t.getMessage(), t);
            }
        });
    }
}