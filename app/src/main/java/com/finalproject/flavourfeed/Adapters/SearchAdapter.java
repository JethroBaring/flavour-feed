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
import com.finalproject.flavourfeed.Entity.NotificationEntity;
import com.finalproject.flavourfeed.Entity.ResultEntity;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    FirebaseFirestore db;
    Context context;
    List<ResultEntity> results;

    public SearchAdapter(Context context, List<ResultEntity> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_card, parent, false);
        return new SearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = db.collection("userInformation").document(results.get(position).getUserId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        holder.searchDisplayName.setText(documentSnapshot.getString("displayName"));
                        holder.searchEmail.setText(documentSnapshot.getString("email"));
                        Glide.with(holder.itemView.getContext()).load(documentSnapshot.getString("profileUrl")).into(holder.searchProfile);
                    }
                }
            }
        });
        if (user.getUid().equals(results.get(position).getUserId()))
            holder.sendRequest.setVisibility(View.INVISIBLE);
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.sendRequest.setImageResource(R.drawable.pendingicon);
                DocumentReference currentUserRef = db.collection("userInformation").document(user.getUid());
                Map<String, Object> newFriendRequest = new HashMap<>();
                newFriendRequest.put("userId", user.getUid());
                newFriendRequest.put("profileUrl", user.getPhotoUrl());
                newFriendRequest.put("displayName", user.getDisplayName());
                documentReference.collection("friendRequest").add(newFriendRequest);

                Map<String, Object> newNotification = new HashMap<>();
                newNotification.put("toUserId", results.get(position).getUserId());
                newNotification.put("fromUserId", user.getUid());
                newNotification.put("notificationType", NotificationEntity.FRIEND_REQUEST_NOTIFICATION);
                newNotification.put("postId", null);
                newNotification.put("timestamp", FieldValue.serverTimestamp());
                db.collection("notificationInformation").add(newNotification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String notificationId = documentReference.getId();
                        db.collection("notificationInformation").document(notificationId).update("notificationId", notificationId);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView searchProfile;
        ImageView sendRequest;
        TextView searchDisplayName;
        TextView searchEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            searchProfile = itemView.findViewById(R.id.searchProfile);
            searchDisplayName = itemView.findViewById(R.id.searchDisplayName);
            searchEmail = itemView.findViewById(R.id.searchEmail);
            sendRequest = itemView.findViewById(R.id.sendRequest);
        }
    }
}
