package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewUserProfileFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_user_profile_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = getArguments();
        String fromUserId = bundle.getString("fromUserId");

        TextView profileDisplayName = view.findViewById(R.id.profileDisplayName);
        TextView profileEmail = view.findViewById(R.id.profileEmail);
        ImageView profilePicture = view.findViewById(R.id.profilePicture);

        db.collection("userInformation").document(fromUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        profileDisplayName.setText(documentSnapshot.getString("displayName"));
                        profileEmail.setText(documentSnapshot.getString("email"));
                        Glide.with(view.getContext()).load(documentSnapshot.getString("profileUrl")).into(profilePicture);
                    }
                }
            }
        });
        Button addFriend = view.findViewById(R.id.btnAddFriend);
        db.collection("userInformation").document(user.getUid()).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserId = document.getString("userId");
                        if (friendUserId.equals(fromUserId)) {
                            // Current user and this result object have a friend relationship
                            addFriend.setText("Friend");
                        }
                    }
                }
            }
        });

        db.collection("userInformation").document(user.getUid()).collection("sentFriendRequest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserId = document.getString("userId");
                        if (friendUserId.equals(fromUserId)) {
                            // Current user and this result object have a friend relationship
                            addFriend.setText("Pending");
                        }
                    }
                }
            }
        });

        return view;
    }
}