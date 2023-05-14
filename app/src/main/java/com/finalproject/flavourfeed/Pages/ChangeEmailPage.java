package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.PasswordToggle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;

public class ChangeEmailPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextInputEditText txtInptEmail = findViewById(R.id.txtInptEmail);
        TextInputEditText txtInptNewEmail = findViewById(R.id.txtInptNewEmail);
        TextInputEditText txtInptPassword = findViewById(R.id.txtInptPassword);
        Button btnChangeEmail = findViewById(R.id.btnChangeEmail);
        final TextInputLayout passwordTextInputLayout = findViewById(R.id.txtInptLayoutPassword);
        final EditText passwordEditText = passwordTextInputLayout.getEditText();
        PasswordToggle.changeToggleIcon(this, passwordTextInputLayout, passwordEditText);

        btnChangeEmail.setOnClickListener(v -> {
            String email = txtInptEmail.getText().toString();
            String newEmail = txtInptNewEmail.getText().toString();
            String password = txtInptPassword.getText().toString();
            if(TextUtils.isEmpty(email ) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(password)) {
                Toast.makeText(ChangeEmailPage.this, "Field/s should not be empty.", Toast.LENGTH_SHORT).show();
            } else {
                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(ChangeEmailPage.this, "Email has been changed.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                        intent.putExtra("fromUpdateProfile", true);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ChangeEmailPage.this, "Incorrect email/password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}