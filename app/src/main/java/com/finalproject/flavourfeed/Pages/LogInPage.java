package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.PasswordToggle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        TextView lnkForgotPassword = findViewById(R.id.lnkForgotPassword);
        TextView lnkSignUp = findViewById(R.id.lnkSignUpPage);
        TextInputEditText txtInptLogInEmail = findViewById(R.id.txtInptLogInEmail);
        TextInputEditText txtInptLogInPassword = findViewById(R.id.txtInptLogInPassword);
        RelativeLayout relativeLayout = findViewById(R.id.layoutLogIn);
        Button btnLogIn = findViewById(R.id.btnLogIn);

        final TextInputLayout passwordTextInputLayout = findViewById(R.id.txtInptLayoutPassword);
        final EditText passwordEditText = passwordTextInputLayout.getEditText();
        PasswordToggle.changeToggleIcon(this, passwordTextInputLayout, passwordEditText);

        lnkSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
            startActivity(intent);
        });

        btnLogIn.setOnClickListener(view -> {
            String email = txtInptLogInEmail.getText().toString();
            String password = txtInptLogInPassword.getText().toString();
            Snackbar snackbar = Snackbar.make(relativeLayout, null, Snackbar.LENGTH_SHORT);

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                snackbar.setText("Field/s cannot be empty.");
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        snackbar.setText("Logging in.");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.getEmail().equals("admin@gmail.com")) {
                            Intent intent = new Intent(getApplicationContext(), AdminDashboardPage.class);
                            startActivity(intent);
                        } else {
                            db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                                if (Boolean.TRUE.equals(documentSnapshot.getBoolean("ban"))) {
                                    startActivity(new Intent(getApplicationContext(), BannedPage.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainPage.class));
                                }
                            });
                        }
                    } else {
                        snackbar.setText(task.getException().getMessage());
                    }
                });
            }
            snackbar.show();
        });
    }

}