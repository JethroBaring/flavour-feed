package com.finalproject.flavourfeed.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        Window window = WelcomePage.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.flutter_sky));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.flutter_sky));

        ImageView logo = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        logo.startAnimation(animation);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LogInPage.class));
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("newUser", false)) {
            Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
            startActivity(intent);
        } else {
            sharedPreferences = getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("newUser", true);
            editor.apply();
        }*/
    }
}