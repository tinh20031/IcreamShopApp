package com.example.iceamapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.iceamapp.IceCreamDetailActivity;
import com.example.iceamapp.R;
import com.example.iceamapp.entity.Cart;
import com.example.iceamapp.entity.IceCream;
import com.example.iceamapp.Services.CartApiService;
import com.example.iceamapp.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import com.example.iceamapp.Fragment_homeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IceCreamAdapter extends RecyclerView.Adapter<IceCreamAdapter.ViewHolder> {
    private List<IceCream> iceCreams;
    private final Context context;

    public IceCreamAdapter(Context context, List<IceCream> iceCreams) {
        this.context = context;
        this.iceCreams = new ArrayList<>(iceCreams);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDescription, tvPrice, tvStock;
        public ImageView imgIceCream, imgAddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            imgIceCream = itemView.findViewById(R.id.imgIceCream);
            imgAddToCart = itemView.findViewById(R.id.imgAddToCart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icecream, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IceCream iceCream = iceCreams.get(position);
        holder.tvName.setText(iceCream.getName());
        holder.tvDescription.setText(iceCream.getDescription());
        holder.tvPrice.setText("Price: $" + iceCream.getPrice());
        holder.tvStock.setText("Stock: " + iceCream.getStock());

        String imageUrl = iceCream.getImageUrl();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_launcher_background)
                .override(300, 300)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        holder.imgIceCream.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.imgIceCream);
        holder.imgAddToCart.setOnClickListener(v -> addToCart(iceCream));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), IceCreamDetailActivity.class);
            intent.putExtra("ICE_CREAM_ID", iceCream.getIceCreamId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return iceCreams.size();
    }


    private void addToCart(IceCream iceCream) {
        if (context == null) {
            return;
        }

        // Lấy SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        // Kiểm tra userId
        if (userId == -1) {
            Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi API thêm vào giỏ hàng
        Cart cart = new Cart(userId, iceCream.getIceCreamId(), 1);
        CartApiService cartApiService = RetrofitClient.getCartApiService();
        Call<Void> call = cartApiService.addToCart(cart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    // 🔄 Cập nhật cart badge
                    if (context instanceof Fragment_homeActivity) {
                        ((Fragment_homeActivity) context).refreshCartBadge();
                    }
                } else {
                    Toast.makeText(context, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateData(List<IceCream> newIceCreams) {
        this.iceCreams.clear();
        this.iceCreams.addAll(newIceCreams);
        notifyDataSetChanged();
    }
}