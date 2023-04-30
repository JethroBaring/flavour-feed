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
import com.finalproject.flavourfeed.Models.ResultModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatSearchAdapter extends RecyclerView.Adapter<ChatSearchAdapter.MyViewHolder> {
    FirebaseFirestore db;
    Context context;
    List<ResultModel> results;

    ChatSearchClickInterface chatSearchClickInterface;
    public ChatSearchAdapter(Context context, List<ResultModel> results, ChatSearchClickInterface chatSearchClickInterface) {
        this.context = context;
        this.results = results;
        this.chatSearchClickInterface = chatSearchClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_card, parent, false);
        return new ChatSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        db = FirebaseFirestore.getInstance();
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
        holder.sendRequest.setVisibility(View.INVISIBLE);
        holder.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatSearchClickInterface.onSearchResultClick(results.get(position).getUserId());
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
    
    public interface ChatSearchClickInterface {
        public void onSearchResultClick(String otherUserId);
    }

}
