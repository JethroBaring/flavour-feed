package com.finalproject.flavourfeed;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

public class DeleteAccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_account_page);
        RelativeLayout relativeLayout = findViewById(R.id.layoutDelete);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(relativeLayout, "Are you sure you want to delete your account?", Snackbar.LENGTH_LONG);
                snackbar.setAction("Confirm", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO
                    }
                });

                snackbar.show();
            }
        });
    }
}