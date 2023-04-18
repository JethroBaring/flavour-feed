package com.finalproject.flavourfeed;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;

public class Post {
    public String displayName;
    public String email;
    public String profileUrl;
    public String photoUrl;
    public String caption;
    public Post() {
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Post(String displayName, String email, String profileUrl, String photoUrl, String caption) {
        this.displayName = displayName;
        this.email = email;
        this.profileUrl = profileUrl;
        this.photoUrl = photoUrl;
        this.caption = caption;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
