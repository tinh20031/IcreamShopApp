package com.example.iceamapp.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.iceamapp.R;

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
            holder.chatMessage.setBackgroundResource(R.drawable.chat_bubble_user);
            holder.chatMessage.setGravity(Gravity.END);
        } else {
            holder.chatMessage.setBackgroundResource(R.drawable.chat_bubble_bot);
            holder.chatMessage.setGravity(Gravity.START);
        }

        holder.chatMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessage = itemView.findViewById(R.id.chatMessage);
        }
    }
}
