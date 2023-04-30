package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class ChatRoomModel {
    public String chatRoomId;
    public String otherUser;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, String otherUser) {
        this.chatRoomId = chatRoomId;
        this.otherUser = otherUser;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChatRoomModel chatRoomModel = (ChatRoomModel) obj;
        return Objects.equals(chatRoomId, chatRoomModel.chatRoomId);
    }

    public static DiffUtil.ItemCallback<ChatRoomModel> itemCallback = new DiffUtil.ItemCallback<ChatRoomModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatRoomModel oldItem, @NonNull ChatRoomModel newItem) {
            if(oldItem == null || oldItem.getChatRoomId() == null || newItem == null || newItem.getChatRoomId() == null) {
                return false;
            }
            return oldItem.getChatRoomId().equals(newItem.getChatRoomId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatRoomModel oldItem, @NonNull ChatRoomModel newItem) {
            return false;
        }
    };
}
