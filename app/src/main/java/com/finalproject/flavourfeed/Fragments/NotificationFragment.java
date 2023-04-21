package com.finalproject.flavourfeed.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.NotificationAdapter;
import com.finalproject.flavourfeed.Entity.CommentEntity;
import com.finalproject.flavourfeed.Entity.NotificationEntity;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationClickInterface {
    RecyclerView notificationRecyclerView;
    ArrayList<NotificationEntity> notifications;
    NotificationAdapter notificationAdapter;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        notificationAdapter = new NotificationAdapter(NotificationEntity.itemCallback, this);
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
                    List<NotificationEntity> data = value.toObjects(NotificationEntity.class);
                    notifications = new ArrayList<>();
                    for (NotificationEntity notification : data) {
                        if (notification.getToUserId().equals(user.getUid()))
                            notifications.add(notification);
                    }
                    notificationAdapter.submitList(notifications);
                    notificationRecyclerView.scrollToPosition(notificationAdapter.getItemCount() - 1);
                }
            }
        });
    }

    public void addDummyData() {
        Map<String, Object> newNotification = new HashMap<>();
        newNotification.put("notificationId", UUID.randomUUID().toString());
        newNotification.put("toUserId", "yXLUGG6MXeRBnGtUaMIMh6tu8nt1");
        newNotification.put("fromUserId", "yXLUGG6MXeRBnGtUaMIMh6tu8nt1");
        newNotification.put("notificationType", 0);
        newNotification.put("postId", UUID.randomUUID().toString());
        newNotification.put("timestamp", FieldValue.serverTimestamp());
        db.collection("notificationInformation").add(newNotification);
    }

    @Override
    public void onDelete(int pos) {

    }
}