package com.dev4lazy.pricecollector.model.logic;

// todo raczej do usunięcia
public class AuthenticationServices2 {
    private static final AuthenticationServices2 ourInstance = new AuthenticationServices2();

    public static AuthenticationServices2 getInstance() {
        return ourInstance;
    }

    private AuthenticationServices2() {
    }

    public void setUserLogin(String userLogin) {

    }

    public void requestForAccess( String password) {

    }

    public boolean accessIsGranted() {
        // todo
        return true;
    }
}
