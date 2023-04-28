package com.finalproject.flavourfeed.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Firebase.FirebaseOperations;
import com.finalproject.flavourfeed.Models.NotificationModel;
import com.finalproject.flavourfeed.Models.ResultModel;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Fragments.ViewUserProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    FirebaseFirestore db;
    Context context;
    List<ResultModel> results;

    SearchResultClickInterface searchResultClickInterface;

    ViewUserProfileFragment viewUserProfileFragment = new ViewUserProfileFragment();

    public SearchAdapter(Context context, List<ResultModel> results, SearchResultClickInterface searchResultClickInterface) {
        this.context = context;
        this.results = results;
        this.searchResultClickInterface = searchResultClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_card, parent, false);
        return new SearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = db.collection("userInformation").document(results.get(position).getUserId());
        String receiverName;
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
        //check for sent friend request and friends in database
        db.collection("userInformation").document(user.getUid()).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserId = document.getString("userId");
                        if (friendUserId.equals(results.get(position).getUserId())) {
                            // Current user and this result object have a friend relationship
                            holder.sendRequest.setImageResource(R.drawable.checkicon);
                            holder.sendRequest.setEnabled(false);
                        }
                    }
                }
            }
        });

        db.collection("userInformation").document(user.getUid()).collection("sentFriendRequest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserId = document.getString("userId");
                        if (friendUserId.equals(results.get(position).getUserId())) {
                            // Current user and this result object have a friend relationship
                            holder.sendRequest.setImageResource(R.drawable.pendingicon);
                            holder.sendRequest.setEnabled(false);
                        }
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
                //add sent friend request to current user
                FirebaseOperations.addFriendRequest(user.getUid(), results.get(position).getUserId(), holder.searchDisplayName.getText().toString(), NotificationModel.SENT_FRIEND_REQUEST, db);

                //add received friend request to receiver
                FirebaseOperations.addFriendRequest(results.get(position).getUserId(), user.getUid(), user.getDisplayName(), NotificationModel.RECEIVED_FRIEND_REQUEST, db);

                //add friend request notification to receiver
                NotificationModel newNotification = new NotificationModel(results.get(position).getUserId(), user.getUid(), NotificationModel.FRIEND_REQUEST_NOTIFICATION);
                FirebaseOperations.addNotification(newNotification, db);
            }
        });

        holder.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultClickInterface.viewUserProfile(results.get(position).getUserId());
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

        RelativeLayout searchCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            searchProfile = itemView.findViewById(R.id.searchProfile);
            searchDisplayName = itemView.findViewById(R.id.searchDisplayName);
            searchEmail = itemView.findViewById(R.id.searchEmail);
            sendRequest = itemView.findViewById(R.id.sendRequest);
            searchCard = itemView.findViewById(R.id.searchCard);
        }
    }

    public interface SearchResultClickInterface{
       public void viewUserProfile(String fromUserId);
    }
}
