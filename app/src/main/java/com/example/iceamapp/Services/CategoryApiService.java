package com.example.iceamapp.Services;

import com.example.iceamapp.entity.Category;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CategoryApiService {
    @GET("api/CategoryApi")
    Call<List<Category>> getAllCategories();

    @GET("api/CategoryApi/{id}")
    Call<Category> getCategoryById(@Path("id") int id);

    @Multipart
    @POST("api/CategoryApi")
    Call<Category> addCategory(
            @Part("name") String name,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("api/CategoryApi/{id}")
    Call<Void> editCategory(
            @Path("id") int id,
            @Part("name") String name,
            @Part MultipartBody.Part image // image có thể null
    );

    @DELETE("api/CategoryApi/{id}")
    Call<Void> deleteCategory(@Path("id") int id);
}