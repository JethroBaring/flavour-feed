package com.finalproject.flavourfeed.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.MyProductAdapter;
import com.finalproject.flavourfeed.Adapters.SearchAdapter;
import com.finalproject.flavourfeed.Models.ProductModel;
import com.finalproject.flavourfeed.Models.ResultModel;
import com.finalproject.flavourfeed.Pages.AddProductPage;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class AdminProductsFragment extends Fragment {
    RecyclerView myProductsRecyclerView;
    MyProductAdapter myProductAdapter;

    ArrayList<ProductModel> myProducts;

    FirebaseFirestore db;
    FirebaseUser user;
    LinearLayout allCategory;
    LinearLayout mainCategory;
    LinearLayout saladCategory;
    LinearLayout burgerCategory;
    LinearLayout drinkCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_products_fragment, container, false);
        FloatingActionButton addProduct = view.findViewById(R.id.addProduct);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        EditText search = view.findViewById(R.id.search);
        myProductsRecyclerView = view.findViewById(R.id.myProductsRecyclerView);
        myProductAdapter = new MyProductAdapter(ProductModel.itemCallback);
        myProductsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        myProductsRecyclerView.setAdapter(myProductAdapter);
        myProductsRecyclerView.setItemAnimator(new NoChangeAnimation());
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
                                    myProducts = new ArrayList<>();
                                    myProducts.clear();
                                    myProductAdapter.notifyDataSetChanged();
                                    List<ProductModel> data = task.getResult().toObjects(ProductModel.class);
                                    myProducts.addAll(data);
                                    myProductAdapter.submitList(myProducts);
                                }
                            }
                        });
                    }
                }
                return false;
            }
        });



        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductPage.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void getCategoryData(String category) {
        if (category.equals("All")) {
            db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        List<ProductModel> data = value.toObjects(ProductModel.class);
                        myProducts = new ArrayList<>();
                        myProducts.clear();
                        myProductAdapter.notifyDataSetChanged();
                        myProducts.addAll(data);
                        myProductAdapter.submitList(myProducts);

                    }
                }
            });
        } else {
            db.collection("allProducts").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        myProducts = new ArrayList<>();
                        myProducts.clear();
                        myProductAdapter.notifyDataSetChanged();
                        for (DocumentSnapshot p : value) {
                            ProductModel product = p.toObject(ProductModel.class);
                            if (product.getCategory().equals(category)) {
                                myProducts.add(product);
                            }
                        }
                        myProductAdapter.submitList(myProducts);

                    }
                }
            });
        }


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
}