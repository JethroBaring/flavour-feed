package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = LogInPage.this.getWindow();
        Drawable background = LogInPage.this.getResources().getDrawable(R.drawable.gradient);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(LogInPage.this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.log_in_page);


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
        mAuth = FirebaseAuth.getInstance();
        TextView lnkForgotPassword = findViewById(R.id.lnkForgotPassword);
        TextView lnkSignUp = findViewById(R.id.lnkSignUpPage);
        TextInputEditText txtInptLogInEmail = findViewById(R.id.txtInptLogInEmail);
        TextInputEditText txtInptLogInPassword = findViewById(R.id.txtInptLogInPassword);
        RelativeLayout relativeLayout = findViewById(R.id.layoutLogIn);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        GradientText.setTextViewColor(lnkForgotPassword, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));
        GradientText.setTextViewColor(lnkSignUp, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));

        lnkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtInptLogInEmail.getText().toString();
                String password = txtInptLogInPassword.getText().toString();
                Snackbar snackbar = Snackbar.make(relativeLayout, null, Snackbar.LENGTH_SHORT);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(intent);
                    } else {
                        snackbar.setText(task.getException().getMessage());
                    }
                });
                snackbar.show();
            }
        });
    }
}