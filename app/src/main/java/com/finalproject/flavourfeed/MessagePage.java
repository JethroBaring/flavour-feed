package com.finalproject.flavourfeed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.finalproject.flavourfeed.Adapters.CommentAdapter;
import com.finalproject.flavourfeed.Adapters.MessageAdapter;
import com.finalproject.flavourfeed.Models.CommentModel;
import com.finalproject.flavourfeed.Models.MessageModel;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePage extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    RecyclerView messageRecyclerView;
    ArrayList<MessageModel> chats;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        String otherUserId = getIntent().getStringExtra("otherUserId");

        messageAdapter = new MessageAdapter(MessageModel.itemCallback);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setItemAnimator(new NoChangeAnimation());
        getAllData(otherUserId);
    }

    public void getAllData(String otherUserId) {
        String[]ids = {user.getUid(), otherUserId};
        Arrays.sort(ids);
        String chatRoomId = String.join("_", ids);
        DocumentReference chatRoomRef = db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId);
        chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        db.collection("userInformation").document()
                    } else {
                        Map<String, Object> newChatRoom = new HashMap<>();
                        newChatRoom.put("chatRoomId", chatRoomId);
                        newChatRoom.put("userOneId", user.getUid());
                        newChatRoom.put("userTwoId", otherUserId);
                        db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).set(newChatRoom);
                        db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).set(newChatRoom);
                    }
                }
            }
        });




        db.collection("commentInformation")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            List<CommentModel> data = value.toObjects(CommentModel.class);
                            comments = new ArrayList<>();
                            for (CommentModel comment : data) {
                                if (comment.getPostId().equals(postId)) {
                                    comments.add(comment);
                                }
                            }
                            commentAdapter.submitList(comments);
                            commentRecyclerView.scrollToPosition(commentAdapter.getItemCount() - 1);
                        }
                    }
                });
    }
}