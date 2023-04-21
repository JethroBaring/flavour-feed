package com.finalproject.flavourfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostPage extends AppCompatActivity implements CommentAdapter.CommentClickInterface{
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView commentRecyclerView;
    ArrayList<Comment> comments;
    String postId;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ImageView btnClosePost = findViewById(R.id.btnClosePost);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        EditText inptComment = findViewById(R.id.inptComment);
        ImageView btnComment = findViewById(R.id.btnComment);
        postId = getIntent().getStringExtra("postId");
        commentAdapter = new CommentAdapter(Comment.itemCallback , this);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setItemAnimator(new NoChangeAnimation());
        //initializeData(postId);
        getAllData(postId);

        btnClosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPage.super.onBackPressed();
            }
        });

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
                    }
                });
            }
        });
    }
    /*public void initializeData(String postId) {
        db.collection("commentInformation").whereEqualTo("postId", postId).orderBy("timestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    List<Comment> comments = new ArrayList<>();

                    for(QueryDocumentSnapshot document : snapshot) {
                        // Get the data from each comment document and create a Comment object
                        String commentId = document.getId();
                        String commentText = document.getString("commentText");
                        String userId = document.getString("userId");
                        Comment comment = new Comment(commentId, commentText,userId,postId);
                        comments.add(comment);
                        commentAdapter.submitList(comments);
                    }
                }
            }
        });
    }
    public void addItem(Comment comment) {
        List<Comment> commentList = new ArrayList<>(commentAdapter.getCurrentList());
        commentList.add(comment);
        commentAdapter.submitList(commentList);
    }*/
    public void getAllData(String postId) {
        db.collection("commentInformation")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<Comment> data = value.toObjects(Comment.class);
                            comments = new ArrayList<>();
                            for (Comment comment : data) {
                                if (comment.getPostId().equals(postId)) {
                                    comments.add(comment);
                                }
                            }
                            commentAdapter.submitList(comments);
                            commentRecyclerView.scrollToPosition(commentAdapter.getItemCount()-1);
                        }
                    }
                });
    }


    @Override
    public void onDelete(int pos) {

    }
}