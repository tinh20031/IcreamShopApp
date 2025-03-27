package com.example.iceamapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.iceamapp.Activity.ChatActivityRealtime;
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

        iceCreamAdapter = new IceCreamAdapter(this, new ArrayList<>());
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
        if (tvCartBadge == null) return;

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            tvCartBadge.setVisibility(View.GONE);
            return;
        }

        CartApiService cartApiService = RetrofitClient.getCartApiService();
        cartApiService.getCartsByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int itemCount = response.body().size();
                    if (itemCount > 0) {
                        tvCartBadge.setVisibility(View.VISIBLE);
                        tvCartBadge.setText(String.valueOf(itemCount));
                    } else {
                        tvCartBadge.setVisibility(View.GONE);
                    }
                } else {
                    tvCartBadge.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                tvCartBadge.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

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
            View Chatsup = findViewById(R.id.Chatsup);
            if (Chatsup != null) {
                Chatsup.setOnClickListener(v -> {
                    Log.d("HomeActivity", "chat real time clicked!");
                    Intent intent = new Intent(Fragment_homeActivity.this, ChatActivityRealtime.class);
                    startActivity(intent);
                });
            } else {
                Log.e("HomeActivity", "chatrealtime not found!");
            }
            View userFrame = findViewById(R.id.userFrame);
            if (userFrame != null) {
                userFrame.setOnClickListener(v -> {
                    Intent intent = new Intent(Fragment_homeActivity.this, infor_user.class);
                    startActivity(intent);
                });
            }

            View chatbotFrame = findViewById(R.id.chatbotFrame);
            if (chatbotFrame != null) {
                chatbotFrame.setOnClickListener(v -> {
                    Intent intent = new Intent(Fragment_homeActivity.this, ChatActivity.class);
                    startActivity(intent);
                });
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

                    filteredIceCreamList.clear();
                    filteredIceCreamList.addAll(originalIceCreamList);
                    iceCreamAdapter.updateData(filteredIceCreamList);
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                // Không hiển thị log
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
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                // Không hiển thị log
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

                    categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Category category) {
                            loadIceCreamsByCategory(category.getCategoryId());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Không hiển thị log
            }
        });
    }

    private void searchIceCreams(String name) {
        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();

        if (name == null || name.trim().isEmpty()) {
            loadDefaultIceCreams();
            return;
        }

        apiService.searchIceCream(name).enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<IceCream> result = response.body();
                if (result == null || result.isEmpty()) {
                    iceCreamAdapter.updateData(new ArrayList<>());
                    return;
                }

                iceCreamAdapter.updateData(result);
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                // Không hiển thị log
            }
        });
    }

    private void loadDefaultIceCreams() {
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
                // Không hiển thị log
            }
        });
    }

    private void filterIceCreamsByCategory(String categoryName) {
        filteredIceCreamList.clear();

        if (categoryName.equals("All")) {
            filteredIceCreamList.addAll(originalIceCreamList);
        } else {
            for (IceCream iceCream : originalIceCreamList) {
                if (iceCream.getCategory() != null &&
                        iceCream.getCategory().getName() != null) {

                    String iceCreamCategory = iceCream.getCategory().getName().trim().toLowerCase();
                    String selectedCategory = categoryName.trim().toLowerCase();

                    if (iceCreamCategory.equals(selectedCategory)) {
                        filteredIceCreamList.add(iceCream);
                    }
                }
            }
        }

        iceCreamAdapter.updateData(filteredIceCreamList);
    }
}