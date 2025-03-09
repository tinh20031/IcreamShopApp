package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.adapter.BannerAdapter;
import com.example.iceamapp.adapter.CategoryAdapter;
import com.example.iceamapp.adapter.IceCreamAdapter;
import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.Category;
import com.example.iceamapp.entity.IceCream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.relex.circleindicator.CircleIndicator3;
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
    private ViewPager2 bannerViewPager;
    private CircleIndicator3 bannerIndicator;
    private List<Integer> bannerImages;
    private Handler bannerHandler = new Handler();
    private Runnable bannerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        bannerViewPager = findViewById(R.id.bannerViewPager);
        bannerIndicator = findViewById(R.id.bannerIndicator);
        recyclerView = findViewById(R.id.recyclerView);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        iceCreamAdapter = new IceCreamAdapter(this, new ArrayList<>()); // Nếu đây là Activity
        recyclerView.setAdapter(iceCreamAdapter);

        loadIceCreams();
        loadCategories();
        setupBanner();
        setupSearch();
        setupCartNavigation();
    }

    private void setupBanner() {
        bannerImages = Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3);
        BannerAdapter bannerAdapter = new BannerAdapter(this, bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        bannerIndicator.setViewPager(bannerViewPager);
        autoSlideBanner();
    }

    private void autoSlideBanner() {
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = bannerViewPager.getCurrentItem();
                bannerViewPager.setCurrentItem((currentItem + 1) % bannerImages.size());
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    private void setupSearch() {
        searchButton.setOnClickListener(v -> searchIceCreams(searchEditText.getText().toString().trim()));

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                searchIceCreams(searchEditText.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void setupCartNavigation() {
        View cartFrame = findViewById(R.id.cartFrame);
        if (cartFrame != null) {
            cartFrame.setOnClickListener(v -> {
                Intent intent = new Intent(Fragment_homeActivity.this, CartActivity.class);
                startActivity(intent);
            });

            View userFrame = findViewById(R.id.userFrame);
            if (userFrame != null) {
                userFrame.setOnClickListener(v -> {
                    Log.d("HomeActivity", "User avatar clicked!");
                    Intent intent = new Intent(Fragment_homeActivity.this, infor_user.class);
                    startActivity(intent);
                });
            } else {
                Log.e("HomeActivity", "userFrame not found!");
            }
        }
    }

    private void loadIceCreams() {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        apiService.getAllIceCreams().enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    iceCreamAdapter.updateData(response.body());
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
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter = new CategoryAdapter(Fragment_homeActivity.this, response.body());
                    categoryRecyclerView.setAdapter(categoryAdapter);
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

        if (name == null || name.trim().isEmpty()) {
            Log.d("SearchAPI", "🔄 Từ khóa trống, tải lại danh sách mặc định.");
            loadDefaultIceCreams();
            return;
        }

        apiService.searchIceCream(name).enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (!response.isSuccessful()) {
                    Log.e("SearchAPI", "❌ Lỗi API - Mã: " + response.code());
                    return;
                }

                List<IceCream> result = response.body();
                if (result == null || result.isEmpty()) {
                    Log.w("SearchAPI", "⚠️ Không tìm thấy kết quả cho: " + name);
                    iceCreamAdapter.updateData(new ArrayList<>()); // Xóa danh sách nếu không tìm thấy
                    return;
                }

                Log.d("SearchAPI", "✅ Tìm thấy " + result.size() + " kết quả.");
                iceCreamAdapter.updateData(result);
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("SearchAPI", "🚨 Lỗi kết nối API: " + t.getMessage(), t);
            }
        });
    }

    // 🟢 Hàm lấy danh sách kem mặc định
    private void loadDefaultIceCreams() {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        apiService.getAllIceCreams().enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    iceCreamAdapter.updateData(response.body());
                    Log.d("LoadDefault", "✅ Danh sách mặc định được tải.");
                } else {
                    Log.e("LoadDefault", "❌ Không thể tải danh sách mặc định.");
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("LoadDefault", "🚨 Lỗi kết nối API: " + t.getMessage(), t);
            }
        });
    }



}
