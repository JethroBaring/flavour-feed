package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.flavourfeed.Adapters.ProductAdapter;
import com.finalproject.flavourfeed.R;


public class MarketplaceFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.marketplace_fragment, container, false);

        RecyclerView productRecyclerView = view.findViewById(R.id.productRecyclerView);
        ProductAdapter productAdapter = new ProductAdapter(getContext());
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL, false));

        return view;
    }
}