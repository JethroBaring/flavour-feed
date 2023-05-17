package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.TopProductsFragment;
import com.finalproject.flavourfeed.TopSpenderFragment;
import com.finalproject.flavourfeed.Utitilies.CreateChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MarketDashboardFragment extends Fragment {
    TopSpenderFragment topSpenderFragment = new TopSpenderFragment();
    TopProductsFragment topProductsFragment = new TopProductsFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_dashboard_fragment, container, false);
        LineChart salesStatisticsChart = view.findViewById(R.id.salesStatisticsChart);
        LineChart ordersChart = view.findViewById(R.id.ordersChart);
        LineChart earningsChart = view.findViewById(R.id.earningsChart);
        TextView orders = view.findViewById(R.id.orders);
        TextView earnings = view.findViewById(R.id.earnings);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference salesStatisticsRef = db.collection("allTransactions");
        CollectionReference ordersRef = db.collection("orderInformation");
        CollectionReference earningsRef = db.collection("earnings");
        CreateChart.createChart(salesStatisticsChart, salesStatisticsRef);
        CreateChart.createChart(ordersChart, ordersRef);
        CreateChart.createChart(earningsChart, earningsRef);
        TabLayout market = view.findViewById(R.id.market);

        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, topProductsFragment).commit();
        market.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, topProductsFragment).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.tabContainer, topSpenderFragment).commit();
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

        CollectionReference collection = db.collection("orderInformation");
        AggregateQuery countQuery = collection.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                orders.setText(Integer.toString((int) snapshot.getCount()));
            }
        });

        db.collection("earnings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    int total = 0;
                    for (QueryDocumentSnapshot v : value) {
                        total += v.getLong("earning").intValue();
                    }
                    earnings.setText(String.format("%,d", total));
                }
            }
        });


        return view;
    }
}