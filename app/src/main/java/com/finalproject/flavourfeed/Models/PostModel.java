package com.finalproject.flavourfeed.Models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class PostModel {
    public String photoUrl;
    public String caption;
    public String userId;
    public String postId;
    public int likes;
    public int comments;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public PostModel() {
    }

    public PostModel(String photoUrl, String caption, String postId, String userId, int comments) {
        this.photoUrl = photoUrl;
        this.caption = caption;
        this.userId = userId;
        this.postId = postId;
        this.comments = comments;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }



    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostModel postModel = (PostModel) o;
        return postId.equals(postModel.getPostId());
    }

    public static DiffUtil.ItemCallback<PostModel> itemCallback = new DiffUtil.ItemCallback<PostModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull PostModel oldItem, @NonNull PostModel newItem) {
            if(oldItem.getPostId() == null || newItem.getPostId() == null) {
                return false;
            }
            return oldItem.getPostId().equals(newItem.getPostId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PostModel oldItem, @NonNull PostModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
