package com.dev4lazy.pricecollector.model.logic.auth;

import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public interface FirebaseAuthSupport extends AuthSupport, AuthSupport.LoginCallback {

    FirebaseAuth firebaseAuthServices = FirebaseAuth.getInstance();

    default FirebaseUser getCurrentFirebaseUser() {
        //todo - to powinno być wołane przy inicjalizacji aktywności - When initializing your Activity, check to see if the user is currently signed in.
        return firebaseAuthServices.getCurrentUser();
    }

}
