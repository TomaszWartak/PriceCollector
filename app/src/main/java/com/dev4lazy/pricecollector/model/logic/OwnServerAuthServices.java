package com.dev4lazy.pricecollector.model.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.TreeMap;

public interface OwnServerAuthServices {

    Map<String,String> credentials = new TreeMap<>();

    default void addCredential(String key, String value) {
        credentials.put(key, value);
    }

    default String getCredential(String key) {
        return credentials.get(key);
    }

    void signInOwnServer();

    void signOutFromOwnServer();

}
