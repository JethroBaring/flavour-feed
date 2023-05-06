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

import com.finalproject.flavourfeed.R;


public class WelcomePage extends AppCompatActivity {

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
    }
}