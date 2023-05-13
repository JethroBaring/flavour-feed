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
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        imageView = view.findViewById(R.id.heart);
        return view;
    }

    public void getAllData() {

        db.collection("postInformation").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                // Handle new document
                                PostModel post = dc.getDocument().toObject(PostModel.class);
                                posts.add(0, post);
                                postAdapter.notifyItemInserted(0);
                                break;
                            case MODIFIED:
                                // Handle modified document
                                PostModel po = dc.getDocument().toObject(PostModel.class);
                                int position = getPosition(po);
                                if (position != -1) {
                                    posts.set(position, po);
                                    postAdapter.notifyItemChanged(position);
                                }
                                break;
                            case REMOVED:
                                // Handle removed document
                                PostModel pt = dc.getDocument().toObject(PostModel.class);
                                int posin = getPosition(pt);
                                if (posin != -1) {
                                    posts.remove(posin);
                                    postAdapter.notifyItemRemoved(posin);
                                }
                                break;
                        }
                    }
                    postAdapter.submitList(posts);
                }
            }
        });


    }

    private int getPosition(PostModel post) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getPostId().equals(post.getPostId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(getContext(), PostPage.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }

    @Override
    public void onLikeClick(boolean like) {
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.unlike);
        if (like) {
            anim = AnimationUtils.loadAnimation(getContext(), R.anim.like);
        }
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(anim);
        imageView.setVisibility(View.INVISIBLE);
    }
}