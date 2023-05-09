package com.finalproject.flavourfeed.Pages;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.finalproject.flavourfeed.Fragments.HomeFragment;
import com.finalproject.flavourfeed.Fragments.NotificationFragment;
import com.finalproject.flavourfeed.Fragments.MarketplaceFragment;
import com.finalproject.flavourfeed.Fragments.ProfileFragment;
import com.finalproject.flavourfeed.NewProfileFragment;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    MarketplaceFragment marketplaceFragment = new MarketplaceFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    SearchFragment searchFragment = new SearchFragment();

    NewProfileFragment newProfileFragment = new NewProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        bottomNavigationView = findViewById(R.id.bottomNav);
        if (getIntent().getBooleanExtra("fromSignUp", false) || getIntent().getBooleanExtra("fromUpdateProfile", false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.profilePage);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homePage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                        break;
                    case R.id.marketPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, marketplaceFragment).commit();
                        break;
                    case R.id.searchPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, searchFragment).commit();
                        break;
                    case R.id.notificationPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, notificationFragment).commit();
                        break;
                    case R.id.profilePage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, newProfileFragment).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                        break;
                }
                return true;
            }
        });


    }
}