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

import com.finalproject.flavourfeed.Pages.CreatePostPage;
import com.finalproject.flavourfeed.Entity.PostEntity;
import com.finalproject.flavourfeed.Adapters.PostAdapter;
import com.finalproject.flavourfeed.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ArrayList<PostEntity> posts;
    RecyclerView postRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        FloatingActionButton btnCreatePost = view.findViewById(R.id.btnCreatePost);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);
        getAllData();

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreatePostPage.class));
            }
        });
        return view;
    }

    public void getAllData() {
        db.collection("postInformation").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null) {
                    posts.clear();
                    List<PostEntity> data = value.toObjects(PostEntity.class);
                    posts.addAll(data);
                    postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    postRecyclerView.setAdapter(new PostAdapter(getContext(), posts));
                }
            }
        });
    }
}