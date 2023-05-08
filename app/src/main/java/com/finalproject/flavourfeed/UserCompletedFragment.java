package com.finalproject.flavourfeed;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UserCompletedFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    RecyclerView userCompletedRecyclerView;
    UserOrderAdapter userOrderAdapter;
    ArrayList<OrderModel> completedOrders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_completed_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userCompletedRecyclerView = view.findViewById(R.id.userCompletedRecyclerView);
        userOrderAdapter = new UserOrderAdapter(OrderModel.itemCallback, false);
        userCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userCompletedRecyclerView.setAdapter(userOrderAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("orderInformation").whereEqualTo("status",OrderModel.COMPLETED).whereEqualTo("buyerId",user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<OrderModel> data = value.toObjects(OrderModel.class);
                completedOrders = new ArrayList<>();
                completedOrders.addAll(data);
                userOrderAdapter.submitList(completedOrders);
            }
        });
    }
}