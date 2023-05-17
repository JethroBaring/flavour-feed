package com.finalproject.flavourfeed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.AllPostLikedAdapter;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewUserInformationPage extends AppCompatActivity implements AllPostLikedAdapter.PostClickInterface {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    RecyclerView allPostsRecyclerView;
    AllPostLikedAdapter allPostLikedAdapter;
    ArrayList<PostModel> allposts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_information_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        String fromUserId = getIntent().getStringExtra("fromUserId");
        allposts = new ArrayList<>();
        allPostsRecyclerView = findViewById(R.id.profileRecyclerView);
        allPostLikedAdapter = new AllPostLikedAdapter(PostModel.itemCallback, this);
        allPostsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        allPostsRecyclerView.setAdapter(allPostLikedAdapter);
        ImageView profilePicture = findViewById(R.id.profilePicture);
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView profileDisplayName = findViewById(R.id.profileDisplayName);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        TabLayout profileTabLayout = findViewById(R.id.profileTabLayout);
        getAllData(fromUserId);
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getAllData(fromUserId);
                        break;
                    case 1:
                        getLikedPost(fromUserId);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ImageView backgroundPicture = findViewById(R.id.backgroundPicture);
        db.collection("userInformation").document(fromUserId).get().addOnSuccessListener(documentSnapshot -> {
            Glide.with(this).load(documentSnapshot.getString("profileUrl")).into(profilePicture);
            profileDisplayName.setText(documentSnapshot.getString("displayName"));
            Glide.with(this).load(documentSnapshot.getString("backgroundUrl")).into(backgroundPicture);
        });
    }

    public void getAllData(String fromUserId) {
        db.collection("postInformation").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allposts.clear();
                allPostLikedAdapter.notifyDataSetChanged();
                if (error == null) {
                    allposts = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                        if (queryDocumentSnapshot.getString("userId").equals(fromUserId)) {
                            allposts.add(queryDocumentSnapshot.toObject(PostModel.class));
                        }
                    }
                    allPostLikedAdapter.submitList(allposts);
                }
            }
        });
    }
    public void getLikedPost(String fromUserId) {
        db.collection("userInformation").document(fromUserId).collection("likedPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    allposts.clear();
                    allPostLikedAdapter.notifyDataSetChanged();
                    List<PostModel> data = value.toObjects(PostModel.class);
                    allposts.addAll(data);
                    allPostLikedAdapter.submitList(allposts);
                }
            }
        });
    }


    @Override
    public void onPostClick(String postId) {
        Intent intent = new Intent(getApplicationContext(), PostPage.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

}