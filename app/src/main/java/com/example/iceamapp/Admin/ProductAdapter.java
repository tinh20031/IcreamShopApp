package com.example.iceamapp.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.entity.IceCream;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<IceCream> productList;
    private final OnEditClickListener editClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(IceCream iceCream);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(IceCream iceCream);
    }

    public ProductAdapter(List<IceCream> productList, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        this.productList = productList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        IceCream iceCream = productList.get(position);
        holder.tvProductName.setText(iceCream.getName());
        holder.tvProductPrice.setText(String.format("Giá: %.2f", iceCream.getPrice()));
        holder.tvProductStock.setText("Kho: " + iceCream.getStock());

        holder.btnEdit.setOnClickListener(v -> editClickListener.onEditClick(iceCream));
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(iceCream));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvProductStock;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductStock = itemView.findViewById(R.id.tvProductStock);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}