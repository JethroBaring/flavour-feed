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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView commentRecyclerView;
    ArrayList<Comment> comments;

    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ImageView btnClosePost = findViewById(R.id.btnClosePost);
        comments = new ArrayList<>();
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        EditText inptComment = findViewById(R.id.inptComment);
        ImageView btnComment = findViewById(R.id.btnComment);
        postId = getIntent().getStringExtra("postId");
        commentRecyclerView.setAdapter(new CommentAdapter(PostPage.this, comments));
        getAllData();

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

    public void getAllData() {
        db.collection("commentInformation").whereEqualTo("postId", postId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            comments.clear();
                            List<Comment> data = value.toObjects(Comment.class);
                            comments.addAll(data);
                            commentRecyclerView.setLayoutManager(new LinearLayoutManager(PostPage.this));
                            commentRecyclerView.setAdapter(new CommentAdapter(PostPage.this, comments));
                        }
                    }
                });
    }
}