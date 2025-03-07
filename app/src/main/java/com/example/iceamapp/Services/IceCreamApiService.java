package com.example.iceamapp.Services;
import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface IceCreamApiService {
    @GET("api/IceCreamApi")
    Call<List<IceCream>> getAllIceCreams();

    @GET("api/IceCreamApi/search")
    Call<List<IceCream>> searchIceCream(@Query("name") String name);
}