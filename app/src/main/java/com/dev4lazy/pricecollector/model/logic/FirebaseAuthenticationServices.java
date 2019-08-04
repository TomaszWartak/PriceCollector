package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// todo co dalej?
public class FirebaseAuthenticationServices {

    private static final FirebaseAuthenticationServices ourInstance = new FirebaseAuthenticationServices();

    private FirebaseAuth authServices;

    private FirebaseAuthenticationServices() {
        authServices = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthenticationServices getInstance() { return ourInstance; }

    public FirebaseUser getCurrentUser() {
        return authServices.getCurrentUser();
    }

    public void signIn(Object object) {
        // todo !!!
    }

    public void signOut() {
        authServices.signOut();
    }
}
