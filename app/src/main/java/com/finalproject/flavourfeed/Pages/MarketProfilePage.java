package com.finalproject.flavourfeed.Pages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.TransactionAdapter;
import com.finalproject.flavourfeed.Models.TransactionModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MarketProfilePage extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    RecyclerView transactionsRecyclerView;
    ArrayList<TransactionModel> transactions;
    TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_profile_page);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        transactionAdapter = new TransactionAdapter(TransactionModel.itemCallback);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsRecyclerView.setAdapter(transactionAdapter);

        LinearLayout userPending = findViewById(R.id.userPending);
        LinearLayout userInProgress = findViewById(R.id.userInProgress);
        LinearLayout userCompleted = findViewById(R.id.userCompleted);
        LinearLayout addFunds = findViewById(R.id.funds);
        LinearLayout upgradeCard = findViewById(R.id.upgradecard);
        TextView balance = findViewById(R.id.balance);
        TextView displayName = findViewById(R.id.displayName);
        ImageView card = findViewById(R.id.card);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        getAllData();

        db.collection("userInformation").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            int b = documentSnapshot.getLong("balance").intValue();
            balance.setText(String.format("%,d", b));
            if (documentSnapshot.getString("card").equals("gold")) {
                card.setImageResource(R.drawable.newcreditcardgold);
            } else if (documentSnapshot.getString("card").equals("black")) {
                card.setImageResource(R.drawable.newcreditcardblack);
            } else {
                card.setImageResource(R.drawable.newcreditcardblue);
            }
        });

        displayName.setText(user.getDisplayName().toUpperCase());

        addFunds.setOnClickListener(this::onClick);
        upgradeCard.setOnClickListener(this::onClick);
        userPending.setOnClickListener(this::onClick);
        userInProgress.setOnClickListener(this::onClick);
        userCompleted.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userPending:
                goTo(UserOrderPage.class, 1);
                break;
            case R.id.userInProgress:
                goTo(UserOrderPage.class, 2);
                break;
            case R.id.userCompleted:
                goTo(UserOrderPage.class, 3);
                break;
            case R.id.funds:
                startActivity(new Intent(getApplicationContext(), AddFundsPage.class));
                break;
            case R.id.upgradecard:
                startActivity(new Intent(getApplicationContext(), UpgradeCardPage.class));
                break;
        }
    }

    public void goTo(Class<?> destinationClass, int n) {
        Intent intent = new Intent(getApplicationContext(), destinationClass);
        intent.putExtra("tab", n);
        startActivity(intent);
    }

    public void getAllData() {
        db.collection("allTransactions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    transactions = new ArrayList<>();
                    for (QueryDocumentSnapshot v : value) {
                        if (user.getUid().equals(v.getString("userId"))) {
                            TransactionModel t = v.toObject(TransactionModel.class);
                            transactions.add(t);
                        }
                    }
                    transactionAdapter.submitList(transactions);
                }
            }
        });
    }
}