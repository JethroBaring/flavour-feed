package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductPage extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ImageView productPicture = findViewById(R.id.productPicture);
        ImageView sellerProfile = findViewById(R.id.sellerProfile);
        TextView storeName = findViewById(R.id.storeName);
        TextView productName = findViewById(R.id.productName);
        TextView productPrice = findViewById(R.id.productPrice);
        TextView productCategory = findViewById(R.id.productCategory);
        EditText quantity = findViewById(R.id.quantity);
        MaterialCardView btnAddToCart = findViewById(R.id.btnAddToCart);
        String productId = getIntent().getStringExtra("productId");
        String sellerId = getIntent().getStringExtra("sellerId");
        ImageView decrementQuantity = findViewById(R.id.decrementQuantity);
        ImageView incrementQuantity = findViewById(R.id.incrementQuantity);


        decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = Integer.parseInt(quantity.getText().toString()) - 1;
                if (newQuantity >= 1)
                    quantity.setText(Integer.toString(newQuantity));
            }
        });

        incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = Integer.parseInt(quantity.getText().toString()) + 1;

                quantity.setText(Integer.toString(newQuantity));

            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newCartItem = new HashMap<>();
                newCartItem.put("sellerId", sellerId);
                newCartItem.put("productId", productId);
                newCartItem.put("quantity", Integer.parseInt(quantity.getText().toString()));
                db.collection("userInformation").document(user.getUid()).collection("cart").add(newCartItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            String cartItemId = documentReference.getId();
                            db.collection("allProducts").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot snapshot = task.getResult();
                                        db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemId).update("price", snapshot.getLong("price").intValue());
                                    }
                                }
                            });
                            db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemId).update("cartItemId", cartItemId);
                            Toast.makeText(getApplicationContext(), "Added to cart.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        db.collection("allProducts").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Glide.with(getApplicationContext()).load(documentSnapshot.getString("photoUrl")).into(productPicture);
                    productName.setText(documentSnapshot.getString("name"));
                    productPrice.setText(Integer.toString(documentSnapshot.getLong("price").intValue()));
                    productCategory.setText(documentSnapshot.getString("category"));
                }
            }
        });

        db.collection("userInformation").document(sellerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Glide.with(getApplicationContext()).load(documentSnapshot.getString("profileUrl")).into(sellerProfile);
                    storeName.setText(documentSnapshot.getString("displayName"));
                }
            }
        });

    }
}