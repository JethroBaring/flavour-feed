package com.finalproject.flavourfeed.Entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CommentEntity {
    public String commentId;
    public String comment;
    public String postId;
    public String userId;

    public CommentEntity(){}
    public CommentEntity(String commentId, String comment, String postId, String userId) {
        this.commentId = commentId;
        this.comment = comment;
        this.postId = postId;
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity comment1 = (CommentEntity) o;
        return Objects.equals(commentId, comment1.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static DiffUtil.ItemCallback<CommentEntity> itemCallback = new DiffUtil.ItemCallback<CommentEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommentEntity oldItem, @NonNull CommentEntity newItem) {
            if(oldItem == null || oldItem.getCommentId() == null || newItem == null || newItem.getCommentId() == null) {
                return false;
            }
            return oldItem.getCommentId().equals(newItem.getCommentId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentEntity oldItem, @NonNull CommentEntity newItem) {
            return false;
        }
    };
}
