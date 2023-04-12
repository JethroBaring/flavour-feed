package com.finalproject.flavourfeed.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
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
        mAuth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.appName);
        Button getStarted = findViewById(R.id.getStarted);
        GradientText.setTextViewColor(textView, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        }
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