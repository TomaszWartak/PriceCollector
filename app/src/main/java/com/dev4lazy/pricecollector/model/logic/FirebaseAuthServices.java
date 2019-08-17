package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthServices extends AbstractFirebaseAuthServices {

    private static final String TAG = "FirebaseAuthServices";

    private FirebaseAuth firebaseAuthServices;

    public FirebaseAuthServices() {

    }

    public void signInFirebase() {

    }

        public void signOutFromFirebase() {
        firebaseAuthServices.signOut();
    }

    public FirebaseUser getCurrentFirebaseUser() {
        //todo - to powinno być wołane przy inicjalizacji aktywności - When initializing your Activity, check to see if the user is currently signed in.
        return firebaseAuthServices.getCurrentUser();
    }


}
