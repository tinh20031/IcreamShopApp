package com.example.iceamapp.Services;

import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface IceCreamApiService {

    @GET("api/IceCreamApi")
    Call<List<IceCream>> getAllIceCreams(); // Lấy danh sách tất cả kem

    @GET("api/IceCreamApi/{id}") // Đảm bảo đường dẫn đúng với API thực tế
    Call<IceCream> getIceCreamById(@Path("id") int id); // Lấy chi tiết kem theo ID
}
