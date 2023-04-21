package com.finalproject.flavourfeed.Pages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.CommentAdapter;
import com.finalproject.flavourfeed.Entity.CommentEntity;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostPage extends AppCompatActivity implements CommentAdapter.CommentClickInterface{
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView commentRecyclerView;
    ArrayList<CommentEntity> comments;
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
        commentAdapter = new CommentAdapter(CommentEntity.itemCallback , this);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setItemAnimator(new NoChangeAnimation());
        String photoUrl;
        getAllData(postId);
        DocumentReference documentReference = db.collection("postInformation").document(postId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(PostPage.this).load(documentSnapshot.getString("photoUrl")).into(postPicture);
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
                            List<CommentEntity> data = value.toObjects(CommentEntity.class);
                            comments = new ArrayList<>();
                            for (CommentEntity comment : data) {
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