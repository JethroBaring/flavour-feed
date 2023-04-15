package com.finalproject.flavourfeed;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.finalproject.flavourfeed.Pages.LogInPage;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        mAuth = FirebaseAuth.getInstance();
        ImageView icnSettingsBack = findViewById(R.id.icnSettingsBack);
        LinearLayout lnkDeleteAccount = findViewById(R.id.lnkDeleteAccount);
        LinearLayout lnkLogOut = findViewById(R.id.lnkLogOut);
        icnSettingsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsPage.super.onBackPressed();
            }
        });

        lnkDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteAccountPage.class));
            }
        });

        lnkLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LogInPage.class));
            }
        });
    }
}