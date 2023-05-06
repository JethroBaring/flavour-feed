package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.ProductModel;
import com.finalproject.flavourfeed.R;



public class ProductAdapter extends ListAdapter<ProductModel, ProductAdapter.ProductViewHolder> {

    ProductClickInterface productClickInterface;
    public ProductAdapter(@NonNull DiffUtil.ItemCallback<ProductModel> diffCallback, ProductClickInterface productClickInterface) {
        super(diffCallback);
        this.productClickInterface = productClickInterface;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = getItem(position);
        holder.bind(productModel);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productPicture;
        TextView productName;
        Button productPrice;
        RelativeLayout productCard;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productPicture = itemView.findViewById(R.id.productPicture);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCard = itemView.findViewById(R.id.productCard);
        }

        public void bind(ProductModel productModel) {
            Glide.with(itemView.getContext()).load(productModel.getPhotoUrl()).into(productPicture);
            productName.setText(productModel.getName());
            productPrice.setText(Integer.toString(productModel.getPrice()));
            productCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productClickInterface.onProductClick(productModel.getSellerId(), productModel.getProductId());
                }
            });
        }
    }

    public interface ProductClickInterface {
        public void onProductClick(String sellerId, String productId);
    }
}
