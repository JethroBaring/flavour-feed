package com.finalproject.flavourfeed;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Comment {
    public String commentId;
    public String comment;
    public String postId;
    public String userId;

    public Comment(){}
    public Comment(String commentId, String comment, String postId, String userId) {
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
        Comment comment1 = (Comment) o;
        return Objects.equals(commentId, comment1.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
