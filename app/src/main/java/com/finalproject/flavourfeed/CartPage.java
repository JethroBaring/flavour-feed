package com.finalproject.flavourfeed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.finalproject.flavourfeed.Models.CommentModel;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class CartPage extends AppCompatActivity implements CartAdapter.CartCheckInterface {
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ArrayList<CartItemModel> cartItems;
    RecyclerView cartRecyclerView;

    CartAdapter cartAdapter;

    ArrayList<CartItemModel> toBeCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartAdapter = new CartAdapter(CartItemModel.itemCallback, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setItemAnimator(new NoChangeAnimation());
        MaterialCardView btnCheckOut = findViewById(R.id.btnCheckOut);
        toBeCheckOut = new ArrayList<>();
        getAllData();

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmationPage.class);
                intent.putExtra("orderList", toBeCheckOut);
                startActivity(intent);
            }
        });
    }

    public void getAllData() {
        db.collection("userInformation").document(user.getUid()).collection("cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    List<CartItemModel> data = value.toObjects(CartItemModel.class);
                    cartItems = new ArrayList<>();
                    cartItems.addAll(data);
                    cartAdapter.submitList(cartItems);
                }
            }
        });
    }

    @Override
    public void onItemCheck(CheckBox checkBox, CartItemModel cartItemModel) {
        toBeCheckOut.clear();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    toBeCheckOut.add(cartItemModel);
                } else {
                    toBeCheckOut.remove(cartItemModel);
                }
            }
        });
    }
}