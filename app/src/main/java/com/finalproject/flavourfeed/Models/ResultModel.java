package com.finalproject.flavourfeed.Models;

public class ResultModel {
    public String userId;

    public ResultModel(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
