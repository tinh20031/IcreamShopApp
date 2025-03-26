package com.example.iceamapp.Services;

import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface IceCreamApiService {

    @GET("api/IceCreamApi")
    Call<List<IceCream>> getAllIceCreams();

    @GET("api/IceCreamApi/{id}")
    Call<IceCream> getIceCreamById(@Path("id") int id);

    @GET("api/CategoryApi/{id}/ice_cream")
    Call<List<IceCream>> getIceCreamsByCategory(@Path("id") int categoryId);

    @GET("api/IceCreamApi/search")
    Call<List<IceCream>> searchIceCream(@Query("name") String name);

    // Thêm các phương thức mới để khớp với backend
    @POST("api/IceCreamApi")
    Call<IceCream> addIceCream(@Body IceCream iceCream);

    @PUT("api/IceCreamApi/{id}")
    Call<Void> editIceCream(@Path("id") int id, @Body IceCream iceCream);

    @DELETE("api/IceCreamApi/{id}")
    Call<Void> deleteIceCream(@Path("id") int id);
}