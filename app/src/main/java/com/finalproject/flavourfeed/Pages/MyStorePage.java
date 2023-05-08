package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Fragments.StoreDashboardFragment;
import com.finalproject.flavourfeed.Fragments.StoreProductsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MyStorePage extends AppCompatActivity {

    StoreDashboardFragment storeDashboardFragment = new StoreDashboardFragment();
    StoreProductsFragment storeProductsFragment = new StoreProductsFragment();
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_store_page);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FrameLayout storeFragmentContainer = findViewById(R.id.storeFragmentContainer);
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView storeName = findViewById(R.id.storeName);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.storeFragmentContainer, storeDashboardFragment).commit();


        storeName.setText(user.getDisplayName()+"'s store");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStorePage.super.onBackPressed();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.storeDashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.storeFragmentContainer, storeDashboardFragment).commit();
                        break;
                    case R.id.storeProducts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.storeFragmentContainer, storeProductsFragment).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.storeFragmentContainer, storeDashboardFragment).commit();
                        break;
                }
                return true;
            }
        });
    }
}