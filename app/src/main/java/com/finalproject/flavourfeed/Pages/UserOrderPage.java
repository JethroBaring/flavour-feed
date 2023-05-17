package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Fragments.UserCompletedFragment;
import com.finalproject.flavourfeed.Fragments.UserInProgressFragment;
import com.finalproject.flavourfeed.Fragments.UserPendingFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
        int tab = getIntent().getIntExtra("tab", 1);
        if (tab == 1) {
            userOrderTabLayout.selectTab(userOrderTabLayout.getTabAt(0));
            changeFragment(userPendingFragment);
        } else if (tab == 2) {
            userOrderTabLayout.selectTab(userOrderTabLayout.getTabAt(1));
            changeFragment(userInProgressFragment);
        } else {
            userOrderTabLayout.selectTab(userOrderTabLayout.getTabAt(2));
            changeFragment(userCompletedFragment);
        }
        userOrderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        changeFragment(userPendingFragment);
                        break;
                    case 1:
                        changeFragment(userInProgressFragment);
                        break;
                    case 2:
                        changeFragment(userCompletedFragment);
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

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.tabContainer, fragment).commit();
    }
}