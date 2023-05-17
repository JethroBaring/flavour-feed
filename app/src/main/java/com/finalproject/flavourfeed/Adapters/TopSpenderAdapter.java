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
import com.finalproject.flavourfeed.Models.TopSpenderModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class TopSpenderAdapter extends ListAdapter<TopSpenderModel, TopSpenderAdapter.TopSpenderViewHolder> {

    FirebaseFirestore db;

    public TopSpenderAdapter(@NonNull DiffUtil.ItemCallback<TopSpenderModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TopSpenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopSpenderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_spender_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopSpenderViewHolder holder, int position) {
        TopSpenderModel topSpenderModel = getItem(position);
        holder.bind(topSpenderModel);
    }

    class TopSpenderViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView displayName;
        TextView email;
        TextView total;
        public TopSpenderViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            displayName = itemView.findViewById(R.id.displayName);
            email = itemView.findViewById(R.id.email);
            total = itemView.findViewById(R.id.totalSpend);
        }

        public void bind(TopSpenderModel topSpenderModel) {
            db = FirebaseFirestore.getInstance();
            total.setText(String.format("%,d",topSpenderModel.getTotalSpend()));
            db.collection("userInformation").document(topSpenderModel.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
                Glide.with(itemView.getContext()).load(documentSnapshot.get("profileUrl")).into(profile);
                displayName.setText(documentSnapshot.getString("displayName"));
                email.setText(documentSnapshot.getString("email"));
            });
        }
    }
}
