package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.MyProductAdapter;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.Pages.AddProductPage;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
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

public class StoreProductsFragment extends Fragment {

    RecyclerView myProductsRecyclerView;
    MyProductAdapter myProductAdapter;

    ArrayList<ProductModel> myProducts;

    FirebaseFirestore db;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_products_fragment, container, false);
        FloatingActionButton addProduct = view.findViewById(R.id.addProduct);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myProducts = new ArrayList<>();
        myProductsRecyclerView = view.findViewById(R.id.myProductsRecyclerView);
        myProductAdapter = new MyProductAdapter(ProductModel.itemCallback);
        myProductsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        myProductsRecyclerView.setAdapter(myProductAdapter);
        myProductsRecyclerView.setItemAnimator(new NoChangeAnimation());
        getAllData();
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductPage.class);
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
                    myProducts.addAll(data);
                    myProductAdapter.submitList(myProducts);
                }
            }
        });
    }
}