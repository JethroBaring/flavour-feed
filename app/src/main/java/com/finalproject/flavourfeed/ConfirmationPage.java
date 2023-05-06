package com.finalproject.flavourfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ConfirmationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page);
        ArrayList<CartItemModel> myList = (ArrayList<CartItemModel>) getIntent().getSerializableExtra("orderList");
        RecyclerView orderRecyclerview = findViewById(R.id.orderRecyclerView);
        ConfirmationAdapter confirmationAdapter = new ConfirmationAdapter(this, myList);
        orderRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerview.setAdapter(confirmationAdapter);
    }
}