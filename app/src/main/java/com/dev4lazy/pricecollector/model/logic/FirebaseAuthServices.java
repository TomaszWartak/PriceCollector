package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.TreeMap;

public interface FirebaseAuthServices {

    FirebaseAuth firebaseAuthServices = FirebaseAuth.getInstance();

    Map<String,String> credentials = new TreeMap<>();

    default void addCredential(String key, String value) {
        credentials.put(key, value);
    }

    default String getCredential( String key ) {
        return credentials.get(key);
    }

    void signInFirebase();

    void signOutFromFirebase();

    default FirebaseUser getCurrentFirebaseUser() {
        //todo - to powinno być wołane przy inicjalizacji aktywności - When initializing your Activity, check to see if the user is currently signed in.
        return firebaseAuthServices.getCurrentUser();
    }
}
