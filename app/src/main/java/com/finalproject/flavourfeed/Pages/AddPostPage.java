package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPostPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    ImageView postPicture;
    TextInputEditText txtInptNewDisplayName;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    FirebaseFirestore db;
    DocumentReference postRef;
    String imageUrl;
    ImageView cameraIcon;

    EditText caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_page);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        postPicture = findViewById(R.id.postPicture);
        ImageView profileImage = findViewById(R.id.userProfilePicture);
        TextView userDisplayName = findViewById(R.id.userDisplayName);
        TextView userEmail = findViewById(R.id.userEmail);
        ImageView btnUploadPost = findViewById(R.id.btnAddProduct);
        RelativeLayout postPictureContainer = findViewById(R.id.postPictureContainer);
        cameraIcon = findViewById(R.id.cameraIcon);
        caption = findViewById(R.id.caption);

        Glide.with(this).load(user.getPhotoUrl()).into(profileImage);
        userDisplayName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        ImageView btnClose = findViewById(R.id.btnClose);

        postPictureContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPostPage.super.onBackPressed();
            }
        });


        btnUploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            postPicture.setImageURI(imageUri);
            cameraIcon.setVisibility(View.INVISIBLE);
        } else if(imageUri == null){
            cameraIcon.setVisibility(View.VISIBLE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveChanges() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child("post/" + UUID.randomUUID().toString());

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = fileReference.getDownloadUrl();
                            downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    db = FirebaseFirestore.getInstance();
                                    Map <String, Object>newPost = new HashMap<>();
                                    newPost.put("photoUrl", uri.toString());
                                    newPost.put("caption", caption.getText().toString());
                                    newPost.put("timestamp", FieldValue.serverTimestamp());
                                    newPost.put("userId", user.getUid());
                                    newPost.put("likes", 0);
                                    db.collection("postInformation").add(newPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String postId = documentReference.getId();
                                            db.collection("postInformation").document(postId).update("postId",postId);
                                            Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), MainPage.class));
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
        }
    }
}