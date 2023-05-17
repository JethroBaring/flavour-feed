package com.finalproject.flavourfeed.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class ProductModel {
    public String sellerId;

    public String productId;
    public String photoUrl;
    public String name;
    public String category;
    public int price;
    public int sold;

    public ProductModel() {
    }

    public ProductModel(String sellerId, String photoUrl, String name, String category, int price) {
        this.sellerId = sellerId;
        this.photoUrl = photoUrl;
        this.name = name;
        this.category = category;
        this.price = price;
    }


    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductModel productModel = (ProductModel) obj;
        return Objects.equals(productId, productModel.productId);
    }

    public static DiffUtil.ItemCallback<ProductModel> itemCallback = new DiffUtil.ItemCallback<ProductModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ProductModel oldItem, @NonNull ProductModel newItem) {
            if(oldItem == null || oldItem.getProductId() == null || newItem == null || newItem.getProductId() == null) {
                return false;
            }
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductModel oldItem, @NonNull ProductModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}