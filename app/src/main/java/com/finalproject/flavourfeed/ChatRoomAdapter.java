package com.finalproject.flavourfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class ChatRoomAdapter extends ListAdapter<ChatRoomModel, ChatRoomAdapter.ChatRoomViewHolder> {

    public ChatRoomAdapter(@NonNull DiffUtil.ItemCallback<ChatRoomModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomAdapter.ChatRoomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoomModel chatRoomModel = getItem(position);
        holder.bind(chatRoomModel);
    }


    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(ChatRoomModel chatRoomModel) {

        }
    }
}
