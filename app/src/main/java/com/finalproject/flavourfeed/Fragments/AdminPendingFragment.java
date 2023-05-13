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

public class AdminPendingFragment extends Fragment {
    FirebaseFirestore db;
    RecyclerView adminPendingRecyclerView;
    AdminOrderAdapter adminOrderAdapter;
    ArrayList<OrderModel> pendings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_pending_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        adminPendingRecyclerView = view.findViewById(R.id.adminPendingRecyclerView);
        adminOrderAdapter = new AdminOrderAdapter(OrderModel.itemCallback, 1);
        adminPendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adminPendingRecyclerView.setAdapter(adminOrderAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("orderInformation").whereEqualTo("status",OrderModel.PENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<OrderModel> data = value.toObjects(OrderModel.class);
                pendings = new ArrayList<>();
                pendings.addAll(data);
                adminOrderAdapter.submitList(pendings);
            }
        });
    }
}