package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Models.MessageModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageAdapter extends ListAdapter<MessageModel, MessageAdapter.ChatViewHolder> {
    final static int MESSAGE_RIGHT = 1;
    final static int MESSAGE_LEFT = 2;

    public MessageAdapter(@NonNull DiffUtil.ItemCallback diffCallback) {
        super(diffCallback);
    }

    FirebaseUser user;

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_RIGHT) {
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right, parent, false));
        }
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        MessageModel messageModel = getItem(position);
        holder.bind(messageModel);
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (getItem(position).getSenderId().equals(user.getUid())) {
            return MESSAGE_RIGHT;
        }
        return MESSAGE_LEFT;
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView txtMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }

        public void bind(MessageModel message) {
            txtMessage.setText(message.getMessage());
        }
    }
}
