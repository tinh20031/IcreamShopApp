package com.example.iceamapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;
import com.example.iceamapp.entity.OrderDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderDTO> orderList;

    public OrderAdapter(List<OrderDTO> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDTO order = orderList.get(position);

        holder.txtOrderId.setText("Mã đơn hàng: #" + order.getOrderId());
        holder.txtStatus.setText("Trạng thái: " + order.getStatus());

        // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.txtOrderDate.setText("Ngày đặt: " + order.getOrderDate());

        // Định dạng tổng tiền
        DecimalFormat decimalFormat = new DecimalFormat("#,###.## VND");
        holder.txtTotalPrice.setText("Tổng tiền: " + decimalFormat.format(order.getTotalPrice()));

        // Hiển thị chi tiết đơn hàng
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(order.getOrderDetails());
        holder.recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewOrderDetails.setAdapter(detailAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtStatus, txtTotalPrice;
        RecyclerView recyclerViewOrderDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            recyclerViewOrderDetails = itemView.findViewById(R.id.recyclerViewOrderDetails);
        }
    }
}