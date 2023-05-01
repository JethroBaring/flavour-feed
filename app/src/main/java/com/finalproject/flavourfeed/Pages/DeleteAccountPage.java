package com.finalproject.flavourfeed.Pages;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.PasswordToggle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;

public class DeleteAccountPage extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_account_page);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        RelativeLayout relativeLayout = findViewById(R.id.layoutDelete);
        TextInputEditText txtInptEmail = findViewById(R.id.txtInptEmail);
        TextInputEditText txtInptPassword = findViewById(R.id.txtInptPassword);
        TextInputEditText txtInptConfirm = findViewById(R.id.txtInptConfirm);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        final TextInputLayout passwordTextInputLayout = findViewById(R.id.txtInptLayoutPassword);
        final EditText passwordEditText = passwordTextInputLayout.getEditText();
        PasswordToggle.changeToggleIcon(this, passwordTextInputLayout, passwordEditText);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtInptEmail.getText().toString();
                String password = txtInptPassword.getText().toString();
                String confirm = txtInptConfirm.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(password)) {
                    Toast.makeText(DeleteAccountPage.this, "Field/s should not be empty.", Toast.LENGTH_SHORT).show();
                } else if (!confirm.equals("CONFIRM")) {
                    Toast.makeText(DeleteAccountPage.this, "Input CONFIRM.", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayout, "Are you sure you want to delete your account?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Confirm", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mAuth.signOut();
                                                    Toast.makeText(DeleteAccountPage.this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LogInPage.class));
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(DeleteAccountPage.this, "Incorrect email/password.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                    snackbar.show();

                }


            }
        });
    }
}