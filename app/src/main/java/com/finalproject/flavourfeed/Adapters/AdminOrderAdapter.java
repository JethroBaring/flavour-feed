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

import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminOrderAdapter extends ListAdapter<OrderModel, AdminOrderAdapter.AdminOrderViewHolder> {
    FirebaseFirestore db;
    int isPending;
    public AdminOrderAdapter(@NonNull DiffUtil.ItemCallback<OrderModel> diffCallback, int isPending) {
        super(diffCallback);
        this.isPending = isPending;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == OrderModel.IN_PROGRESS) {
            return new AdminOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_in_progress_card, parent, false));
        } else if(viewType == OrderModel.PENDING) {
            return new AdminOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pending_card, parent, false));
        } else {
            return new AdminOrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_completed_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        OrderModel orderModel = getItem(position);
        holder.bind(orderModel);
    }

    @Override
    public int getItemViewType(int position) {
        OrderModel orderModel = getItem(position);
        return  orderModel.getStatus();
    }

    class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        Button processOrder;
        Button deliveredOrder;
        TextView orderId;
        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            if (isPending == 1) {
                processOrder = itemView.findViewById(R.id.processOrder);
            } else if(isPending == 2){
                deliveredOrder = itemView.findViewById(R.id.orderDelivered);
            }
            orderId = itemView.findViewById(R.id.orderId);
        }

        public void bind(OrderModel orderModel) {
            db = FirebaseFirestore.getInstance();
            if(isPending == 1) {
                processOrder.setOnClickListener( view -> {
                    db.collection("orderInformation").document(orderModel.getOrderId()).update("status",OrderModel.IN_PROGRESS);
                });
            } else if(isPending == 2){
                deliveredOrder.setOnClickListener(view -> {
                    db.collection("orderInformation").document(orderModel.getOrderId()).update("delivered", true);
                });
            }
            orderId.setText(orderModel.getOrderId());
        }
    }
}
