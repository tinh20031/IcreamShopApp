package com.example.iceamapp.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.iceamapp.R;
import com.example.iceamapp.entity.IceCream;

import java.util.List;

public class IceCreamAdapter extends RecyclerView.Adapter<IceCreamAdapter.ViewHolder> {
    private final List<IceCream> iceCreams;

    public IceCreamAdapter(List<IceCream> iceCreams) {
        this.iceCreams = iceCreams;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDescription, tvPrice, tvStock;
        public ImageView imgIceCream;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            imgIceCream = itemView.findViewById(R.id.imgIceCream);
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

        final String imageUrl = iceCream.getImageUrl();
        Log.d("Glide", "📌 Đang tải ảnh từ URL: " + imageUrl);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_launcher_background)
                .override(300, 300) // Thử thay đổi kích thước ảnh
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "❌ Load ảnh thất bại: " + imageUrl, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        Log.d("GlideSuccess", "✅ Load ảnh thành công: " + imageUrl);
                        holder.imgIceCream.setImageDrawable(resource); // Đảm bảo ảnh được hiển thị
                        return false;
                    }
                })
                .into(holder.imgIceCream);
    }

    @Override
    public int getItemCount() {
        return iceCreams.size();
    }
}
