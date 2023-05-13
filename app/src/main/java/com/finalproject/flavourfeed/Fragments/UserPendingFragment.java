package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.UserOrderAdapter;
import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserPendingFragment extends Fragment {
    FirebaseUser user;
    FirebaseAuth mAtuh;
    FirebaseFirestore db;
    RecyclerView userPendingRecyclerView;
    UserOrderAdapter userPendingAdapter;
    ArrayList<OrderModel> pendings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_pending_fragment, container, false);
        mAtuh = FirebaseAuth.getInstance();
        user = mAtuh.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userPendingRecyclerView = view.findViewById(R.id.userPendingRecyclerView);
        userPendingAdapter = new UserOrderAdapter(OrderModel.itemCallback, false);
        userPendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userPendingRecyclerView.setAdapter(userPendingAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("orderInformation").whereEqualTo("status", OrderModel.PENDING).whereEqualTo("buyerId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<OrderModel> data = value.toObjects(OrderModel.class);
                pendings = new ArrayList<>();
                pendings.addAll(data);
                userPendingAdapter.submitList(pendings);
            }
        });
    }
}