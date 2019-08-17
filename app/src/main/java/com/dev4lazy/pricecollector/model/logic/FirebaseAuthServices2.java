package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

interface FirebaseAuthServices2 {

    FirebaseAuth firebaseAuthServices = null;

    void signInFirebase();

    void signOutFromFirebase();

    default FirebaseUser getCurrentFirebaseUser() {
        return firebaseAuthServices.getCurrentUser();
    }
}
