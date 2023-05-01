package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.finalproject.flavourfeed.Pages.LogInPage;
import com.finalproject.flavourfeed.Pages.MainPage;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.PasswordToggle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
        final TextInputLayout passwordTextInputLayout = findViewById(R.id.txtInptLayoutPassword);
        final EditText passwordEditText = passwordTextInputLayout.getEditText();
        final TextInputLayout repeatPasswordTextInputLayout = findViewById(R.id.txtInptLayoutRepeatPassword);
        final EditText repeatPasswordEditText = repeatPasswordTextInputLayout.getEditText();
        PasswordToggle.changeToggleIcon(this, passwordTextInputLayout, passwordEditText);
        PasswordToggle.changeToggleIcon(this, repeatPasswordTextInputLayout, repeatPasswordEditText);
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

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
                    snackbar.setText("Field/s cannot be empty.");
                } else if (password.equals(repeatPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String[] parts = email.split("@");
                            String tempName = parts[0];
                            FirebaseUser user = mAuth.getCurrentUser();
                            String randomPhoto;
                            int random = (int) Math.floor(Math.random() * (4 - 1 + 1) + 1);
                            switch (random) {
                                case 1:
                                    randomPhoto = "https://firebasestorage.googleapis.com/v0/b/flavour-feed-39786.appspot.com/o/avatar%2FOne.jpg?alt=media&token=6cda9953-45ef-4e6d-9abb-d78081e6d401";
                                    break;
                                case 2:
                                    randomPhoto = "https://firebasestorage.googleapis.com/v0/b/flavour-feed-39786.appspot.com/o/avatar%2FTwo.jpg?alt=media&token=09c06c74-fe2a-44d9-8e7e-3d40fa566d92";
                                    break;
                                case 3:
                                    randomPhoto = "https://firebasestorage.googleapis.com/v0/b/flavour-feed-39786.appspot.com/o/avatar%2FThree.jpg?alt=media&token=8f43ac03-de3f-4f1b-8f94-64c68a5376d1";
                                    break;
                                default:
                                    randomPhoto = "https://firebasestorage.googleapis.com/v0/b/flavour-feed-39786.appspot.com/o/avatar%2FFour.jpg?alt=media&token=0860feb1-72c3-49f5-a7b4-3a3c27edcbd2";
                                    break;
                            }

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(tempName)
                                    .setPhotoUri(Uri.parse(randomPhoto))
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    FirebaseApp.initializeApp(getApplicationContext());
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Map<String, Object> newUser = new HashMap<>();
                                    newUser.put("email", user.getEmail());
                                    newUser.put("displayName", user.getDisplayName());
                                    newUser.put("profileUrl", user.getPhotoUrl());
                                    newUser.put("userId", user.getUid());
                                    db.collection("userInformation").document(user.getUid()).set(newUser);
                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                    intent.putExtra("fromSignUp", true);
                                    finish();
                                    startActivity(intent);
                                }
                            });
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