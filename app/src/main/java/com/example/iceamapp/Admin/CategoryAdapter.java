package com.example.iceamapp.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iceamapp.R;
import com.example.iceamapp.entity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private final OnEditClickListener editClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(Category category);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        this.categoryList = categoryList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category1, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());


        Glide.with(holder.itemView.getContext())
                .load(category.getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgCategoryImage);

        holder.btnEdit.setOnClickListener(v -> editClickListener.onEditClick(category));
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView imgCategoryImage;
        Button btnEdit, btnDelete;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            imgCategoryImage = itemView.findViewById(R.id.imgCategoryImage); // Đổi từ TextView sang ImageView
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
