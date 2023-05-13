package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class DashboardUserModel {
    public String userId;
    public String profileUrl;
    public String displayName;

    public DashboardUserModel() {
    }

    public DashboardUserModel(String userId, String photoUrl, String displayName) {
        this.userId = userId;
        this.profileUrl = photoUrl;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DashboardUserModel dashboardUserModel = (DashboardUserModel) obj;
        return Objects.equals(userId, dashboardUserModel.getUserId());
    }

    public static DiffUtil.ItemCallback<DashboardUserModel> itemCallback = new DiffUtil.ItemCallback<DashboardUserModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DashboardUserModel oldItem, @NonNull DashboardUserModel newItem) {
            if(oldItem.getUserId() == null || newItem.getUserId() == null) {
                return false;
            }
            return oldItem.getUserId().equals(newItem.getUserId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DashboardUserModel oldItem, @NonNull DashboardUserModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
