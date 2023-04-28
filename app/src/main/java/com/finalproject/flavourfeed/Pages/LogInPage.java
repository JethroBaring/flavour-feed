package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    snackbar.setText("Field/s cannot be empty.");
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            snackbar.setText("Logging in.");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            startActivity(intent);
                        } else {
                            snackbar.setText(task.getException().getMessage());
                        }
                    });
                }
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        }
    }
}