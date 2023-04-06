package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.finalproject.flavourfeed.EmailPassword;
import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        RelativeLayout relativeLayout = findViewById(R.id.relativeSignUp);
        TextView logInPage = findViewById(R.id.logInPage);
        TextInputEditText signUpEmail = findViewById(R.id.signUpEmail);
        TextInputEditText signUpPassword = findViewById(R.id.signUpPassword);
        TextInputEditText signUpRepeatPassword = findViewById(R.id.signUpRepeatPassword);
        Button signUp = findViewById(R.id.signUp);
        GradientText.setTextViewColor(logInPage, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));


        logInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(relativeLayout, null, Snackbar.LENGTH_SHORT);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (TextUtils.isEmpty(signUpEmail.getText().toString()) || TextUtils.isEmpty(signUpPassword.getText().toString()) || TextUtils.isEmpty(signUpRepeatPassword.getText().toString())) {
                    snackbar.setText("Field/s should not be empty.");
                } else if (!signUpEmail.getText().toString().matches(emailPattern)) {
                    snackbar.setText("Invalid email.");
                } else if (signUpPassword.length() < 6) {
                    snackbar.setText("Password minimum length is 6.");
                } else if (!signUpPassword.getText().toString().equals(signUpRepeatPassword.getText().toString())) {
                    snackbar.setText("Password do not match.");
                } else {
                    snackbar.setText("Account created successfully.");
                    EmailPassword.register(signUpEmail.getText().toString(), signUpPassword.getText().toString());
                }
                snackbar.show();
            }
        });
    }

}