package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.android.material.textfield.TextInputEditText;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = LogInPage.this.getWindow();
        Drawable background = LogInPage.this.getResources().getDrawable(R.drawable.gradient);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(LogInPage.this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.log_in_page);

        TextView forgotPassword = findViewById(R.id.lnkForgotPassword);
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                //TODO
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                //TODO
                break;
        }

        TextView signUp = findViewById(R.id.lnkSignUpPage);
        TextInputEditText email = findViewById(R.id.txtInptLogInEmail);
        TextInputEditText password = findViewById(R.id.txtInptLogInPassword);
        Button logIn = findViewById(R.id.btnLogIn);
        GradientText.setTextViewColor(forgotPassword, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));
        GradientText.setTextViewColor(signUp, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}