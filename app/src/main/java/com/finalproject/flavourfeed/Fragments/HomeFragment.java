package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Pages.ChatRoomPage;
import com.finalproject.flavourfeed.Pages.AddPostPage;
import com.finalproject.flavourfeed.Models.PostModel;
import com.finalproject.flavourfeed.Adapters.PostAdapter;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PostAdapter.PostAdapterInterface {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<PostModel> posts;
    RecyclerView postRecyclerView;
    PostAdapter postAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        posts = new ArrayList<>();
        ImageView homeProfileFragment = view.findViewById(R.id.homeProfilePicture);
        FloatingActionButton btnCreatePost = view.findViewById(R.id.btnCreatePost);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);

        postRecyclerView = view.findViewById(R.id.postRecyclerView);
        postAdapter = new PostAdapter(PostModel.itemCallback, this);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setAdapter(postAdapter);
        postRecyclerView.setItemAnimator(new NoChangeAnimation());

        getAllData();

        ImageView btnChat = view.findViewById(R.id.btnMessageRoom);
        Glide.with(this).load(user.getPhotoUrl()).into(homeProfileFragment);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatRoomPage.class));
            }
        });

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddPostPage.class));
            }
        });
        return view;
    }

    public void getAllData() {
        db.collection("postInformation").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    List<PostModel> data = value.toObjects(PostModel.class);
                    posts = new ArrayList<>();
                    posts.addAll(data);
                    postAdapter.submitList(posts);
                }
            }
        });

    }



    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(getContext(), PostPage.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(boolean like) {

    }
}