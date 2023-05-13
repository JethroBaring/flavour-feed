package com.finalproject.flavourfeed;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Models.DashboardUserModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ActiveUsersFragment extends Fragment implements BanUnbanAdapter.BanUnbanInterface {
    FirebaseFirestore db;
    RecyclerView activeUsersRecyclerView;
    BanUnbanAdapter banUnbanAdapter;
    ArrayList<DashboardUserModel> users;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_users_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        activeUsersRecyclerView = view.findViewById(R.id.activeUsersRecyclerView);
        banUnbanAdapter = new BanUnbanAdapter(DashboardUserModel.itemCallback, this,true);
        activeUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activeUsersRecyclerView.setAdapter(banUnbanAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("userInformation").whereEqualTo("ban",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DashboardUserModel> data = value.toObjects(DashboardUserModel.class);
                users = new ArrayList<>();
                users.addAll(data);
                banUnbanAdapter.submitList(users);
            }
        });
    }

    @Override
    public void OnViewInformationClick() {

    }

    @Override
    public void OnBanUnbanClick(String userId, boolean ban) {
        db.collection("userInformation").document(userId).update("ban",ban);
    }
}