package com.finalproject.flavourfeed;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.TopSpenderAdapter;
import com.finalproject.flavourfeed.Models.TopSpenderModel;
import com.finalproject.flavourfeed.Models.TransactionModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TopSpenderFragment extends Fragment {
    FirebaseFirestore db;
    RecyclerView topSpenderRecyclerView;
    ArrayList<TopSpenderModel> topSpenderModels;
    TopSpenderAdapter topSpenderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_spender_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        topSpenderRecyclerView = view.findViewById(R.id.topSpenderRecyclerView);
        topSpenderAdapter = new TopSpenderAdapter(TopSpenderModel.itemCallback);
        topSpenderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topSpenderRecyclerView.setAdapter(topSpenderAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("userInformation").orderBy("totalSpend", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    List<TopSpenderModel> data= value.toObjects(TopSpenderModel.class);
                    topSpenderModels = new ArrayList<>();
                    topSpenderModels.addAll(data);
                    topSpenderAdapter.submitList(topSpenderModels);
                }
            }
        });
    }
}