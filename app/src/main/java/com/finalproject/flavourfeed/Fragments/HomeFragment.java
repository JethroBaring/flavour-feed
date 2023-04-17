package com.finalproject.flavourfeed.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.finalproject.flavourfeed.Pages.CreatePostPage;
import com.finalproject.flavourfeed.PostAdapter;
import com.finalproject.flavourfeed.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        FloatingActionButton btnCreatePost = view.findViewById(R.id.btnCreatePost);
        RecyclerView postRecyclerView = view.findViewById(R.id.postRecyclerView);
        PostAdapter adapter = new PostAdapter(getContext());
        postRecyclerView.setAdapter(adapter);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostPage.class);
                startActivity(intent);
            }
        });

        return view;
    }

}