package com.finalproject.flavourfeed.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {
    private static FirebaseFirestore firebaseFirestoreInstance;
    private static FirebaseAuth firebaseAuthInstance;

    public static FirebaseFirestore getFirestoreInstance() {
        if (firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance();
        }
        return firebaseFirestoreInstance;
    }
}
