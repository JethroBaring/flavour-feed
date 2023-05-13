package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.AdminOrderAdapter;
import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdminCompletedFragment extends Fragment {
    FirebaseFirestore db;
    RecyclerView adminCompletedRecyclerView;
    AdminOrderAdapter adminOrderAdapter;
    ArrayList<OrderModel> orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_completed_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        adminCompletedRecyclerView = view.findViewById(R.id.adminCompletedRecyclerView);
        adminOrderAdapter = new AdminOrderAdapter(OrderModel.itemCallback, 0);
        adminCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adminCompletedRecyclerView.setAdapter(adminOrderAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("orderInformation").whereEqualTo("status",OrderModel.COMPLETED).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<OrderModel> data = value.toObjects(OrderModel.class);
                orders = new ArrayList<>();
                orders.addAll(data);
                adminOrderAdapter.submitList(orders);
            }
        });
    }
}