package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.flavourfeed.Models.CartItemModel;
import com.finalproject.flavourfeed.Adapters.ConfirmationAdapter;
import com.finalproject.flavourfeed.Models.OrderModel;
import com.finalproject.flavourfeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationPage extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<CartItemModel> myList = (ArrayList<CartItemModel>) getIntent().getSerializableExtra("orderList");
        RecyclerView orderRecyclerview = findViewById(R.id.orderRecyclerView);
        ConfirmationAdapter confirmationAdapter = new ConfirmationAdapter(this, myList);
        orderRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerview.setAdapter(confirmationAdapter);
        TextView txtTotal = findViewById(R.id.overAllTotal);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        final int[] total = {0};

        for (CartItemModel cartItemModel : myList) {
            total[0] += (cartItemModel.getPrice() * cartItemModel.getQuantity());
        }
        txtTotal.setText(String.format("%,d", total[0]));

        btnPlaceOrder.setOnClickListener(view -> {
            db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.getLong("balance").intValue() >= total[0]) {
                    for (CartItemModel cartItemModel : myList) {
                        db.collection("userInformation").document(user.getUid()).collection("cart").document(cartItemModel.getCartItemId()).delete();
                    }

                    Map<String, Object> newOrder = new HashMap<>();
                    newOrder.put("status", OrderModel.PENDING);
                    newOrder.put("buyerId", user.getUid());
                    newOrder.put("orderTotal", Integer.parseInt(txtTotal.getText().toString()));
                    newOrder.put("orderItems", myList);
                    newOrder.put("delivered", false);
                    newOrder.put("timestamp", FieldValue.serverTimestamp());
                    db.collection("orderInformation").add(newOrder).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                DocumentReference documentReference = task.getResult();
                                String orderId = documentReference.getId();
                                db.collection("orderInformation").document(orderId).update("orderId", orderId);
                                db.collection("userInformation").document(user.getUid()).update("balance", documentSnapshot.getLong("balance").intValue() - total[0]);
                                startActivity(new Intent(getApplicationContext(), UserOrderPage.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient funds", Toast.LENGTH_SHORT).show();
                }
            });


        });


    }

}