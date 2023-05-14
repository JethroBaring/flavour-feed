package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import com.finalproject.flavourfeed.BannedPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class WelcomePage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Window window = WelcomePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.flutter_sky));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.flutter_sky));

        ImageView logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        logo.startAnimation(animation);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                db = FirebaseFirestore.getInstance();
                if (user != null) {
                    if (user.getEmail().equals("admin@gmail.com")) {
                        startActivity(new Intent(getApplicationContext(), AdminDashboardPage.class));
                    } else {
                        db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(Boolean.TRUE.equals(documentSnapshot.getBoolean("ban"))) {
                                    startActivity(new Intent(getApplicationContext(), BannedPage.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                                }
                            }
                        });

                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), LogInPage.class));
                }
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}