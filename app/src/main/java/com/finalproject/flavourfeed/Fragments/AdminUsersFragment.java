package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.R;
import com.google.android.material.tabs.TabLayout;


public class AdminUsersFragment extends Fragment {
    ActiveUsersFragment activeUsersFragment = new ActiveUsersFragment();
    BannedUsersFragment bannedUsersFragment = new BannedUsersFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_users_fragment, container, false);
        TabLayout tabLayout = view.findViewById(R.id.adminUsersTabLayout);
        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer,activeUsersFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer,activeUsersFragment).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer,bannedUsersFragment).commit();
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
        return view;
    }
}