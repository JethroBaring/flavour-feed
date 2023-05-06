package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Objects;

public class ChatRoomModel {
    public String chatRoomId;
    public String otherUserId;

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @ServerTimestamp
    public Date lastModified;
    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, String otherUser) {
        this.chatRoomId = chatRoomId;
        this.otherUserId = otherUser;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
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
            return oldItem.equals(newItem);
        }
    };
}
