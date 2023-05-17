package com.finalproject.flavourfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.DashboardUserModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardUserAdapter extends ListAdapter<DashboardUserModel, DashboardUserAdapter.DashboardUserViewHolder> {
    boolean followers;
    FirebaseFirestore db;
    public DashboardUserAdapter(@NonNull DiffUtil.ItemCallback<DashboardUserModel> diffCallback, boolean followers) {
        super(diffCallback);
        this.followers = followers;
    }

    @NonNull
    @Override
    public DashboardUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardUserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_user_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardUserViewHolder holder, int position) {
        DashboardUserModel dashboardUserModel = getItem(position);
        holder.bind(dashboardUserModel);
    }

    class DashboardUserViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView displayName;
        LinearLayout followersContainer;
        TextView follow;
        TextView email;

        public DashboardUserViewHolder(@NonNull View itemView) {
            super(itemView);
            profile  = itemView.findViewById(R.id.profile);
            displayName = itemView.findViewById(R.id.displayName);
            followersContainer = itemView.findViewById(R.id.followwersContainer);
            email = itemView.findViewById(R.id.email);
            if(followers) {
                follow = itemView.findViewById(R.id.followers);
            } else {
                followersContainer.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(DashboardUserModel dashboardUserModel) {
            Glide.with(itemView.getContext()).load(dashboardUserModel.getProfileUrl()).into(profile);
            displayName.setText(dashboardUserModel.getDisplayName());
            db = FirebaseFirestore.getInstance();

            if(followers) {
                db.collection("userInformation").document(dashboardUserModel.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
                   follow.setText(String.format("%,d",documentSnapshot.getLong("followers").intValue()));
                });
            }
            db.collection("userInformation").document(dashboardUserModel.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
                email.setText(documentSnapshot.getString("email"));
            });
        }
    }
}
