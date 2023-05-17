package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Models.CartItemModel;
import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOrderAdapter extends ListAdapter<OrderModel, UserOrderAdapter.UserPendingViewHolder> {
    boolean hasButton;
    FirebaseFirestore db;

    public UserOrderAdapter(@NonNull DiffUtil.ItemCallback diffCallback, boolean hasButton) {
        super(diffCallback);
        this.hasButton = hasButton;
    }

    @NonNull
    @Override
    public UserPendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == OrderModel.IN_PROGRESS) {
            return new UserPendingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_progress_card2, parent, false));
        }
        return new UserPendingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_card2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserPendingViewHolder holder, int position) {
        OrderModel orderModel = getItem(position);
        holder.bind(orderModel);
    }

    @Override
    public int getItemViewType(int position) {
        OrderModel orderModel = getItem(position);
        return orderModel.getStatus();
    }

    public class UserPendingViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        Button btnReceived;

        public UserPendingViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            if (hasButton) {
                btnReceived = itemView.findViewById(R.id.btnReceived);
            }
        }

        public void bind(OrderModel orderModel) {
            orderId.setText(orderModel.getOrderId());
            if (hasButton && orderModel.isDelivered()) {
                db = FirebaseFirestore.getInstance();
                btnReceived.setEnabled(true);
                btnReceived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("orderInformation").document(orderModel.getOrderId()).update("status", OrderModel.COMPLETED);
                        Map<String, Object> newTransaction = new HashMap<>();
                        newTransaction.put("transactionId", orderModel.getOrderId());
                        newTransaction.put("userId", orderModel.getBuyerId());
                        newTransaction.put("total", orderModel.getOrderTotal());
                        newTransaction.put("timestamp", FieldValue.serverTimestamp());
                        db.collection("allTransactions").document(orderModel.getOrderId()).set(newTransaction);
                        Map<String, Object> newEearnings = new HashMap<>();
                        newEearnings.put("earning", orderModel.getOrderTotal());
                        newEearnings.put("timestamp", FieldValue.serverTimestamp());
                        db.collection("earnings").add(newEearnings);
                        db.collection("userInformation").document(orderModel.getBuyerId()).get().addOnSuccessListener(documentSnapshot -> {
                            int newSpend = documentSnapshot.getLong("totalSpend").intValue();
                            db.collection("userInformation").document(orderModel.getBuyerId()).update("totalSpend",newSpend+orderModel.getOrderTotal());
                        });
                        ArrayList<CartItemModel> orderItems = orderModel.getOrderItems();
                        for(CartItemModel c : orderItems) {
                            db.collection("allProducts").document(c.getProductId()).get().addOnSuccessListener(documentSnapshot -> {
                               int newS = documentSnapshot.getLong("sold").intValue();
                               db.collection("allProducts").document(c.getProductId()).update("sold",newS+1);
                            });
                        }
                    }
                });
            }
        }
    }
}
