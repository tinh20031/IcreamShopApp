package com.example.iceamapp.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iceamapp.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<String> messages;

    public ChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);

        if (message.startsWith("Bạn: ")) {
            // Tin nhắn của người dùng (căn phải, màu xanh)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.sent_message_background));
            holder.chatMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.container.setGravity(Gravity.END); // Căn phải
        } else {
            // Tin nhắn của bot/admin (căn trái, màu xám)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.message_background));
            holder.chatMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.container.setGravity(Gravity.START); // Căn trái
        }

        holder.chatMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatMessage;
        CardView cardView;
        LinearLayout container;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            cardView = itemView.findViewById(R.id.cardViewMessage);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}