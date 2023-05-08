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

import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.Pages.CartPage;
import com.finalproject.flavourfeed.Pages.MyStorePage;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.Pages.ProductPage;
import com.finalproject.flavourfeed.Pages.UserOrderPage;
import com.finalproject.flavourfeed.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MarketplaceFragment extends Fragment implements ProductAdapter.ProductClickInterface {

    ImageView btnOption;
    FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseFirestore db;
    ArrayList<ProductModel> allProducts;
    RecyclerView productRecyclerView;

    ProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(ProductModel.itemCallback, this);
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
                startActivity(new Intent(getContext(), UserOrderPage.class));
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


    @Override
    public void onProductClick(String sellerId, String productId) {
        Intent intent = new Intent(getActivity(), ProductPage.class);
        intent.putExtra("productId", productId);
        intent.putExtra("sellerId", sellerId);
        startActivity(intent);
    }
}
