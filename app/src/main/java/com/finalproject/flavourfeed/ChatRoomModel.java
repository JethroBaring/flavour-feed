package com.finalproject.flavourfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class ChatRoomModel {
    public String chatRoomId;
    public String userOneId;
    public String userTwoId;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, String userOneId, String userTwoId) {
        this.chatRoomId = chatRoomId;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(String userOneId) {
        this.userOneId = userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(String userTwoId) {
        this.userTwoId = userTwoId;
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
