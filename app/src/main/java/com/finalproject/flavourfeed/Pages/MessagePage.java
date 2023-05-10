package com.finalproject.flavourfeed.Pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.finalproject.flavourfeed.Adapters.MessageAdapter;
import com.finalproject.flavourfeed.Models.MessageModel;
import com.finalproject.flavourfeed.R;
import com.finalproject.flavourfeed.Utitilies.NoChangeAnimation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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
    ArrayList<MessageModel> messages;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        String otherUserId = getIntent().getStringExtra("otherUserId");
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageAdapter = new MessageAdapter(MessageModel.itemCallback);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);
        messageRecyclerView.setItemAnimator(new NoChangeAnimation());
        getAllData(otherUserId);

        TextView otherUserDisplayName = findViewById(R.id.txtDisplayName);

        db.collection("userInformation").document(otherUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    otherUserDisplayName.setText(snapshot.getString("displayName"));
                }
            }
        });

        EditText message = findViewById(R.id.message);
        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String toBeSend = message.getText().toString();
                    String[] ids = {user.getUid(), otherUserId};
                    Arrays.sort(ids);
                    String chatRoomId = String.join("_", ids);
                    Map<String, Object> newMessage = new HashMap<>();
                    newMessage.put("senderId", user.getUid());
                    newMessage.put("receiverId", otherUserId);
                    newMessage.put("message", toBeSend);
                    newMessage.put("timestamp", FieldValue.serverTimestamp());
                    message.setText("");

                    db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {
                                    //check if chatRoom exists
                                    db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").add(newMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String messageId = documentReference.getId();
                                            db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").document(messageId).update("messageId", messageId);
                                        }
                                    });
                                    db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).collection("messages").add(newMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                DocumentReference documentReference = task.getResult();
                                                String messageId = documentReference.getId();
                                                db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).collection("messages").document(messageId).update("messageId", messageId);
                                            }
                                        }
                                    });
                                    db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).update("lastModified", FieldValue.serverTimestamp());
                                    db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).update("lastModified", FieldValue.serverTimestamp());
                                } else {
                                    //create new chatroom
                                    Map<String, Object> newChatRoom = new HashMap<>();
                                    newChatRoom.put("otherUserId", otherUserId);
                                    newChatRoom.put("chatRoomId", chatRoomId);
                                    db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).set(newChatRoom);

                                    Map<String, Object> newChatRoom1 = new HashMap<>();
                                    newChatRoom1.put("otherUserId", user.getUid());
                                    newChatRoom1.put("chatRoomId", chatRoomId);
                                    db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).set(newChatRoom1);

                                    // Add the new message to the newly created chat room
                                    db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").add(newMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String messageId = documentReference.getId();
                                            db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").document(messageId).update("messageId", messageId);
                                            db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).update("lastModified", FieldValue.serverTimestamp());
                                        }
                                    });

                                    db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).collection("messages").add(newMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                DocumentReference documentReference = task.getResult();
                                                String messageId = documentReference.getId();
                                                db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).collection("messages").document(messageId).update("messageId", messageId);
                                                db.collection("userInformation").document(otherUserId).collection("chatRoom").document(chatRoomId).update("lastModified", FieldValue.serverTimestamp());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
                return true;
            }
        });
    }

    public void getAllData(String otherUserId) {
        String[] ids = {user.getUid(), otherUserId};
        Arrays.sort(ids);
        String chatRoomId = String.join("_", ids);

        db.collection("userInformation").document(user.getUid()).collection("chatRoom").document(chatRoomId).collection("messages").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<MessageModel> data = value.toObjects(MessageModel.class);
                messages = new ArrayList<>();
                messages.addAll(data);
                messageAdapter.submitList(messages);
                messageRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });

    }
}