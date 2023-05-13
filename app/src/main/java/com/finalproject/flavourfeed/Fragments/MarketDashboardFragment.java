package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.CreateChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MarketDashboardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_dashboard_fragment, container, false);
        LineChart salesStatisticsChart = view.findViewById(R.id.salesStatisticsChart);
        LineChart ordersChart = view.findViewById(R.id.ordersChart);
        LineChart earningsChart = view.findViewById(R.id.earningsChart);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference salesStatisticsRef = db.collection("purchaseHistoryInformation");
        CollectionReference ordersRef = db.collection("orderInformation");
        CollectionReference earningsRef = db.collection("earningsInformation");
        CreateChart.createChart(salesStatisticsChart, salesStatisticsRef);
        CreateChart.createChart(ordersChart, ordersRef);
        CreateChart.createChart(earningsChart, earningsRef);
        return view;
    }
}