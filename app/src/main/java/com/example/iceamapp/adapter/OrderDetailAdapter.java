package com.example.iceamapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.entity.OrderDetailDTO;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetailDTO> orderDetailList;

    public OrderDetailAdapter(List<OrderDetailDTO> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailDTO detail = orderDetailList.get(position);

        // Gán thông tin văn bản
        holder.txtIceCreamName.setText(detail.getIceCreamName());
        holder.txtQuantity.setText("Số lượng: " + detail.getQuantity());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.## VND");
        holder.txtPrice.setText("Giá: " + decimalFormat.format(detail.getPrice()));

        // Xử lý hình ảnh từ URL hoặc Base64
        String imageUrl = detail.getImageUrl();
        if (imageUrl != null) {
            if (imageUrl.startsWith("data:image/jpeg;base64,")) {
                // Xử lý hình ảnh Base64
                try {
                    String base64Image = imageUrl.split(",")[1];
                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    holder.imgIceCream.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Sử dụng Picasso để tải ảnh từ URL
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.logo) // Ảnh hiển thị khi đang tải
                        .error(R.drawable.ic_launcher_background) // Ảnh hiển thị nếu lỗi
                        .into(holder.imgIceCream);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList != null ? orderDetailList.size() : 0;
    }

    // ViewHolder class
    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIceCream;
        TextView txtIceCreamName;
        TextView txtQuantity;
        TextView txtPrice;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIceCream = itemView.findViewById(R.id.imgIceCream);
            txtIceCreamName = itemView.findViewById(R.id.txtIceCreamName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}
