package com.finalproject.flavourfeed.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.R;

import org.w3c.dom.Text;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    ProductClickInterface productClickInterface;
    List<ProductModel> products;
    Context context;

    public ProductAdapter(Context context, List<ProductModel> products, ProductClickInterface productClickInterface) {
        this.context = context;
        this.products = products;
        this.productClickInterface = productClickInterface;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = products.get(position);
        holder.bind(productModel);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productPicture;
        TextView productName;
        TextView productPrice;
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
