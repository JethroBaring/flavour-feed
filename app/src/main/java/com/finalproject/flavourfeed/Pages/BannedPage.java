package com.finalproject.flavourfeed.Pages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.finalproject.flavourfeed.Pages.LogInPage;
import com.finalproject.flavourfeed.Pages.MainPage;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class BannedPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banned_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LogInPage.class));
        });

        db.collection("userInformation").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(Boolean.FALSE.equals(value.getBoolean("ban"))){
                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                    Toast.makeText(getApplicationContext(),"You are unbanned",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}