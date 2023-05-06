package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.CommentAdapter;
import com.finalproject.flavourfeed.Firebase.FirebaseOperations;
import com.finalproject.flavourfeed.Models.CommentModel;
import com.finalproject.flavourfeed.Models.NotificationModel;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostPage extends AppCompatActivity{
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView commentRecyclerView;
    ArrayList<CommentModel> comments;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.finalproject.flavourfeed.R.layout.post_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ImageView btnClosePost = findViewById(R.id.btnClosePost);
        ImageView postPicture = findViewById(R.id.postPicture);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        EditText inptComment = findViewById(R.id.inptComment);
        ImageView btnComment = findViewById(R.id.btnComment);
        String postId = getIntent().getStringExtra("postId");
        commentAdapter = new CommentAdapter(CommentModel.itemCallback);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setItemAnimator(new NoChangeAnimation());
        TextView numberOfLikes = findViewById(R.id.numberOfLikes);
        TextView postDisplayName = findViewById(R.id.postDisplayName);
        ImageView postPageLike = findViewById(R.id.postPageLike);
        String photoUrl;
        getAllData(postId);
        DocumentReference documentReference = db.collection("postInformation").document(postId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(PostPage.this).load(documentSnapshot.getString("photoUrl")).into(postPicture);
                numberOfLikes.setText(Integer.toString(documentSnapshot.getLong("likes").intValue()));
                db.collection("userInformation").document(documentSnapshot.getString("userId")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        postDisplayName.setText(documentSnapshot.getString("displayName")+"'s post");
                    }
                });
            }
        });

        postPageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference collectionReference = db.collection("userInformation").document(user.getUid()).collection("likedPosts");
                Query query = collectionReference.whereEqualTo("postId", postId);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null && !snapshot.isEmpty()) {
                            // The post exists in the collection
                            db.collection("userInformation").document(user.getUid()).collection("likedPosts").document(postId).delete();

                            DocumentReference docRef = db.collection("postInformation").document(postId);

                            docRef.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document = task1.getResult();
                                    if (document.exists()) {
                                        int likes = document.getLong("likes").intValue();
                                        // Document exists
                                        db.collection("postInformation").document(postId).update("likes", likes - 1);
                                    }
                                }
                            });

                            postPageLike.setImageResource(R.drawable.newlikeicon);
                        } else {
                            // The post does not exist in the collection
                            Map<String, Object> likedPost = new HashMap<>();
                            likedPost.put("postId", postId);
                            db.collection("userInformation").document(user.getUid()).collection("likedPosts").document(postId).set(likedPost);
                            postPageLike.setImageResource(R.drawable.newlikedicon);
                            DocumentReference docRef = db.collection("postInformation").document(postId);

                            docRef.get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document = task1.getResult();
                                    if (document.exists()) {
                                        int likes = document.getLong("likes").intValue();
                                        // Document exists
                                        db.collection("postInformation").document(postId).update("likes", likes + 1);
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

        CollectionReference collectionReference = db.collection("userInformation").document(user.getUid()).collection("likedPosts");
        Query query = collectionReference.whereEqualTo("postId", postId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    postPageLike.setImageResource(R.drawable.newlikedicon);
                }
            }
        });


        btnClosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPage.super.onBackPressed();
            }
        });
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> newComment = new HashMap<>();
                newComment.put("comment", inptComment.getText().toString());
                newComment.put("userId", user.getUid());
                newComment.put("postId", postId);
                newComment.put("timestamp", FieldValue.serverTimestamp());
                db.collection("commentInformation").add(newComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String commentId = documentReference.getId();
                        db.collection("commentInformation").document(commentId).update("commentId", commentId);
                        inptComment.setText("");
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                });

                db.collection("postInformation").document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String postUserId = documentSnapshot.getString("userId");
                            if (postUserId != user.getUid()) {
                                NotificationModel newNotification = new NotificationModel(postUserId, user.getUid(), NotificationModel.COMMENT_NOTIFICATION, postId);
                                FirebaseOperations.addNotification(newNotification, db);
                            }
                        }
                    }
                });

            }
        });
    }

    public void getAllData(String postId) {
        db.collection("commentInformation")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<CommentModel> data = value.toObjects(CommentModel.class);
                            comments = new ArrayList<>();
                            for (CommentModel comment : data) {
                                if (comment.getPostId().equals(postId)) {
                                    comments.add(comment);
                                }
                            }
                            commentAdapter.submitList(comments);
                            commentRecyclerView.scrollToPosition(commentAdapter.getItemCount() - 1);
                        }
                    }
                });
    }

}