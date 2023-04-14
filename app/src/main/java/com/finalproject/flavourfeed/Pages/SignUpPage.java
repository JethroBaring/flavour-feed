package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        TextView lnkLogInPage = findViewById(R.id.lnkLogInPage);
        TextInputEditText txtInptSignUpEmail = findViewById(R.id.txtInptSignUpEmail);
        TextInputEditText txtInptSignUpPassword = findViewById(R.id.txtInptSignUpPassword);
        TextInputEditText txtInptSignUpRepeatPassword = findViewById(R.id.txtInptSignUpRepeatPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        RelativeLayout relativeLayout = findViewById(R.id.layoutSignUp);
        mAuth = FirebaseAuth.getInstance();


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
                String email = txtInptSignUpEmail.getText().toString();
                String password = txtInptSignUpPassword.getText().toString();
                String repeatPassword = txtInptSignUpRepeatPassword.getText().toString();
                Snackbar snackbar = Snackbar.make(relativeLayout, null, Snackbar.LENGTH_SHORT);

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
                    snackbar.setText("Field/s cannot be empty.");
                } else if(password.equals(repeatPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            snackbar.setText("Logging in.");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                            intent.putExtra("fromSignUp", true);
                            finish();
                            startActivity(intent);
                        } else {
                            snackbar.setText(task.getException().getMessage());
                        }
                    });
                } else {
                    snackbar.setText("Password do not match.");
                }
                snackbar.show();
            }
        });
    }

}