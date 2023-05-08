package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.UserCompletedFragment;
import com.finalproject.flavourfeed.UserInProgressFragment;
import com.finalproject.flavourfeed.UserPendingFragment;
import com.google.android.material.tabs.TabLayout;

public class UserOrderPage extends AppCompatActivity {

    UserPendingFragment userPendingFragment = new UserPendingFragment();
    UserInProgressFragment userInProgressFragment = new UserInProgressFragment();
    UserCompletedFragment userCompletedFragment = new UserCompletedFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_page);
        TabLayout userOrderTabLayout = findViewById(R.id.userOrderTabLayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.tabContainer, userPendingFragment).commit();
        userOrderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tabContainer, userPendingFragment).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tabContainer, userInProgressFragment).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.tabContainer, userCompletedFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}