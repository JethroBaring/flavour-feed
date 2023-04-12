package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = SignUpPage.this.getWindow();
        Drawable background = SignUpPage.this.getResources().getDrawable(R.drawable.gradientsignup);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(SignUpPage.this.getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        setContentView(R.layout.sign_up_page);

        TextView lnkLogInPage = findViewById(R.id.lnkLogInPage);
        TextInputEditText txtInptSignUpEmail = findViewById(R.id.txtInptSignUpEmail);
        TextInputEditText txtInptSignUpPassword = findViewById(R.id.txtInptSignUpPassword);
        TextInputEditText txtInptSignUpRepeatPassword = findViewById(R.id.txtInptSignUpRepeatPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        relativeLayout = findViewById(R.id.relativeSignUp);
        mAuth = FirebaseAuth.getInstance();

        GradientText.setTextViewColor(lnkLogInPage, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));


        lnkLogInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogInPage.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserWithEmailAndPassword(txtInptSignUpEmail.getText().toString(), txtInptSignUpPassword.getText().toString(), txtInptSignUpRepeatPassword.getText().toString());
            }
        });
    }


    public void createUserWithEmailAndPassword(String email, String password, String repeatPassword) {
        Snackbar snackbar = Snackbar.make(relativeLayout, null, Snackbar.LENGTH_SHORT);
        if(password.equals(repeatPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    snackbar.setText(task.getException().getMessage());
                }
            });
        } else {
            snackbar.setText("Password do not match.");
        }
        snackbar.show();
    }
}