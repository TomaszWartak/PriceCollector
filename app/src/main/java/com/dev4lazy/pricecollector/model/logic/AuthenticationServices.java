package com.dev4lazy.pricecollector.model.logic;

public class AuthenticationServices {
    private static final AuthenticationServices ourInstance = new AuthenticationServices();

    public static AuthenticationServices getInstance() {
        return ourInstance;
    }

    private AuthenticationServices() {
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
