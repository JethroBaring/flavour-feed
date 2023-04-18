package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.finalproject.flavourfeed.R;

public class ThemePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_page);
        RadioGroup themes = findViewById(R.id.radioGroup);

        themes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.lightTheme:
                        //setTheme(R.style.Theme_FlavourFeedLight);
                        break;
                    case R.id.darkTheme:
                       // setTheme(R.style.Theme_FlavourFeedDark);
                        break;
                    case R.id.tokyoNightTheme:
                    case R.id.synthWaveTheme:
                    case R.id.deepOceanTheme:
                }
            }
        });
    }
}