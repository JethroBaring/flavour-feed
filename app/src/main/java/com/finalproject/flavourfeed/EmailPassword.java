package com.finalproject.flavourfeed;

import com.google.firebase.auth.FirebaseAuth;

public class EmailPassword {
    static FirebaseAuth mAuth;

    public static void register(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password);

    }

}
