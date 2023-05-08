package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Objects;

public class OrderModel {
    public final static int PENDING = 1;
    public final static int IN_PROGRESS = 2;
    public final static int ON_DELIVERY = 3;
    public final static int COMPLETED = 4;
    public String orderId;
    public int status;

    public String buyerId;
    public int orderTotal;
    public ArrayList<CartItemModel> orderItems;

    public boolean delivered;

    public OrderModel() {
    }

    public OrderModel(String orderId, int status, String buyerId, int orderTotal, ArrayList<CartItemModel> orderItems, boolean delivered) {
        this.orderId = orderId;
        this.status = status;
        this.buyerId = buyerId;
        this.orderTotal = orderTotal;
        this.orderItems = orderItems;
        this.delivered = false;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public ArrayList<CartItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<CartItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderModel orderModel = (OrderModel) obj;
        return Objects.equals(orderId, orderModel.getOrderId());
    }

    public static DiffUtil.ItemCallback<OrderModel> itemCallback = new DiffUtil.ItemCallback<OrderModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderModel oldItem, @NonNull OrderModel newItem) {
            if(oldItem == null || oldItem.getOrderId() == null || newItem == null || newItem.getOrderId() == null) {
                return false;
            }
            return oldItem.getOrderId().equals(newItem.getOrderId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderModel oldItem, @NonNull OrderModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
