package com.finalproject.flavourfeed.Utitilies;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateChart {
    public static void createChart(LineChart lineChart, CollectionReference collectionReference) {
        String[] daysOfMonth = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        };
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date endOfMonth = cal.getTime();
        List<Entry> entries = new ArrayList<>();

        Query query = collectionReference.whereGreaterThanOrEqualTo("timestamp", startOfMonth).whereLessThanOrEqualTo("timestamp", endOfMonth);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> userList = queryDocumentSnapshots.getDocuments();
                // Do something with the list of users
                for (int i = 1; i <= 31; i++) {
                    int count = 0;
                    for (DocumentSnapshot user : userList) {
                        Timestamp timestamp = user.getTimestamp("timestamp");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timestamp.toDate());
                        if (cal.get(Calendar.DAY_OF_MONTH) == i) {
                            count++;
                        }
                    }
                    entries.add(new Entry(i, count));
                }
                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setDrawValues(false);
                dataSet.setDrawCircles(false);
                dataSet.setLineWidth(3);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                leftAxis.setDrawAxisLine(false);
                rightAxis.setDrawAxisLine(false);
                rightAxis.setDrawLabels(false);
                leftAxis.setDrawLabels(false);
                xAxis.setDrawLabels(false);
                leftAxis.setDrawGridLines(false);
                rightAxis.setDrawGridLines(false);
                lineChart.getDescription().setEnabled(false);
                lineChart.setData(new LineData(dataSet));
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(daysOfMonth));
                lineChart.setDragEnabled(false);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setTouchEnabled(false);
                lineChart.setDrawBorders(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.invalidate();
            }
        });
    }
}
