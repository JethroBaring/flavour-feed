package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Pages.EditProfilePage;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.SettingsPage;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        TextView profileEmail = view.findViewById(R.id.profileEmail);
        TextView profileDisplayName = view.findViewById(R.id.profileDisplayName);
        ShapeableImageView profilePicture = view.findViewById(R.id.profilePicture);
        ImageView icnSettings = view.findViewById(R.id.icnSettings);

        icnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SettingsPage.class));
            }
        });

        profileDisplayName.setText(user.getDisplayName());
        profileEmail.setText(user.getEmail());

        Glide.with(this)
                        .load(user.getPhotoUrl())
                                .into(profilePicture);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfilePage.class);
                startActivity(intent);
            }
        });

        return view;
    }


}