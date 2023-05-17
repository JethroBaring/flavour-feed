package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class TransactionModel {
    public String transactionId;
    public String userId;
    public int total;

    public TransactionModel(){}
    public TransactionModel(String transactionId, String userId, int total) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.total = total;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel transactionModel = (TransactionModel) o;
        return Objects.equals(transactionId, transactionModel.transactionId);
    }

    public static DiffUtil.ItemCallback<TransactionModel> itemCallback = new DiffUtil.ItemCallback<TransactionModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TransactionModel oldItem, @NonNull TransactionModel newItem) {
            if (oldItem.getTransactionId() == null || newItem.getTransactionId() == null) {
                return false;
            }
            return oldItem.getTransactionId().equals(newItem.getTransactionId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TransactionModel oldItem, @NonNull TransactionModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
