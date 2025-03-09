package com.example.iceamapp.Services;

import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface IceCreamApiService {

    @GET("api/IceCreamApi")
    Call<List<IceCream>> getAllIceCreams(); // Lấy danh sách tất cả kem

    @GET("api/IceCreamApi/{id}") // Đảm bảo đường dẫn đúng với API thực tế
    Call<IceCream> getIceCreamById(@Path("id") int id); // Lấy chi tiết kem theo ID

    @GET("api/CategoryApi/{id}/ice_cream") // 🟢 Gọi API theo danh mục
    Call<List<IceCream>> getIceCreamsByCategory(@Path("id") int categoryId);
    @GET("api/IceCreamApi/search")
    Call<List<IceCream>> searchIceCream(@Query("name") String name);
}
