package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.AllPostLikedAdapter;
import com.finalproject.flavourfeed.Firebase.FirebaseOperations;
import com.finalproject.flavourfeed.Models.NotificationModel;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.Pages.MessagePage;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
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


public class NewViewUserProfileFragment extends Fragment implements AllPostLikedAdapter.PostClickInterface {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    RecyclerView allPostsRecyclerView;
    AllPostLikedAdapter allPostLikedAdapter;
    ArrayList<PostModel> allposts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_view_user_profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        String fromUserId = bundle.getString("fromUserId");
        allposts = new ArrayList<>();
        allPostsRecyclerView = view.findViewById(R.id.profileRecyclerView);
        allPostLikedAdapter = new AllPostLikedAdapter(PostModel.itemCallback, this);
        allPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        allPostsRecyclerView.setAdapter(allPostLikedAdapter);
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView profileDisplayName = view.findViewById(R.id.profileDisplayName);

        TabLayout profileTabLayout = view.findViewById(R.id.profileTabLayout);
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
        TextView numberOfFollowing = view.findViewById(R.id.numberOfFollowing);
        TextView numberOfFollowers = view.findViewById(R.id.numberOfFollowers);
        TextView numberOfLikes = view.findViewById(R.id.numberOfLikes);
        MaterialCardView btnFollow = view.findViewById(R.id.btnFollow);
        MaterialCardView btnMessage = view.findViewById(R.id.btnMessage);
        TextView txtFollow = view.findViewById(R.id.txtFollow);
        ImageView backgroundPicture = view.findViewById(R.id.backgroundPicture);
        final boolean[] followed = {false};

        btnFollow.setOnClickListener(v -> {
            if(!followed[0]) {
                FirebaseOperations.addFollow(fromUserId,user.getUid(),user.getDisplayName(), NotificationModel.FOLLOWERS,db);
                db.collection("userInformation").document(fromUserId).get().addOnSuccessListener(documentSnapshot -> {
                    FirebaseOperations.addFollow(user.getUid(),fromUserId,documentSnapshot.getString("displayName"), NotificationModel.FOLLOWINGS,db);
                });
                txtFollow.setText("Followed");
                followed[0] = true;
            } else {

                FirebaseOperations.removeFollow(user.getUid(),fromUserId,NotificationModel.FOLLOWINGS,db);
                FirebaseOperations.removeFollow(fromUserId,user.getUid(),NotificationModel.FOLLOWERS,db);
                txtFollow.setText("Follow");
                followed[0] = false;
            }
        });
        btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessagePage.class);
            intent.putExtra("otherUserId", fromUserId);
            startActivity(intent);
        });


        db.collection("userInformation").document(fromUserId).get().addOnSuccessListener(documentSnapshot -> {
            Glide.with(this).load(documentSnapshot.getString("profileUrl")).into(profilePicture);
            profileDisplayName.setText(documentSnapshot.getString("displayName"));
            numberOfFollowing.setText(Integer.toString(documentSnapshot.getLong("followings").intValue()));
            numberOfFollowers.setText(Integer.toString(documentSnapshot.getLong("followers").intValue()));
            numberOfLikes.setText(Integer.toString(documentSnapshot.getLong("likes").intValue()));
            Glide.with(this).load(documentSnapshot.getString("backgroundUrl")).into(backgroundPicture);
        });

        db.collection("userInformation").document(user.getUid()).collection("followings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserId = document.getString("userId");
                        if (friendUserId.equals(fromUserId)) {
                            // Current user and this result object have a friend relationship
                            txtFollow.setText("Followed");
                            followed[0] = true;
                        }
                    }
                }
            }
        });

        return view;
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
        Intent intent = new Intent(getActivity(), PostPage.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

}