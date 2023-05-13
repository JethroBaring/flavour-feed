package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.DashboardUserAdapter;
import com.finalproject.flavourfeed.Models.DashboardUserModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TopFollowersFragment extends Fragment {

    FirebaseFirestore db;
    RecyclerView newUsersRecyclerView;
    DashboardUserAdapter dashboardUserAdapter;
    ArrayList<DashboardUserModel> results;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_followers_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        newUsersRecyclerView = view.findViewById(R.id.topFollowersRecyclerView);
        dashboardUserAdapter = new DashboardUserAdapter(DashboardUserModel.itemCallback);
        newUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newUsersRecyclerView.setAdapter(dashboardUserAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("userInformation").orderBy("followers", Query.Direction.DESCENDING).limit(3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DashboardUserModel> data = value.toObjects(DashboardUserModel.class);
                results = new ArrayList<>();
                results.addAll(data);
                dashboardUserAdapter.submitList(results);
            }
        });
    }
}