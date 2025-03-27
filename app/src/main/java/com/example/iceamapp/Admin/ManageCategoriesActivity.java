package com.example.iceamapp.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.RetrofitClient;
import com.example.iceamapp.Services.CategoryApiService;
import com.example.iceamapp.entity.Category;
import com.google.android.material.button.MaterialButton;

import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private MaterialButton btnAddCategory;
    private MaterialButton btnBack;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnBack = findViewById(R.id.btnBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                selectedImageUri = result.getData().getData();
            }
        });

        loadCategories();

        btnAddCategory.setOnClickListener(v -> showAddEditDialog(null));
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCategories() {
        CategoryApiService apiService = RetrofitClient.getRetrofitInstance().create(CategoryApiService.class);
        Call<List<Category>> call = apiService.getAllCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(categoryList, ManageCategoriesActivity.this::showAddEditDialog, ManageCategoriesActivity.this::deleteCategory);
                    recyclerView.setAdapter(categoryAdapter);
                } else {
                    Toast.makeText(ManageCategoriesActivity.this, "Không thể tải danh sách danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddEditDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_category, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        if (category != null) {
            etName.setText(category.getName());
            selectedImageUri = null;
            builder.setTitle("Chỉnh sửa danh mục");
        } else {
            selectedImageUri = null;
            builder.setTitle("Thêm danh mục mới");
        }

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category == null && selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh cho danh mục mới", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category != null) {
                editCategory(category.getCategoryId(), name, selectedImageUri);
            } else {
                addCategory(name, selectedImageUri);
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void addCategory(String name, Uri imageUri) {
        CategoryApiService apiService = RetrofitClient.getRetrofitInstance().create(CategoryApiService.class);

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        MultipartBody.Part imagePart = null;
        try {
            if (imageUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    byte[] imageBytes = new byte[inputStream.available()];
                    inputStream.read(imageBytes);
                    inputStream.close();

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                    imagePart = MultipartBody.Part.createFormData("image", "category_image.jpg", requestFile);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đọc file ảnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Call<Category> call = apiService.addCategory(name, imagePart);

        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageCategoriesActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    loadCategories();
                } else {
                    String errorMessage = response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            // Không hiển thị lỗi phân tích errorBody bằng Toast
                        }
                    }
                    Toast.makeText(ManageCategoriesActivity.this, "Lỗi khi thêm danh mục: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editCategory(int categoryId, String name, Uri imageUri) {
        CategoryApiService apiService = RetrofitClient.getRetrofitInstance().create(CategoryApiService.class);

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        MultipartBody.Part imagePart = null;
        try {
            if (imageUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    byte[] imageBytes = new byte[inputStream.available()];
                    inputStream.read(imageBytes);
                    inputStream.close();

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                    imagePart = MultipartBody.Part.createFormData("image", "category_image.jpg", requestFile);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đọc file ảnh: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Call<Void> call = apiService.editCategory(categoryId, name, imagePart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageCategoriesActivity.this, "Sửa danh mục thành công", Toast.LENGTH_SHORT).show();
                    loadCategories();
                } else {
                    String errorMessage = response.message();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            // Không hiển thị lỗi phân tích errorBody bằng Toast
                        }
                    }
                    Toast.makeText(ManageCategoriesActivity.this, "Lỗi khi sửa danh mục: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageCategoriesActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteCategory(Category category) {
        // Hiển thị dialog xác nhận trước khi xóa
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục '" + category.getName() + "' không?")
                .setPositiveButton("OK", (dialogInterface, which) -> {
                    // Nếu nhấn OK, gọi API xóa danh mục
                    CategoryApiService apiService = RetrofitClient.getRetrofitInstance().create(CategoryApiService.class);
                    Call<Void> call = apiService.deleteCategory(category.getCategoryId());

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ManageCategoriesActivity.this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                                loadCategories();
                            } else {
                                Toast.makeText(ManageCategoriesActivity.this, "Lỗi khi xóa danh mục: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ManageCategoriesActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    // Nếu nhấn Cancel, đóng dialog và không làm gì cả
                    dialogInterface.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

        // Hiển thị dialog
        dialog.show();

        // Đặt màu đen cho nút "OK" và "Cancel"
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
    }
}