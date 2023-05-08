package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        TextView txtTotal = findViewById(R.id.overallTotal);
        MaterialCardView btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        int total = 0;

        for (CartItemModel cartItemModel : myList) {
            total+=(cartItemModel.getPrice()*cartItemModel.getQuantity());
        }
        txtTotal.setText(Integer.toString(total));

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newOrder = new HashMap<>();
                newOrder.put("status", OrderModel.PENDING);
                newOrder.put("buyerId", user.getUid());
                newOrder.put("orderTotal", Integer.parseInt(txtTotal.getText().toString()));
                newOrder.put("orderItems", myList);
                newOrder.put("delivered", false);
                db.collection("orderInformation").add(newOrder).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            String orderId  = documentReference.getId();
                            db.collection("orderInformation").document(orderId).update("orderId", orderId);
                        }
                    }
                });
            }
        });


    }

}