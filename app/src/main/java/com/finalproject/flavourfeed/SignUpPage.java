package com.finalproject.flavourfeed;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        TextView logInPage = findViewById(R.id.logInPage);
        GradientText gradientText = new GradientText();
        gradientText.setTextViewColor(logInPage,getResources().getColor(R.color.red), getResources().getColor(R.color.pink));

        logInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(intent);
            }
        });
    }
}