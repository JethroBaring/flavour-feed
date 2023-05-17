package com.finalproject.flavourfeed.Pages;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.finalproject.flavourfeed.Fragments.HomeFragment;
import com.finalproject.flavourfeed.Fragments.NotificationFragment;
import com.finalproject.flavourfeed.Fragments.MarketplaceFragment;
import com.finalproject.flavourfeed.Fragments.NewProfileFragment;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    MarketplaceFragment marketplaceFragment = new MarketplaceFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    SearchFragment searchFragment = new SearchFragment();
    NewProfileFragment newProfileFragment = new NewProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        bottomNavigationView = findViewById(R.id.bottomNav);
        if (getIntent().getBooleanExtra("fromSignUp", false) || getIntent().getBooleanExtra("fromUpdateProfile", false)) {
            changeFragment(newProfileFragment);
            bottomNavigationView.setSelectedItemId(R.id.profilePage);
        } else {
            changeFragment(homeFragment);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homePage:
                        changeFragment(homeFragment);
                        break;
                    case R.id.marketPage:
                        changeFragment(marketplaceFragment);
                        break;
                    case R.id.searchPage:
                        changeFragment(searchFragment);
                        break;
                    case R.id.notificationPage:
                        changeFragment(notificationFragment);
                        break;
                    case R.id.profilePage:
                        changeFragment(newProfileFragment);
                        break;
                }
                return true;
            }
        });
    }

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}