package com.finalproject.flavourfeed;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView signUp = findViewById(R.id.signUpPage);
        GradientText.setTextViewColor(forgotPassword, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));
        GradientText.setTextViewColor(signUp, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });
    }
}