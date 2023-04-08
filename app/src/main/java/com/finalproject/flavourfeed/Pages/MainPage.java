package com.finalproject.flavourfeed.Pages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.finalproject.flavourfeed.Fragments.HomeFragment;
import com.finalproject.flavourfeed.Fragments.MarketplaceFragment;
import com.finalproject.flavourfeed.Fragments.ProfileFragment;
import com.finalproject.flavourfeed.GradientText;
import com.finalproject.flavourfeed.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    MarketplaceFragment marketplaceFragment = new MarketplaceFragment();
    ProfileFragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        TextView mainPageAppName = findViewById(R.id.mainPageAppName);
        ImageView mode = findViewById(R.id.lightmode);
        GradientText.setTextViewColor(mainPageAppName, ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.pink));
        bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
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
                    case R.id.profilePage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                        break;
                }
                return true;
            }
        });

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);

                if (mode.getId() == R.id.lightmode) {
                    mode.setImageResource(R.drawable.darkmodeicon);
                    mode.setId(R.id.darkmode);
                } else {
                    mode.setImageResource(R.drawable.lightmodeicon);
                    mode.setId(R.id.lightmode);

                }
                mode.startAnimation(animation);

            }
        });

    }
}