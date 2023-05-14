package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.finalproject.flavourfeed.R;

public class ThemePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FlavourFeedDark);
        setContentView(R.layout.theme_page);
        final boolean [] isThemeSet = {false};

        RadioGroup themes = findViewById(R.id.radioGroup);
        themes.check(R.id.lightTheme);

        themes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.lightTheme:
                        if (!isThemeSet[0]) {
                            setTheme(R.style.Theme_FlavourFeed);
                            isThemeSet[0] = true;
                        }
                        break;
                    case R.id.darkTheme:
                        if (!isThemeSet[0]) {
                            setTheme(R.style.Theme_FlavourFeedDark);
                            isThemeSet[0] = true;
                        }
                        break;
                }
            }
        });
    }
}