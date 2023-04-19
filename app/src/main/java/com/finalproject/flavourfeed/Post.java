package com.finalproject.flavourfeed;



public class Post {
    public String photoUrl;
    public String caption;
    public String userId;
    public String postId;

    public Post() {
    }

    public Post(String photoUrl, String caption, String postId, String userId) {
        this.photoUrl = photoUrl;
        this.caption = caption;
        this.userId = userId;
        this.postId = postId;
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
}
