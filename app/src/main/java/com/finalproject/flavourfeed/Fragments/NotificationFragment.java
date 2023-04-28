package com.finalproject.flavourfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.flavourfeed.Adapters.NotificationAdapter;
import com.finalproject.flavourfeed.Models.NotificationModel;
import com.finalproject.flavourfeed.Pages.PostPage;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationClickInterface {
    RecyclerView notificationRecyclerView;
    ArrayList<NotificationModel> notifications;
    NotificationAdapter notificationAdapter;
    FirebaseFirestore db;
    FirebaseUser user;

    ViewUserProfileFragment viewUserProfileFragment = new ViewUserProfileFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        notificationAdapter = new NotificationAdapter(NotificationModel.itemCallback, this);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerView.setAdapter(notificationAdapter);
        getAllData();
        return view;
    }

    public void getAllData() {
        db.collection("notificationInformation").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<NotificationModel> data = value.toObjects(NotificationModel.class);
                            notifications = new ArrayList<>();
                            for (NotificationModel notification : data) {
                                if (notification.getToUserId().equals(user.getUid()))
                                    notifications.add(notification);
                            }
                            notificationAdapter.submitList(notifications);
                            notificationRecyclerView.scrollToPosition(notificationAdapter.getItemCount() - 1);
                        }
                    }
                });
    }


    @Override
    public void onDelete(int pos) {

    }

    @Override
    public void onNotificationClick(NotificationModel notification) {
        if (notification.getNotificationType() != NotificationModel.COMMENT_NOTIFICATION && notification.getNotificationType() != NotificationModel.LIKE_NOTIFICATION) {
            Bundle bundle = new Bundle();
            bundle.putString("fromUserId",notification.getFromUserId());
            viewUserProfileFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, viewUserProfileFragment).commit();
        } else {
            Intent intent = new Intent(getContext(), PostPage.class);
            intent.putExtra("postId", notification.getPostId());
            startActivity(intent);
        }
    }
}