package com.example.iceamapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.Services.IceCreamApiService;
import com.example.iceamapp.adapter.IceCreamAdapter;
import com.example.iceamapp.entity.IceCream;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_homeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IceCreamAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        IceCreamApiService apiService = RetrofitClient.getIceCreamApiService();
        Call<List<IceCream>> call = apiService.getAllIceCreams();

        call.enqueue(new Callback<List<IceCream>>() {
            @Override
            public void onResponse(Call<List<IceCream>> call, Response<List<IceCream>> response) {
                if (response.isSuccessful()) {
                    List<IceCream> iceCreams = response.body();
                    Log.d("API", "Response: " + response.body());

                    if (iceCreams != null && !iceCreams.isEmpty()) {
                        adapter = new IceCreamAdapter(iceCreams);
                        recyclerView.setAdapter(adapter);
                        Log.d("MainActivity", "Adapter set with " + iceCreams.size() + " items");
                    } else {
                        Log.e("MainActivity", "Empty or null data received");
                    }
                } else {
                    Log.e("MainActivity", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<IceCream>> call, Throwable t) {
                Log.e("MainActivity", "Failure: " + t.getMessage());
            }
        });

    }
}