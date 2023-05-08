package com.finalproject.flavourfeed.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CartItemModel implements Parcelable {
    public String sellerId;
    public String cartItemId;
    public String productId;
    public int quantity;
    public int price;


    public CartItemModel() {
    }

    public CartItemModel(String sellerId, String cartItemId, String productId, int quantity, int price) {
        this.sellerId = sellerId;
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItemModel carItemModel = (CartItemModel) obj;
        return Objects.equals(cartItemId, carItemModel.getCartItemId());
    }

    public static DiffUtil.ItemCallback<CartItemModel> itemCallback = new DiffUtil.ItemCallback<CartItemModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItemModel oldItem, @NonNull CartItemModel newItem) {
            if(oldItem.getCartItemId() == null || newItem.getCartItemId() == null) {
                return false;
            }
            return oldItem.getCartItemId().equals(newItem.getCartItemId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItemModel oldItem, @NonNull CartItemModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel in) {
            return new CartItemModel(in);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };

    protected CartItemModel(Parcel in) {
        sellerId = in.readString();
        cartItemId = in.readString();
        productId = in.readString();
        quantity = in.readInt();
        price = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(sellerId);
        dest.writeString(cartItemId);
        dest.writeString(productId);
        dest.writeInt(quantity);
        dest.writeInt(price);
    }
}
