package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.finalproject.flavourfeed.Adapters.TopSpenderAdapter;

import java.util.Objects;

public class TopSpenderModel {
    public String userId;
    public int totalSpend;

    public TopSpenderModel() {
    }

    public TopSpenderModel(String userId, int totalSpend) {
        this.userId = userId;
        this.totalSpend = totalSpend;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(int totalSpend) {
        this.totalSpend = totalSpend;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TopSpenderModel orderModel = (TopSpenderModel) obj;
        return Objects.equals(userId, orderModel.getUserId());
    }

    public static DiffUtil.ItemCallback<TopSpenderModel> itemCallback = new DiffUtil.ItemCallback<TopSpenderModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TopSpenderModel oldItem, @NonNull TopSpenderModel newItem) {
            if (oldItem == null || oldItem.getUserId() == null || newItem == null || newItem.getUserId() == null) {
                return false;
            }
            return oldItem.getUserId().equals(newItem.getUserId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TopSpenderModel oldItem, @NonNull TopSpenderModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
