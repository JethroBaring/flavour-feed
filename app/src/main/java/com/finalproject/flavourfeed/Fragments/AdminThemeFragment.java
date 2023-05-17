package com.finalproject.flavourfeed.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.finalproject.flavourfeed.R;

public class AdminThemeFragment extends Fragment {
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_theme_fragment, container, false);
        RadioGroup themes = view.findViewById(R.id.radioGroup);
        sharedPreferences = getActivity().getSharedPreferences("theme", Context.MODE_PRIVATE);
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
        return view;
    }


    private void saveThemePreference(int themeValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", themeValue);
        editor.apply();
    }
}