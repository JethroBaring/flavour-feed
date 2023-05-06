package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.CartPage;
import com.finalproject.flavourfeed.MyProductAdapter;
import com.finalproject.flavourfeed.MyStorePage;
import com.finalproject.flavourfeed.ProductModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MarketplaceFragment extends Fragment {

    ImageView btnOption;
    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseFirestore db;
    ArrayList<ProductModel> allProducts;
    RecyclerView productRecyclerView;

    MyProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new MyProductAdapter(ProductModel.itemCallback);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        allProducts = new ArrayList<>();
        ImageView btnCart = view.findViewById(R.id.btnCart);
        ImageView btnOrder = view.findViewById(R.id.btnOrder);
        FloatingActionButton btnMyStore = view.findViewById(R.id.btnMyStore);
        getAllData();

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartPage.class);
                startActivity(intent);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnMyStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyStorePage.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getAllData() {

        db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null) {
                    List<ProductModel> data = value.toObjects(ProductModel.class);
                    allProducts.addAll(data);
                    productAdapter.submitList(allProducts);
                }
            }
        });
    }

}
