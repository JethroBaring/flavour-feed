package com.finalproject.flavourfeed.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.flavourfeed.Pages.MarketProfilePage;
import com.finalproject.flavourfeed.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpgradeCardPage extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_card_page);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        RadioButton goldcard = findViewById(R.id.goldcard);
        RadioButton blackcard = findViewById(R.id.blackcard);
        TextView total = findViewById(R.id.total);
        MaterialCardView btnUpgrade = findViewById(R.id.btnUpgrade);
        final boolean []gold = {true};
        final int[] price = {5000};
        total.setText(String.format("%,d",5000));
        goldcard.setChecked(true);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        goldcard.setOnClickListener(v -> {
            total.setText(String.format("%,d", 5000));
            price[0] = 3000;
            gold[0] = true;
            goldcard.setChecked(true);
            blackcard.setChecked(false);
        });

        blackcard.setOnClickListener(v -> {
            total.setText(String.format("%,d", 10000));
            price[0] = 10000;
            gold[0] = false;
            blackcard.setChecked(true);
            goldcard.setChecked(false);
        });

        btnUpgrade.setOnClickListener(v -> {
            db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.getLong("balance").intValue() >= price[0]) {
                    if (gold[0]) {
                        db.collection("userInformation").document(user.getUid()).update("card", "gold");
                        db.collection("userInformation").document(user.getUid()).update("balance", documentSnapshot.getLong("balance").intValue() - price[0]);
                    } else {
                        db.collection("userInformation").document(user.getUid()).update("card", "black");
                        db.collection("userInformation").document(user.getUid()).update("balance", documentSnapshot.getLong("balance").intValue() - price[0]);
                    }

                    Map<String, Object> newTransaction = new HashMap<>();
                    newTransaction.put("userId", user.getUid());
                    newTransaction.put("total", price[0]);
                    newTransaction.put("timestamp", FieldValue.serverTimestamp());
                    db.collection("allTransactions").add(newTransaction).addOnSuccessListener(documentReference -> {
                       String id = documentReference.getId();
                       db.collection("allTransactions").document(id).update("transactionId",id);
                    });

                    Toast.makeText(getApplicationContext(),"Card upgraded",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MarketProfilePage.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Insufficient funds", Toast.LENGTH_SHORT).show();

                }
            });
        });
    }
}