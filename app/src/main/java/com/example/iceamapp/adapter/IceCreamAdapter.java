package com.example.iceamapp.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.entity.IceCream;

import java.util.List;

public class IceCreamAdapter extends RecyclerView.Adapter<IceCreamAdapter.ViewHolder> {
    private List<IceCream> iceCreams;

    public IceCreamAdapter(List<IceCream> iceCreams) {
        this.iceCreams = iceCreams;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvDescription;
        public TextView tvPrice;
        public TextView tvStock;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_icecream, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IceCream iceCream = iceCreams.get(position);
        holder.tvName.setText(iceCream.getName());
        holder.tvDescription.setText(iceCream.getDescription());
        holder.tvPrice.setText("Price: $" + iceCream.getPrice());
        holder.tvStock.setText("Stock: " + iceCream.getStock());
    }

    @Override
    public int getItemCount() {
        return iceCreams.size();
    }
}