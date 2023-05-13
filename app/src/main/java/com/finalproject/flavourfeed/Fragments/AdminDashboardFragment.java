package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.R;
import com.google.android.material.tabs.TabLayout;

public class AdminDashboardFragment extends Fragment {

    SocialDashboardFragment socialDashboardFragment = new SocialDashboardFragment();
    MarketDashboardFragment marketDashboardFragment = new MarketDashboardFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_dashboard_fragment, container, false);
        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, socialDashboardFragment).commit();
        TabLayout tabLayout = view.findViewById(R.id.adminDashboardLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, socialDashboardFragment).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, marketDashboardFragment).commit();
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