package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.finalproject.flavourfeed.Adapters.ChatRoomAdapter;
import com.finalproject.flavourfeed.Models.ChatRoomModel;
import com.finalproject.flavourfeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomPage extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseUser user;
    RecyclerView chatListRecyclerView;
    ArrayList<ChatRoomModel> chats;
    ChatRoomAdapter chatRoomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_page);
        db = FirebaseFirestore.getInstance();
        chatListRecyclerView = findViewById(R.id.chatListRecyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        chatRoomAdapter = new ChatRoomAdapter(ChatRoomModel.itemCallback);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListRecyclerView.setAdapter(chatRoomAdapter);
        chatListRecyclerView.setItemAnimator(new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
        ImageView btnSearch = findViewById(R.id.icnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatSearchPage.class));
            }
        });
        getAllData();
    }

    public void getAllData() {
        db.collection("userInformation")
                .document(user.getUid()).collection("chatRoom")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<ChatRoomModel> data = value.toObjects(ChatRoomModel.class);
                            chats = new ArrayList<>();
                            chats.addAll(data);
                            chatRoomAdapter.submitList(chats);
                            chatListRecyclerView.scrollToPosition(chatRoomAdapter.getItemCount() - 1);
                        }
                    }
                });
    }
}