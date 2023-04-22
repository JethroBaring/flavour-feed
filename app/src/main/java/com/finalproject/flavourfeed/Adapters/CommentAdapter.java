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
import com.finalproject.flavourfeed.Entity.CommentEntity;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentAdapter extends ListAdapter<CommentEntity, CommentAdapter.CommentViewHolder> {
    CommentClickInterface commentClickInterface;
    FirebaseFirestore db;
    public CommentAdapter(@NonNull DiffUtil.ItemCallback<CommentEntity> diffCallback, CommentClickInterface commentClickInterface) {
        super(diffCallback);
        this.commentClickInterface = commentClickInterface;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentEntity comment = getItem(position);
        holder.bind(comment);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView commenterProfile;
        TextView commenterDisplayName;
        TextView commentText;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterProfile = itemView.findViewById(R.id.commenterProfile);
            commenterDisplayName = itemView.findViewById(R.id.commenterDisplayName);
            commentText = itemView.findViewById(R.id.txtComment);
        }

        public void bind(CommentEntity comment){
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("userInformation").document(comment.getUserId());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()) {
                            commenterDisplayName.setText(documentSnapshot.getString("displayName"));
                            Glide.with(itemView.getContext()).load(documentSnapshot.getString("profileUrl")).into(commenterProfile);
                        }
                    }
                }
            });
            commentText.setText(comment.getComment());
        }
    }

    public interface CommentClickInterface {
        public void onDelete(int pos);
    }

}