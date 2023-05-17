package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Models.TransactionModel;
import com.finalproject.flavourfeed.R;

public class TransactionAdapter extends ListAdapter<TransactionModel, TransactionAdapter.TransactionViewHolder> {
    public TransactionAdapter(@NonNull DiffUtil.ItemCallback<TransactionModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel transactionModel = getItem(position);
        holder.bind(transactionModel);
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView total;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.total);
        }

        public void bind(TransactionModel transactionModel) {
            total.setText(String.format("%,d",transactionModel.getTotal()));
        }
    }
}
