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
    private FirebaseAuthSupport firebaseAuthSupport;

// ----------------------------------------------------------
// konstruktor :-)
    public MockCustomTokenOwnAuthSupport() {
        mockCustomTokenOwnAuthServices = new MockCustomTokenOwnAuthServices();
        mockCustomTokenOwnAuthServices.bindToMockAuthService();
        mockCustomTokenOwnAuthServices.setOnReceiveCallback(this);
        // todo tutaj można podmienić rodzaj autentykacji Firebase
        firebaseAuthSupport = CustomTokenFirebaseAuthSupport.getInstance();
        firebaseAuthSupport.setLoginCallbackService(this);
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu logowania AuthSupport
// wysyła dane logowania do własnego serwera logowania
    @Override
    public void signIn() {
        // TODO: mockCustomTokenOwnAuthServices.isBoundToMockAuthService() trzeba zamienić na
        //  coś w rodzaju "isAuthServerAvailable()
        //  Następnie całą sekwencję
        //   Bundle bundle = new Bundle();
        //   bundle.putString("userId", userId);
        //   bundle.putString("password", userPassword);
        //   mockCustomTokenOwnAuthServices.sendDataToService(bundle);
        //   trzeba prznieść do MCTOAService, a tutaj zrobic wywołanie metody abstrakcyjne
        //   interfejsu albo klasy, po której dziedziczyłby MCTOAService, np.
        //   FirebaseTokenAuthServerServices  <-- AuthServerServices
        //   A metoda nazywałaby się po prostu signIn???
        //   UWAGA - stworzyłem interfejs TokenAuthServerServices, ale nie użyłem, go żeby czegoś nie popsuć...
        if (!mockCustomTokenOwnAuthServices.isBoundToMockAuthService()) {
            callIfUnsuccessful();
            return;
        }
        String userId = getCredential("USER_ID");
        String userPassword = getCredential("USER_PASSWORD");
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("password", userPassword);
        mockCustomTokenOwnAuthServices.sendDataToService(bundle);
        //todo czy token jest case sensitive, tzn czy dla nowak_j i NOWAK_J będzie taki sam?
    }

    // wylogowuje użytkownika z własnego serwera logowania
    @Override
    public void signOut() {
        firebaseAuthSupport.signOut();
        // TODO: jeśli zrobisz to, co opisałeś w komentarzach do signIn(), to tutaj też musisz
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
    public void callIfSuccessful() {
        setLoggedIn(true);
        loginCallbackService.callIfSuccessful();
    }

    @Override
    public void callIfUnsuccessful() {
        setLoggedIn(false);
        loginCallbackService.callIfUnsuccessful();
    }

 // ----------------------------------------------------------
// Implementacja metody interfejsu callbaku odbioru danych z mocka serwera logowania
// MockCustomTokenOwnAuthServices.OnReceiveCallback

    @Override
    public void callIfDataReceived(boolean authenticated, String token) {
        if (authenticated) {
            firebaseAuthSupport.addCredential("TOKEN", token);
            firebaseAuthSupport.signIn();
        } else {
            callIfUnsuccessful();
        }
    }

}