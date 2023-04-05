package com.finalproject.flavourfeed;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView signUp = findViewById(R.id.signUp);
        GradientText gradientText = new GradientText();
        gradientText.setTextViewColor(forgotPassword, getResources().getColor(R.color.red), getResources().getColor(R.color.pink));
        gradientText.setTextViewColor(signUp, getResources().getColor(R.color.red), getResources().getColor(R.color.pink));
    }
}