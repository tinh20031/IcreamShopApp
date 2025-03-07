package com.example.iceamapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.entity.Cart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartList;

    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        // Hiển thị thông tin cơ bản
        holder.txtName.setText(cart.getIceCreamName());
        holder.txtPrice.setText(String.format("%.2f VND", cart.getPrice()));
        holder.txtQuantity.setText("Số lượng: " + cart.getQuantity());

        // Xử lý hình ảnh
        String imageUrl = cart.getImage();
        Log.d("CartAdapter", "Image data: " + imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("data:image")) {
                // Giải mã base64
                try {
                    String base64Image = imageUrl.split(",")[1]; // Lấy phần dữ liệu sau "data:image/jpeg;base64,"
                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    holder.imgProduct.setImageBitmap(bitmap);
                    Log.d("CartAdapter", "Base64 image decoded successfully for " + cart.getIceCreamName());
                } catch (Exception e) {
                    Log.e("CartAdapter", "Error decoding base64 image: " + e.getMessage());

                }
            } else {
                // Nếu là URL, dùng Picasso
                Picasso.get()
                        .load(imageUrl)

                        .into(holder.imgProduct, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("CartAdapter", "Image loaded successfully: " + imageUrl);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("CartAdapter", "Failed to load image: " + imageUrl + " - " + e.getMessage());
                            }
                        });
            }
        } else {
            Log.w("CartAdapter", "Image URL is null or empty for " + cart.getIceCreamName());

        }
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }
    }
}