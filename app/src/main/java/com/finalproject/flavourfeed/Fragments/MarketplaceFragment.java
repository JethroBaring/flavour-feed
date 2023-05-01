package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MarketplaceFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    ImageView btnOption;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        RecyclerView productRecyclerView = view.findViewById(R.id.productRecyclerView);
        ProductAdapter productAdapter = new ProductAdapter(getContext());
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL, false));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ImageView marketProfilePicture = view.findViewById(R.id.marketProfilePicture);
        btnOption = view.findViewById(R.id.btnOption);

        Glide.with(this).load(user.getPhotoUrl()).into(marketProfilePicture);

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        return view;
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.options_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }
}
