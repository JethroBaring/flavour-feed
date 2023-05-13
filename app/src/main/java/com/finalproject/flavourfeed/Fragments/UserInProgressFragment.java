package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Adapters.UserOrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserInProgressFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    RecyclerView userInProgressRecyclerView;
    ArrayList<OrderModel> inprogress;
    UserOrderAdapter userOrderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_in_progress_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userInProgressRecyclerView = view.findViewById(R.id.userInProgressRecyclerView);
        userOrderAdapter = new UserOrderAdapter(OrderModel.itemCallback, true);
        userInProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userInProgressRecyclerView.setAdapter(userOrderAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("orderInformation").whereEqualTo("status", OrderModel.IN_PROGRESS).whereEqualTo("buyerId", user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<OrderModel> data = value.toObjects(OrderModel.class);
                inprogress = new ArrayList<>();
                inprogress.addAll(data);
                userOrderAdapter.submitList(inprogress);

            }
        });

    }
}