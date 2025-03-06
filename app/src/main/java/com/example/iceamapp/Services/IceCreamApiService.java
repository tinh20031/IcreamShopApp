package com.example.iceamapp.Services;
import com.example.iceamapp.entity.IceCream;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface IceCreamApiService {
    @GET("api/IceCreamApi")
    Call<List<IceCream>> getAllIceCreams();

}