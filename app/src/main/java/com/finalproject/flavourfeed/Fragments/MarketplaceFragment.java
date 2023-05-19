package com.finalproject.flavourfeed.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.Pages.MarketProfilePage;
import com.finalproject.flavourfeed.Pages.CartPage;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.Pages.ProductPage;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    LinearLayout allCategory;
    LinearLayout mainCategory;
    LinearLayout saladCategory;
    LinearLayout burgerCategory;
    LinearLayout drinkCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        productRecyclerView = view.findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(ProductModel.itemCallback, this);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
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
        EditText search = view.findViewById(R.id.search);
        allCategory.setOnClickListener(this::onClick);
        mainCategory.setOnClickListener(this::onClick);
        saladCategory.setOnClickListener(this::onClick);
        burgerCategory.setOnClickListener(this::onClick);
        drinkCategory.setOnClickListener(this::onClick);

        getCategoryData("All");
        CollectionReference productsRef = db.collection("allProducts");
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    String toBeSearch = search.getText().toString();
                    if (!toBeSearch.isEmpty()) {

                        Query query = productsRef.orderBy("name").startAt(toBeSearch).endAt(toBeSearch + "\uf8ff").limit(10);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    allProducts = new ArrayList<>();
                                    allProducts.clear();
                                    productAdapter.notifyDataSetChanged();
                                    List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                                    allProducts.addAll(data);
                                    productAdapter.submitList(allProducts);
                                }
                            }
                        });
                    }
                }
                return false;
            }
        });
        btnCart.setOnClickListener(v -> startActivity(new Intent(getActivity(), CartPage.class)));
        btnOrder.setOnClickListener(v -> startActivity(new Intent(getActivity(), MarketProfilePage.class)));

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
        if (category.equals("All")) {
            db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        List<ProductModel> data = value.toObjects(ProductModel.class);
                        allProducts = new ArrayList<>();
                        allProducts.clear();
                        productAdapter.notifyDataSetChanged();
                        allProducts.addAll(data);
                        productAdapter.submitList(allProducts);

                    }
                }
            });
        } else {
            db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        allProducts = new ArrayList<>();
                        allProducts.clear();
                        productAdapter.notifyDataSetChanged();
                        for (DocumentSnapshot p : value) {
                            ProductModel product = p.toObject(ProductModel.class);
                            if (product.getCategory().equals(category)) {
                                allProducts.add(product);
                            }
                        }
                        productAdapter.submitList(allProducts);

                    }
                }
            });
        }


    }

    @Override
    public void onProductClick(String sellerId, String productId) {
        Intent intent = new Intent(getActivity(), ProductPage.class);
        intent.putExtra("productId", productId);
        intent.putExtra("sellerId", sellerId);
        startActivity(intent);
    }
}
