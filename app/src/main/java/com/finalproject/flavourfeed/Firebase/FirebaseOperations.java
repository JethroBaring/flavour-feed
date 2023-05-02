package com.finalproject.flavourfeed.Firebase;

import androidx.annotation.NonNull;

import com.finalproject.flavourfeed.Models.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseOperations {
    public static void addFollow(String toUserId, String fromUserId, String displayName, String type, FirebaseFirestore firebaseFirestore) {
        Map<String, Object> newFriendRequest = new HashMap<>();
        newFriendRequest.put("userId", fromUserId);
        newFriendRequest.put("displayName", displayName);
        firebaseFirestore.collection("userInformation").document(toUserId).collection(type).document(fromUserId).set(newFriendRequest);
    }

    public static void addNotification(NotificationModel notification, FirebaseFirestore firebaseFirestore) {
        Map<String, Object> newNotification = new HashMap<>();
        newNotification.put("toUserId", notification.getToUserId());
        newNotification.put("fromUserId", notification.getFromUserId());
        newNotification.put("notificationType", notification.getNotificationType());
        newNotification.put("postId", notification.getPostId());
        newNotification.put("timestamp", FieldValue.serverTimestamp());
        firebaseFirestore.collection("notificationInformation").add(newNotification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String notificationId = documentReference.getId();
                firebaseFirestore.collection("notificationInformation").document(notificationId).update("notificationId", notificationId);
            }
        });
    }

    public static void updateNotification(String notificationId, int notificationType, FirebaseFirestore firebaseFirestore) {
        firebaseFirestore.collection("notificationInformation").document(notificationId).update("notificationType", notificationType);
    }

    public static void deleteFriendRequest(String toUserId, String fromUserId, String type, FirebaseFirestore firebaseFirestore) {
        firebaseFirestore.collection("userInformation").document(toUserId).collection(type).document(fromUserId).delete();
    }

    public static void addToFriendsList(String toUserId, String newFriendId, FirebaseFirestore firebaseFirestore) {
        DocumentReference newFriend = firebaseFirestore.collection("userInformation").document(newFriendId);

        newFriend.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Map<String, Object> newUserFriend = new HashMap<>();
                        newUserFriend.put("displayName", documentSnapshot.getString("displayName"));
                        newUserFriend.put("userId", documentSnapshot.getString("userId"));
                        firebaseFirestore.collection("userInformation").document(toUserId).collection("friends").document(newFriendId).set(newUserFriend);
                    }
                }
            }
        });
    }
}
