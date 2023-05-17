package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.CreateChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class SocialDashboardFragment extends Fragment {
    NewUsersFragment newUsersFragment = new NewUsersFragment();
    TopFollowersFragment topFollowersFragment = new TopFollowersFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.social_dashboard_fragment, container, false);

        LineChart monthlyUsersChart = view.findViewById(R.id.monthlyUsersChart);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TabLayout newusers = view.findViewById(R.id.newusers);
        CollectionReference usersRef = db.collection("userInformation");
        CreateChart.createChart(monthlyUsersChart, usersRef);
        TextView usersToday = view.findViewById(R.id.usersToday);
        TextView totalPosts = view.findViewById(R.id.totalPosts);
        TextView totalUsers = view.findViewById(R.id.totalUsers);


        CollectionReference coll = db.collection("userInformation");
        AggregateQuery cQuery = coll.count();
        cQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                totalUsers.setText(Integer.toString((int) snapshot.getCount()));
            }
        });

        CollectionReference collection = db.collection("postInformation");
        AggregateQuery countQuery = collection.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                totalPosts.setText(Integer.toString((int) snapshot.getCount()));
            }
        });

        CollectionReference usersTodayRef = db.collection("userInformation");

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();

        Query query = usersTodayRef.whereGreaterThanOrEqualTo("timestamp", startOfDay).whereLessThan("timestamp", today);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int userCount = queryDocumentSnapshots.size();
                usersToday.setText(Integer.toString(userCount));

            }
        });

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
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }
}