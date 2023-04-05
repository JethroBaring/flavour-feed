package com.finalproject.flavourfeed;

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
        GradientText gradientText = new GradientText();
        gradientText.setTextViewColor(textView, getResources().getColor(R.color.red), getResources().getColor(R.color.pink));

    }
}