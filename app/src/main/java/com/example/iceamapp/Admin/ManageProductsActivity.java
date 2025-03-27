package com.example.iceamapp.Admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.entity.IceCream;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private MaterialButton btnAddProduct;
    private MaterialButton btnBack; // Thêm biến cho nút Back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnBack = findViewById(R.id.btnBack); // Liên kết với nút Back trong layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();

        btnAddProduct.setOnClickListener(v -> showAddEditDialog(null));

        // Xử lý sự kiện khi nhấn nút Back
        btnBack.setOnClickListener(v -> finish()); // Gọi finish() để quay lại màn hình trước đó
    }

    private void loadProducts() {
        IceCreamApiService apiService = RetrofitClient.getRetrofitInstance().create(IceCreamApiService.class);
        Call<List<IceCream>> call = apiService.getAllIceCreams(); // Sử dụng API getAllIceCreams

        call.enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<IceCream> productList = response.body();
                    productAdapter = new ProductAdapter(productList, ManageProductsActivity.this::showAddEditDialog, ManageProductsActivity.this::deleteProduct);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(ManageProductsActivity.this, "Không thể tải danh sách kem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("ManageProducts", "Lỗi: " + t.getMessage());
                Toast.makeText(ManageProductsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddEditDialog(IceCream iceCream) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_product, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etStock = dialogView.findViewById(R.id.etStock);
        EditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);
        EditText etCategoryId = dialogView.findViewById(R.id.etCategoryId);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        if (iceCream != null) {
            etName.setText(iceCream.getName());
            etDescription.setText(iceCream.getDescription());
            etPrice.setText(String.valueOf(iceCream.getPrice()));
            etStock.setText(String.valueOf(iceCream.getStock()));
            etImageUrl.setText(iceCream.getImageUrl());
            etCategoryId.setText(String.valueOf(iceCream.getCategoryId()));
            builder.setTitle("Chỉnh sửa kem");
        } else {
            builder.setTitle("Thêm kem mới");
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String priceStr = etPrice.getText().toString();
            String stockStr = etStock.getText().toString();
            String imageUrl = etImageUrl.getText().toString();
            String categoryIdStr = etCategoryId.getText().toString();

            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || categoryIdStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            IceCream newIceCream = new IceCream();
            newIceCream.setName(name);
            newIceCream.setDescription(description);
            newIceCream.setPrice(Double.parseDouble(priceStr));
            newIceCream.setStock(Integer.parseInt(stockStr));
            newIceCream.setImageUrl(imageUrl);
            newIceCream.setCategoryId(Integer.parseInt(categoryIdStr));

            if (iceCream != null) {
                newIceCream.setIceCreamId(iceCream.getIceCreamId());
                editProduct(newIceCream);
            } else {
                addProduct(newIceCream);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addProduct(IceCream iceCream) {
        IceCreamApiService apiService = RetrofitClient.getRetrofitInstance().create(IceCreamApiService.class);
        Call<IceCream> call = apiService.addIceCream(iceCream); // Gọi API POST từ backend

        call.enqueue(new Callback<IceCream>() {
            @Override
            public void onResponse(Call<IceCream> call, Response<IceCream> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProductsActivity.this, "Thêm kem thành công", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ManageProductsActivity.this, "Lỗi khi thêm kem: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IceCream> call, Throwable t) {
                Toast.makeText(ManageProductsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editProduct(IceCream iceCream) {
        IceCreamApiService apiService = RetrofitClient.getRetrofitInstance().create(IceCreamApiService.class);
        Call<Void> call = apiService.editIceCream(iceCream.getIceCreamId(), iceCream); // Gọi API PUT từ backend

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProductsActivity.this, "Sửa kem thành công", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ManageProductsActivity.this, "Lỗi khi sửa kem: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageProductsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(IceCream iceCream) {
        IceCreamApiService apiService = RetrofitClient.getRetrofitInstance().create(IceCreamApiService.class);
        Call<Void> call = apiService.deleteIceCream(iceCream.getIceCreamId()); // Gọi API DELETE từ backend

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProductsActivity.this, "Xóa kem thành công", Toast.LENGTH_SHORT).show();
                    loadProducts();
                } else {
                    Toast.makeText(ManageProductsActivity.this, "Lỗi khi xóa kem: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageProductsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}