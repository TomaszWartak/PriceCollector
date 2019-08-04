package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// todo raczej do usunięcia
public interface AuthenticationServices3 {

    static AuthenticationServices3 getInstance() { return null; }

    FirebaseUser getCurrentUser();

    void signIn(Object object);

    void signOut();

}
