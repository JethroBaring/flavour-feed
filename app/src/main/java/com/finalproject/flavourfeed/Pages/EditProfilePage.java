package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfilePage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Uri imageUri;

    Uri backgroundUri;
    ImageView profileImage;
    ImageView backgroundImage;
    TextInputEditText txtInptNewDisplayName;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseFirestore db;
    DocumentReference userRef;
    String imageUrl;
    String backgroundUrl;
    boolean isProfile;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_page);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        profileImage = findViewById(R.id.testimage);
        txtInptNewDisplayName = findViewById(R.id.txtInptNewDisplayName);
        txtInptNewDisplayName.setText(user.getDisplayName());
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(profileImage);
        ImageView btnClose = findViewById(R.id.btnClose);
        ImageView btnSave = findViewById(R.id.btnSave);
        backgroundImage = findViewById(R.id.backgroundPicture);
        RelativeLayout btnSelectPicture = findViewById(R.id.btnSelectPicture);
        RelativeLayout btnSelectBackground = findViewById(R.id.btnSelectBackground);
        db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Glide.with(this).load(documentSnapshot.getString("backgroundUrl")).into(backgroundImage);
        });
        btnSelectPicture.setOnClickListener(v -> openFileChooser(true));
        btnSelectBackground.setOnClickListener(v -> openFileChooser(false));
        btnClose.setOnClickListener(v -> EditProfilePage.super.onBackPressed());
        btnSave.setOnClickListener(v -> {
            saveProfileImage();
            saveBackgroundImage();

        });


    }

    private void openFileChooser(boolean x) {
        isProfile = x;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(isProfile) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            } else {
                backgroundUri = data.getData();
                backgroundImage.setImageURI(backgroundUri);
            }
        }
    }

    private void saveProfileImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child("images/" + UUID.randomUUID().toString());

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = fileReference.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(txtInptNewDisplayName.getText().toString())
                                            .setPhotoUri(Uri.parse(imageUrl))
                                            .build();

                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                db = FirebaseFirestore.getInstance();
                                                userRef = db.collection("userInformation").document(user.getUid());
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("displayName", user.getDisplayName());
                                                updates.put("profileUrl", user.getPhotoUrl());
                                                userRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Snackbar.make(findViewById(android.R.id.content), "User profile updated.", Snackbar.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(findViewById(android.R.id.content), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else if (!txtInptNewDisplayName.getText().toString().equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(txtInptNewDisplayName.getText().toString())
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        db = FirebaseFirestore.getInstance();
                        userRef = db.collection("userInformation").document(user.getUid());
                        userRef.update("displayName", user.getDisplayName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "User profile updated.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
            });
        }
    }

    public void saveBackgroundImage() {
        if (backgroundUri != null) {
            StorageReference fileReference = storageRef.child("background/" + UUID.randomUUID().toString());

            fileReference.putFile(backgroundUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = fileReference.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    backgroundUrl = uri.toString();
                                    db.collection("userInformation").document(user.getUid()).update("backgroundUrl",backgroundUrl);
                                }
                            });
                        }
                    });
        }
    }
}
