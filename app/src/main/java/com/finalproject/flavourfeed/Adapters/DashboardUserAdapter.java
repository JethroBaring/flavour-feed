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
import com.finalproject.flavourfeed.Models.DashboardUserModel;
import com.finalproject.flavourfeed.R;

public class DashboardUserAdapter extends ListAdapter<DashboardUserModel, DashboardUserAdapter.DashboardUserViewHolder> {

    public DashboardUserAdapter(@NonNull DiffUtil.ItemCallback<DashboardUserModel> diffCallback) {
        super(diffCallback);
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

        public DashboardUserViewHolder(@NonNull View itemView) {
            super(itemView);
            profile  = itemView.findViewById(R.id.profile);
            displayName = itemView.findViewById(R.id.displayName);
        }

        public void bind(DashboardUserModel dashboardUserModel) {
            Glide.with(itemView.getContext()).load(dashboardUserModel.getProfileUrl()).into(profile);
            displayName.setText(dashboardUserModel.getDisplayName());
        }
    }
}
