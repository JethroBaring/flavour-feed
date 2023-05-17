package com.finalproject.flavourfeed.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.CartItemModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ConfirmationAdapter extends RecyclerView.Adapter<ConfirmationAdapter.ConfirmationViewholder> {
    Context context;
    ArrayList<CartItemModel> cartItemModels;
    FirebaseFirestore db;

    public ConfirmationAdapter(Context context, ArrayList<CartItemModel> cartItemModels) {
        this.context = context;
        this.cartItemModels = cartItemModels;
    }


    @NonNull
    @Override
    public ConfirmationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirmation_card2, parent, false);
        return new ConfirmationAdapter.ConfirmationViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmationViewholder holder, int position) {
        holder.productQuantity.setText(Integer.toString(cartItemModels.get(position).getQuantity()));
        holder.productTotal.setText(Integer.toString(cartItemModels.get(position).getPrice() * cartItemModels.get(position).getQuantity()));
        db = FirebaseFirestore.getInstance();
        db.collection("allProducts").document(cartItemModels.get(position).getProductId()).get().addOnSuccessListener(documentSnapshot -> {
            Glide.with(holder.itemView.getContext()).load(documentSnapshot.getString("photoUrl")).into(holder.productPicture);
            holder.productName.setText(documentSnapshot.getString("name"));
            holder.productPrice.setText(Integer.toString(documentSnapshot.getLong("price").intValue()));
        });

    }

    @Override
    public int getItemCount() {
        return cartItemModels.size();
    }

    public static class ConfirmationViewholder extends RecyclerView.ViewHolder {
        ImageView productPicture;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        TextView productTotal;

        public ConfirmationViewholder(@NonNull View itemView) {
            super(itemView);
            productPicture = itemView.findViewById(R.id.productPicture);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productTotal = itemView.findViewById(R.id.productTotal);
        }
    }

}
