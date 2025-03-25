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

import com.example.iceamapp.Activity.ChatActivity;
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
import android.widget.TextView;
import android.content.SharedPreferences;

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
    private List<IceCream> originalIceCreamList = new ArrayList<>();
    private List<IceCream> filteredIceCreamList = new ArrayList<>();
    private TextView tvCartBadge;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        tvCartBadge = findViewById(R.id.cartBadge);
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
        updateCartBadge();

    }


    private void setupBanner() {
        bannerImages = Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3);
        BannerAdapter bannerAdapter = new BannerAdapter(this, bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        bannerIndicator.setViewPager(bannerViewPager);
        autoSlideBanner();
    }
    private void updateCartBadge() {
        if (tvCartBadge == null) return; // Tránh lỗi NullPointerException

        // 🔥 Lấy userId từ SharedPreferences mà không cần truyền context
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Log.e("CartBadge", "🚨 Không tìm thấy userId trong SharedPreferences!");
            tvCartBadge.setVisibility(View.GONE);
            return;
        }

        CartApiService cartApiService = RetrofitClient.getCartApiService();
        cartApiService.getCartsByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int itemCount = response.body().size();
                    Log.d("CartBadge", "📦 Số lượng sản phẩm trong giỏ hàng của user " + userId + ": " + itemCount);
                    if (itemCount > 0) {
                        tvCartBadge.setVisibility(View.VISIBLE);
                        tvCartBadge.setText(String.valueOf(itemCount));
                    } else {
                        tvCartBadge.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("CartBadge", "❌ API không trả về dữ liệu hợp lệ!");
                    tvCartBadge.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("CartBadge", "🚨 Lỗi API: " + t.getMessage());
                tvCartBadge.setVisibility(View.GONE);
            }
        });
    }





    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // Cập nhật giỏ hàng khi quay lại màn hình chính
    }



    // Gọi updateCartBadge sau khi thêm sản phẩm vào giỏ hàng
    public void refreshCartBadge() {
        updateCartBadge();
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
            View chatbotFrame = findViewById(R.id.chatbotFrame);
            if (chatbotFrame != null) {
                chatbotFrame.setOnClickListener(v -> {
                    Log.d("HomeActivity", "Chatbot icon clicked!");
                    Intent intent = new Intent(Fragment_homeActivity.this, ChatActivity.class);
                    startActivity(intent);
                });
            } else {
                Log.e("HomeActivity", "chatbotFrame not found!");
            }
        }
    }

    private void loadIceCreams() {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        apiService.getAllIceCreams().enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalIceCreamList.clear();
                    originalIceCreamList.addAll(response.body());

                    // Hiển thị tất cả sản phẩm ban đầu
                    filteredIceCreamList.clear();
                    filteredIceCreamList.addAll(originalIceCreamList);
                    iceCreamAdapter.updateData(filteredIceCreamList);
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("API", "Failed to load ice creams: " + t.getMessage());
            }
        });
    }

    private void loadIceCreamsByCategory(int categoryId) {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        apiService.getIceCreamsByCategory(categoryId).enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IceCream> iceCreams = response.body();
                    iceCreamAdapter.updateData(iceCreams);
                    Log.d("API", "🔹 Lấy thành công danh sách kem của danh mục ID: " + categoryId);
                } else {
                    Log.e("API", "⚠️ Không có sản phẩm cho danh mục ID: " + categoryId);
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("API", "🚨 Lỗi khi tải danh sách kem: " + t.getMessage());
            }
        });
    }

    private void loadCategories() {
        CategoryApiService apiService = RetrofitClient.getCategoryApiService();
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();

                    categoryAdapter = new CategoryAdapter(Fragment_homeActivity.this, categories);
                    categoryRecyclerView.setAdapter(categoryAdapter);

                    // 🟢 Thêm sự kiện khi nhấn vào danh mục
                    categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Category category) {
                            Log.d("Category Click", "Chọn danh mục ID: " + category.getCategoryId());
                            loadIceCreamsByCategory(category.getCategoryId()); // 🟢 Gọi API để lấy danh sách kem theo danh mục
                        }
                    });
                } else {
                    Log.e("API", "Không tải được danh mục");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API", "Lỗi khi tải danh mục: " + t.getMessage());
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

    private void filterIceCreamsByCategory(String categoryName) {
        filteredIceCreamList.clear();

        Log.d("Filter", "Filtering by category: " + categoryName);

        if (categoryName.equals("All")) {
            filteredIceCreamList.addAll(originalIceCreamList);
        } else {
            for (IceCream iceCream : originalIceCreamList) {
                if (iceCream.getCategory() != null &&
                        iceCream.getCategory().getName() != null) {

                    String iceCreamCategory = iceCream.getCategory().getName().trim().toLowerCase();
                    String selectedCategory = categoryName.trim().toLowerCase();

                    Log.d("Filter", "Checking: " + iceCream.getName() + " - " + iceCreamCategory + " vs " + selectedCategory);

                    if (iceCreamCategory.equals(selectedCategory)) {
                        filteredIceCreamList.add(iceCream);
                    }
                }
            }
        }

        Log.d("Filter", "Filtered List Size: " + filteredIceCreamList.size());
        iceCreamAdapter.updateData(filteredIceCreamList);
    }


}
