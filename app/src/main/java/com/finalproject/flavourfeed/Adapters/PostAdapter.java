package com.finalproject.flavourfeed.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostAdapter extends ListAdapter<PostModel, PostAdapter.PostViewHolder> {

    FirebaseFirestore db;

    FirebaseUser user;


    PostAdapterInterface postAdapterInterface;

    public PostAdapter(@NonNull DiffUtil.ItemCallback<PostModel> diffCallback, PostAdapterInterface postAdapterInterface) {
        super(diffCallback);
        this.postAdapterInterface = postAdapterInterface;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
    PostModel model = getItem(position);
    holder.bind(model);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card2, parent, false));
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postPhoto;
        ImageView profile;
        TextView displayName;
        TextView email;
        TextView caption;

        LinearLayout likeContainer;

        ImageView likeIcon;

        ImageView btnDown;
        TextView numberOfLikes;
        TextView numberOfComments;

        LinearLayout commentContainer;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postPhoto = itemView.findViewById(R.id.postPhoto);
            profile = itemView.findViewById(R.id.profile);
            displayName = itemView.findViewById(R.id.displayName);
            email = itemView.findViewById(R.id.email);
            caption = itemView.findViewById(R.id.caption);
            likeContainer = itemView.findViewById(R.id.likeContainer);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            btnDown = itemView.findViewById(R.id.btnDown);
            numberOfLikes = itemView.findViewById(R.id.numberOfLikes);
            numberOfComments = itemView.findViewById(R.id.numberOfComments);
            commentContainer = itemView.findViewById(R.id.commentContainer);
        }

        public void bind(PostModel postModel) {
            db = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            btnDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnDown.getId() == R.id.btnDown) {
                        btnDown.setImageResource(R.drawable.newarrowupicon);
                        btnDown.setId(R.id.btnUp);
                        caption.setVisibility(View.VISIBLE);
                    } else {
                        btnDown.setImageResource(R.drawable.newarrowdownicon);
                        btnDown.setId(R.id.btnDown);
                        caption.setVisibility(View.GONE);
                    }
                }
            });

            commentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postAdapterInterface.onCommentClick(postModel.getPostId());
                }
            });

            numberOfLikes.setText(Integer.toString(postModel.getLikes()));
            numberOfComments.setText(Integer.toString(postModel.getComments()));

            DocumentReference documentReference = db.collection("userInformation").document(postModel.getUserId());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            displayName.setText(documentSnapshot.getString("displayName"));
                            email.setText(documentSnapshot.getString("email"));
                            Glide.with(itemView.getContext()).load(documentSnapshot.getString("profileUrl")).into(profile);
                        }
                    }
                }
            });
            Glide.with(itemView.getContext()).load(postModel.getPhotoUrl()).into(postPhoto);
            caption.setText(postModel.getCaption());

            postPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PostPage.class);
                    intent.putExtra("postId", postModel.getPostId());
                    view.getContext().startActivity(intent);
                }
            });

            db.collection("userInformation").document(user.getUid()).collection("likedPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        if(documentSnapshot.getString("postId").equals(postModel.getPostId())){
                            break;
                        }
                    }
                    likeIcon.setImageResource(R.drawable.newlikeicon);
                }
            });

            likeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CollectionReference collectionReference = db.collection("userInformation").document(user.getUid()).collection("likedPosts");
                    Query query = collectionReference.whereEqualTo("postId", postModel.getPostId());
                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null && !snapshot.isEmpty()) {
                                // The post exists in the collection
                                db.collection("userInformation").document(user.getUid()).collection("likedPosts").document(postModel.getPostId()).delete();

                                DocumentReference docRef = db.collection("postInformation").document(postModel.getPostId());

                                docRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            int likes = document.getLong("likes").intValue();
                                            // Document exists
                                            db.collection("postInformation").document(postModel.getPostId()).update("likes", likes - 1);
                                            postAdapterInterface.onLikeClick(false);
                                        }
                                    }
                                });

                                likeIcon.setImageResource(R.drawable.newlikeicon);
                            } else {
                                // The post does not exist in the collection
                                Map<String, Object> likedPost = new HashMap<>();
                                likedPost.put("postId", postModel.getPostId());
                                db.collection("userInformation").document(user.getUid()).collection("likedPosts").document(postModel.getPostId()).set(likedPost);
                                likeIcon.setImageResource(R.drawable.newlikedicon);
                                DocumentReference docRef = db.collection("postInformation").document(postModel.getPostId());

                                docRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            int likes = document.getLong("likes").intValue();
                                            // Document exists
                                            db.collection("postInformation").document(postModel.getPostId()).update("likes", likes + 1);
                                            postAdapterInterface.onLikeClick(true);
                                        }
                                    }
                                });

                            }
                        }
                    });

                }

            });

            CollectionReference collectionReference = db.collection("userInformation").document(user.getUid()).collection("likedPosts");
            Query query = collectionReference.whereEqualTo("postId", postModel.getPostId());

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        likeIcon.setImageResource(R.drawable.newlikedicon);
                    }
                }
            });
        }
    }

    public interface PostAdapterInterface {
        public void onCommentClick(String postId);
        public void onLikeClick(boolean like);
    }
}