package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.R;

public class TopProductsAdapter extends ListAdapter<ProductModel, TopProductsAdapter.TopProductsViewHolder> {
    public TopProductsAdapter(@NonNull DiffUtil.ItemCallback<ProductModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TopProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopProductsViewHolder holder, int position) {
        ProductModel productModel = getItem(position);
        holder.bind(productModel);
    }

    class TopProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView productPicture;
        TextView productName;
        TextView totalSold;
        public TopProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productPicture = itemView.findViewById(R.id.productPicture);
            productName = itemView.findViewById(R.id.productName);
            totalSold = itemView.findViewById(R.id.totalSold);
        }

        public void bind(ProductModel productModel) {
            Glide.with(itemView.getContext()).load(productModel.getPhotoUrl()).into(productPicture);
            productName.setText(productModel.getName());
            totalSold.setText(Integer.toString(productModel.getSold()));
        }
    }
}
