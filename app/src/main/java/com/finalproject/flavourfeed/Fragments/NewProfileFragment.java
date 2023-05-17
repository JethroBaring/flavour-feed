package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.AllPostLikedAdapter;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.Pages.ChatRoomPage;
import com.finalproject.flavourfeed.Pages.EditProfilePage;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.Pages.SettingsPage;
import com.finalproject.flavourfeed.R;
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


public class NewProfileFragment extends Fragment implements AllPostLikedAdapter.PostClickInterface {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    RecyclerView allPostsRecyclerView;
    AllPostLikedAdapter allPostLikedAdapter;
    ArrayList<PostModel> allposts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        allposts = new ArrayList<>();
        allPostsRecyclerView = view.findViewById(R.id.profileRecyclerView);
        allPostLikedAdapter = new AllPostLikedAdapter(PostModel.itemCallback, this);
        allPostsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        allPostsRecyclerView.setAdapter(allPostLikedAdapter);
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView profileDisplayName = view.findViewById(R.id.profileDisplayName);
        Glide.with(this).load(user.getPhotoUrl()).into(profilePicture);
        profileDisplayName.setText(user.getDisplayName());
        ImageView settings = view.findViewById(R.id.icnSettings);
        TabLayout profileTabLayout = view.findViewById(R.id.profileTabLayout);
        ImageView backgroundPicture = view.findViewById(R.id.backgroundPicture);
        getAllData();
        profileTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getAllData();
                        break;
                    case 1:
                        getLikedPost();
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
        settings.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsPage.class)));
        TextView numberOfFollowing = view.findViewById(R.id.numberOfFollowing);
        TextView numberOfFollowers = view.findViewById(R.id.numberOfFollowers);
        TextView numberOfLikes = view.findViewById(R.id.numberOfLikes);
        MaterialCardView btnEditProfile = view.findViewById(R.id.btnEditProfile);
        MaterialCardView btnMessageRoom = view.findViewById(R.id.btnMessageRoom);

        btnEditProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfilePage.class)));
        btnMessageRoom.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChatRoomPage.class)));


        db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            numberOfFollowing.setText(Integer.toString(documentSnapshot.getLong("followings").intValue()));
            numberOfFollowers.setText(Integer.toString(documentSnapshot.getLong("followers").intValue()));
            numberOfLikes.setText(Integer.toString(documentSnapshot.getLong("likes").intValue()));
            Glide.with(this).load(documentSnapshot.getString("backgroundUrl")).into(backgroundPicture);
        });
        return view;
    }

    public void getAllData() {
        db.collection("postInformation").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allposts.clear();
                allPostLikedAdapter.notifyDataSetChanged();
                if (error == null) {
                    allposts = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                        if (queryDocumentSnapshot.getString("userId").equals(user.getUid())) {
                            allposts.add(queryDocumentSnapshot.toObject(PostModel.class));
                        }
                    }
                    allPostLikedAdapter.submitList(allposts);
                }
            }
        });
    }

    public void getLikedPost() {
        db.collection("userInformation").document(user.getUid()).collection("likedPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
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