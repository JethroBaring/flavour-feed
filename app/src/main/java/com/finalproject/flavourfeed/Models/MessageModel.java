package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class MessageModel {
    public String chatId;
    public String senderId;
    public String recieverId;
    public String chatMessage;

    public MessageModel() {}

    public MessageModel(String chatId, String senderId, String recieverId, String chatMessage) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.chatMessage = chatMessage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
    public static DiffUtil.ItemCallback<MessageModel> itemCallback = new DiffUtil.ItemCallback<MessageModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MessageModel oldItem, @NonNull MessageModel newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MessageModel oldItem, @NonNull MessageModel newItem) {
            return false;
        }
    };
}
