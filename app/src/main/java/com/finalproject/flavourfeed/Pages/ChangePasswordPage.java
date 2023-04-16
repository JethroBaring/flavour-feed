package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.finalproject.flavourfeed.Fragments.ProfileFragment;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;

public class ChangePasswordPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextInputEditText txtInptEmail = findViewById(R.id.txtInptEmail);
        TextInputEditText txtInptPassword = findViewById(R.id.txtInptPassword);
        TextInputEditText txtInptNewPassword = findViewById(R.id.txtInptNewPassword);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtInptEmail.getText().toString();
                String password = txtInptPassword.getText().toString();
                String newPassword = txtInptNewPassword.getText().toString();
                if(TextUtils.isEmpty(email ) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(password)) {
                    Toast.makeText(ChangePasswordPage.this, "Field/s should not be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(ChangePasswordPage.this, "Passwod has been changed.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                            intent.putExtra("fromUpdateProfile", true);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(ChangePasswordPage.this, "Incorrect email/password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}