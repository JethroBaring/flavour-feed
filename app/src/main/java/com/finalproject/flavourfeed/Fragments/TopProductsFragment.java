package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.TopProductsAdapter;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TopProductsFragment extends Fragment {
    FirebaseFirestore db;
    RecyclerView topProductsRecyclerView;
    ArrayList<ProductModel> productModels;
    TopProductsAdapter topProductsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_products_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        topProductsRecyclerView = view.findViewById(R.id.topProductsRecyclerView);
        topProductsAdapter = new TopProductsAdapter(ProductModel.itemCallback);
        topProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topProductsRecyclerView.setAdapter(topProductsAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("allProducts").orderBy("sold", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    List<ProductModel> data = value.toObjects(ProductModel.class);
                    productModels = new ArrayList<>();
                    productModels.addAll(data);
                    topProductsAdapter.submitList(productModels);
                }
            }
        });
    }
}