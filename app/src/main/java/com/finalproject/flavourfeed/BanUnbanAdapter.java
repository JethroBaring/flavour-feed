package com.finalproject.flavourfeed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Models.DashboardUserModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class BanUnbanAdapter extends ListAdapter<DashboardUserModel, BanUnbanAdapter.BanUnbanViewHolder> {
    boolean ban;
    BanUnbanInterface banUnbanInterface;
    FirebaseFirestore db;

    public BanUnbanAdapter(@NonNull DiffUtil.ItemCallback<DashboardUserModel> diffCallback, BanUnbanInterface banUnbanInterface, boolean ban) {
        super(diffCallback);
        this.banUnbanInterface = banUnbanInterface;
        this.ban = ban;
    }

    @NonNull
    @Override
    public BanUnbanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BanUnbanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BanUnbanViewHolder holder, int position) {
        DashboardUserModel dashboardUserModel = getItem(position);
        holder.bind(dashboardUserModel);
    }

    class BanUnbanViewHolder extends RecyclerView.ViewHolder {
        TextView displayName;
        TextView email;
        ImageView profile;
        ImageView viewInformation;
        ImageView banUser;

        public BanUnbanViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.displayName);
            email = itemView.findViewById(R.id.email);
            profile = itemView.findViewById(R.id.profile);
            viewInformation = itemView.findViewById(R.id.viewInformation);
            banUser = itemView.findViewById(R.id.banUser);
            if (!ban) {
                banUser.setImageResource(R.drawable.newunbanicon);
            }
        }

        public void bind(DashboardUserModel dashboardUserModel) {
            db = FirebaseFirestore.getInstance();
            banUser.setOnClickListener(view -> {
                db.collection("userInformation").document(dashboardUserModel.getUserId()).update("ban", ban);
            });
        }
    }

    public interface BanUnbanInterface {

        public void OnViewInformationClick();

    }
}
