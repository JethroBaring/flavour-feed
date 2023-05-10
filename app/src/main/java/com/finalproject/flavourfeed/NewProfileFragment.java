package com.finalproject.flavourfeed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        TextView profileDisplayName = view.findViewById(R.id.profileDisplayName);
        Glide.with(this).load(user.getPhotoUrl()).into(profilePicture);
        profileDisplayName.setText(user.getDisplayName());

        return view;
    }
}