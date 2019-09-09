package com.dev4lazy.pricecollector.model.logic.auth;

import android.os.Bundle;

/*
Klasa obsługująca logowanie na podstawie identyfikatora użytkownika i hasła, z wykorzystaniem tokena Firebase.
Realizowane jest to tak, że serwer logowania po otrzymaniu danych uwierzytelniających zwraca token Firebase,
który służy do uwierzytelnienie użytkownika w Firebase.
W tym konkretnym przypadku serwer logowania jest udawany przez usługę obsługiwaną przez instancję MockCustomTokenOwnAuthServices.
Dane logowania wysyłane są do usługi w metodzie signIn().
Obsługa wyniku logowania Firebase odbywa się w metodach callIfSucessful() i callIfUnsucessful()
wołanych w klasie obsługującej logowanie Firebase (implementującej interfejs FirebaseAuthSupport).
 */
public class MockCustomTokenOwnAuthSupport
        implements
        AuthSupport,
        AuthSupport.LoginCallback,
        MockCustomTokenOwnAuthServices.OnReceiveCallback {

// ----------------------------------------------------------
// Wykorzystywane w implementacji metod interfejsu logowania AuthSupport
    private Boolean loggedIn = false;
    LoginCallback loginCallbackService = null;

    // ----------------------------------------------------------
// Obsługa usługi udającej serwer logowania
    private MockCustomTokenOwnAuthServices mockCustomTokenOwnAuthServices;

// ----------------------------------------------------------
// obsługa Firebase
    private FirebaseAuthSupport firebaseAuthServices;

// ----------------------------------------------------------
// konstruktor :-)
    public MockCustomTokenOwnAuthSupport() {
        mockCustomTokenOwnAuthServices = new MockCustomTokenOwnAuthServices();
        mockCustomTokenOwnAuthServices.bindToMockAuthService();
        mockCustomTokenOwnAuthServices.setOnReceiveCallback(this);
        firebaseAuthServices = CustomTokenFirebaseAuthSupport.getInstance();
        firebaseAuthServices.setLoginCallbackService(this);
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu logowania AuthSupport
// wysyła dane logowania do własnego serwera logowania
    @Override
    public void signIn() {
        String userId = getCredential("USER_ID");
        String userPassword = getCredential("USER_PASSWORD");
        // todo !!!
        // na potrzeby mockowania serwera jeśli w haśle jest "x" lub "X" to serwer odrzuca
        if (userPassword.contains("x") || (userPassword.contains("X"))) {
            //todo customToken = null;
            callIfUnsucessful();
            return;
        }
        if (!mockCustomTokenOwnAuthServices.isBoundToMockAuthService()) {
            callIfUnsucessful();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("password", userPassword);
        mockCustomTokenOwnAuthServices.sendDataToService(bundle);
        //todo czy token jest case sensitive, tzn czy dla nowak_j i NOWAK_J będzie taki sam?
    }

    // wylogowuje użytkownika z własnego serwera logowania
    @Override
    public void signOut() {
        firebaseAuthServices.signOut();
        mockCustomTokenOwnAuthServices.unbindFromMockAuthService();
    }

    @Override
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania
    @Override
    public void setLoginCallbackService(LoginCallback loginCallback) {
        loginCallbackService = loginCallback;
    }

    @Override
    public LoginCallback getLoginCallback() {
        return loginCallbackService;
    }

    @Override
    public void callIfSucessful() {
        setLoggedIn(true);
        loginCallbackService.callIfSucessful();
    }

    @Override
    public void callIfUnsucessful() {
        setLoggedIn(false);
        loginCallbackService.callIfUnsucessful();
    }

 // ----------------------------------------------------------
// Implementacja metody interfejsu calbaku odbioru danych z mocka serwera logowania
// MockCustomTokenOwnAuthServices.OnReceiveCallback

    @Override
    public void callIfDataReceived(String token) {
        firebaseAuthServices.addCredential("TOKEN", token );
        firebaseAuthServices.signIn();
    }

}