package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class AbstractFirebaseAuthServices {

    private FirebaseAuth firebaseAuthServices;

    public abstract void signInFirebase();

    public abstract void signOutFromFirebase();

    public abstract FirebaseUser getCurrentFirebaseUser() ;
}
