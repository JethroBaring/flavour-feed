package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ThemePage extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_page);

        RadioGroup themes = findViewById(R.id.radioGroup);
        sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        if(sharedPreferences.getInt("theme",1) == 1) {
            themes.check(R.id.lightTheme);
        } else {
            themes.check(R.id.darkTheme);
        }


        themes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.lightTheme:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        saveThemePreference(1);
                        break;
                    case R.id.darkTheme:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveThemePreference(2);
                        break;
                }
            }
        });
    }

    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void saveThemePreference(int themeValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", themeValue);
        editor.apply();
    }

}