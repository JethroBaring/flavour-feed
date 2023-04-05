package com.finalproject.flavourfeed;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        TextView textView = findViewById(R.id.appName);
        Button getStarted = findViewById(R.id.getStarted);
        GradientText gradientText = new GradientText();
        gradientText.setTextViewColor(textView, getResources().getColor(R.color.red), getResources().getColor(R.color.pink));

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(intent);
            }
        });
    }
}