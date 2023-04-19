package com.finalproject.flavourfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    ArrayList<Comment> comments;
    FirebaseFirestore db;
    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_card, parent, false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Comment comment = comments.get(holder.getAdapterPosition());
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("userInformation").document(comment.getUserId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        holder.commenterDisplayName.setText(documentSnapshot.getString("displayName"));
                        Glide.with(holder.itemView.getContext()).load(documentSnapshot.getString("profilePicture")).into(holder.commenterProfile);
                    }
                }
            }
        });
        holder.comment.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView commenterProfile;
        TextView commenterDisplayName;
        TextView comment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterProfile = itemView.findViewById(R.id.commenterProfile);
            commenterDisplayName = itemView.findViewById(R.id.commenterDisplayName);
            comment = itemView.findViewById(R.id.txtComment);
        }
    }


}
