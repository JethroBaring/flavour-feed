package com.finalproject.flavourfeed;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPassword {
    static FirebaseAuth mAuth;

    public static FirebaseUser register(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password);
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }

}
