package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.Pages.CartPage;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.Pages.ProductPage;
import com.finalproject.flavourfeed.Pages.UserOrderPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    ProductAdapter productAdapter;
    LinearLayout allCategory;
    LinearLayout mainCategory;
    LinearLayout saladCategory;
    LinearLayout burgerCategory;
    LinearLayout drinkCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        allProducts = new ArrayList<>();
        ImageView btnCart = view.findViewById(R.id.btnCart);
        ImageView btnOrder = view.findViewById(R.id.btnOrder);
        allCategory = view.findViewById(R.id.allCategory);
        mainCategory = view.findViewById(R.id.mainCategory);
        saladCategory = view.findViewById(R.id.saladCategory);
        burgerCategory = view.findViewById(R.id.burgerCategory);
        drinkCategory = view.findViewById(R.id.drinkCategory);

        allCategory.setOnClickListener(this::onClick);
        mainCategory.setOnClickListener(this::onClick);
        saladCategory.setOnClickListener(this::onClick);
        burgerCategory.setOnClickListener(this::onClick);
        drinkCategory.setOnClickListener(this::onClick);

        getCategoryData("All");
        btnCart.setOnClickListener(v -> startActivity(new Intent(getActivity(), CartPage.class)));
        btnOrder.setOnClickListener(v -> startActivity(new Intent(getActivity(), UserOrderPage.class)));

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allCategory:
                getCategoryData("All");
                allCategory.setBackgroundResource(R.drawable.simplesearch);
                mainCategory.setBackground(null);
                saladCategory.setBackground(null);
                burgerCategory.setBackground(null);
                drinkCategory.setBackground(null);
                break;
            case R.id.mainCategory:
                getCategoryData("Main");
                allCategory.setBackground(null);
                mainCategory.setBackgroundResource(R.drawable.simplesearch);
                saladCategory.setBackground(null);
                burgerCategory.setBackground(null);
                drinkCategory.setBackground(null);
                break;
            case R.id.saladCategory:
                getCategoryData("Salads");
                allCategory.setBackground(null);
                mainCategory.setBackground(null);
                saladCategory.setBackgroundResource(R.drawable.simplesearch);
                burgerCategory.setBackground(null);
                drinkCategory.setBackground(null);
                break;
            case R.id.burgerCategory:
                getCategoryData("Burgers");
                allCategory.setBackground(null);
                mainCategory.setBackground(null);
                saladCategory.setBackground(null);
                burgerCategory.setBackgroundResource(R.drawable.simplesearch);
                drinkCategory.setBackground(null);
                break;
            case R.id.drinkCategory:
                getCategoryData("Drinks");
                allCategory.setBackground(null);
                mainCategory.setBackground(null);
                saladCategory.setBackground(null);
                burgerCategory.setBackground(null);
                drinkCategory.setBackgroundResource(R.drawable.simplesearch);
                break;
        }
    }

    public void getCategoryData(String category) {
        allProducts.clear();
        db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (category.equals("All")) {
                    List<ProductModel> data = queryDocumentSnapshots.toObjects(ProductModel.class);
                    allProducts.addAll(data);
                } else {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        if (queryDocumentSnapshot.getString("category").equals(category)) {
                            allProducts.add(queryDocumentSnapshot.toObject(ProductModel.class));
                        }
                    }

                }
                productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

                productRecyclerView.setAdapter(new ProductAdapter(getContext(), allProducts, new ProductAdapter.ProductClickInterface() {
                    @Override
                    public void onProductClick(String sellerId, String productId) {
                        Intent intent = new Intent(getActivity(), ProductPage.class);
                        intent.putExtra("productId", productId);
                        intent.putExtra("sellerId", sellerId);
                        startActivity(intent);
                    }
                }));
            }
        });
    }

}
