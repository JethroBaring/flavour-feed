package com.finalproject.flavourfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class MyProductAdapter extends ListAdapter<ProductModel, MyProductAdapter.MyProductViewHolder> {

    public MyProductAdapter(@NonNull DiffUtil.ItemCallback<ProductModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        ProductModel productModel = getItem(position);
        holder.bind(productModel);
    }

    class MyProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productPicture;
        TextView productName;
        Button productPrice;
        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productPicture = itemView.findViewById(R.id.productPicture);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }

        public void bind(ProductModel productModel) {
            Glide.with(itemView.getContext()).load(productModel.getPhotoUrl()).into(productPicture);
            productName.setText(productModel.getName());
            productPrice.setText(Integer.toString(productModel.getPrice()));
        }
    }
}
