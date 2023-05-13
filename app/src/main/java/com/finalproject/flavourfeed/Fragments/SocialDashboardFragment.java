package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.CreateChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SocialDashboardFragment extends Fragment {
    NewUsersFragment newUsersFragment = new NewUsersFragment();
    TopFollowersFragment topFollowersFragment = new TopFollowersFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_dashboard_fragment, container, false);

        LineChart monthlyUsersChart = view.findViewById(R.id.monthlyUsersChart);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TabLayout newusers = view.findViewById(R.id.newusers);
        CollectionReference usersRef = db.collection("commentInformation");
        CreateChart.createChart(monthlyUsersChart, usersRef);
        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, newUsersFragment).commit();

        newusers.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, newUsersFragment).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, topFollowersFragment).commit();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;
    }
}