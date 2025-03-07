package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApiService {
    @GET("api/CategoryApi") // Đường dẫn API của bạn
    Call<List<Category>> getAllCategories();
}
