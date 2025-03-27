package com.example.iceamapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import com.example.iceamapp.R;
import com.example.iceamapp.entity.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder> {
    private List<ChatMessage> messageList = new ArrayList<>();
    private String currentUser;

    public ChatMessageAdapter(String currentUser) {
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.textUser.setText(message.getUser() + ":");
        holder.textMessage.setText(message.getText());

        // Logic căn trái/phải và đổi màu nền
        if (message.getUser() != null && message.getUser().equals(currentUser)) {
            // Tin nhắn của người dùng (căn phải, màu xanh)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.sent_message_background));
            holder.textUser.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.textMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.container.setGravity(Gravity.END); // Căn phải
        } else {
            // Tin nhắn của admin (căn trái, màu xám)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.message_background));
            holder.textUser.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.textMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            holder.container.setGravity(Gravity.START); // Căn trái
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessageList(List<ChatMessage> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        this.messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textUser, textMessage;
        CardView cardView;
        LinearLayout container;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textUser = itemView.findViewById(R.id.textUser);
            textMessage = itemView.findViewById(R.id.textMessage);
            cardView = itemView.findViewById(R.id.cardViewMessage);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}