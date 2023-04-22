package com.finalproject.flavourfeed.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class NotificationEntity {

    public String notificationId;
    public String toUserId;
    public String fromUserId;
    public int notificationType;
    public String postId;

    public static final int FRIEND_REQUEST_NOTIFICATION = 1;
    public static final int COMMENT_NOTIFICATION = 2;
    public static final int LIKE_NOTIFICATION = 3;

    public NotificationEntity() {
    }

    public NotificationEntity(String toUserId, String fromUserId, int notificationType, String postId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.notificationType = notificationType;
        this.postId = postId;
    }

    public NotificationEntity(String toUserId, String fromUserId, int notificationType) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.notificationType = notificationType;
        this.postId = null;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NotificationEntity notification = (NotificationEntity) obj;
        return Objects.equals(notificationId, notification.getNotificationId());
    }

    public static DiffUtil.ItemCallback<NotificationEntity> itemCallback = new DiffUtil.ItemCallback<NotificationEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull NotificationEntity oldItem, @NonNull NotificationEntity newItem) {
            return oldItem.getNotificationId().equals(newItem.notificationId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotificationEntity oldItem, @NonNull NotificationEntity newItem) {
            return oldItem.getNotificationId().equals(newItem.notificationId);
        }
    };
}
