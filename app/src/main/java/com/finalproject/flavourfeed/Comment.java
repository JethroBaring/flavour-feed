package com.finalproject.flavourfeed;

public class Comment {
    public String displayName;
    public String email;
    public String profilePicture;
    public String comment;

    public Comment() {
    }

    public Comment(String displayName, String email, String profilePicture, String comment) {
        this.displayName = displayName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.comment = comment;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
