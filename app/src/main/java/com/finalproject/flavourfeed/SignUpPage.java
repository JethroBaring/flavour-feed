package com.finalproject.flavourfeed;

import android.content.Intent;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintSignUp);
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
                Snackbar snackbar = Snackbar.make(constraintLayout, null, Snackbar.LENGTH_SHORT);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(TextUtils.isEmpty(signUpEmail.getText().toString()) || TextUtils.isEmpty(signUpPassword.getText().toString()) || TextUtils.isEmpty(signUpRepeatPassword.getText().toString())) {
                    snackbar.setText("Field/s should not be empty.");
                } else if(!signUpEmail.getText().toString().matches(emailPattern)) {
                    snackbar.setText("Invalid email.");
                } else if(!signUpPassword.getText().toString().equals(signUpRepeatPassword.getText().toString())) {
                    snackbar.setText("Password do not match.");
                } else {
                    //snackbar.setText("Account created successfully.");
                    FirebaseUser x =  EmailPassword.register(signUpEmail.getText().toString(), signUpPassword.getText().toString());
                    snackbar.setText(x.getEmail());
                }
                snackbar.show();
            }
        });
    }

}