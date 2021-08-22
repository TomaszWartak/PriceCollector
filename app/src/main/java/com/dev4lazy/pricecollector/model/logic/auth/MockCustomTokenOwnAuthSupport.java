package com.dev4lazy.pricecollector.model.logic.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dev4lazy.pricecollector.utils.BroadcastReceiverWrapper;
import com.dev4lazy.pricecollector.utils.ExternalServicesSupport;
import com.dev4lazy.pricecollector.utils.AppHandle;

/**
Klasa obsługująca logowanie na podstawie identyfikatora użytkownika i hasła, z wykorzystaniem tokena Firebase.
Realizowane jest to tak, że serwer logowania po otrzymaniu danych uwierzytelniających zwraca token Firebase,
który służy do uwierzytelnienie użytkownika w Firebase.
W tym konkretnym przypadku serwer logowania jest udawany przez usługę obsługiwaną przez instancję ExternalServicesSupport.
Dane logowania wysyłane są do usługi w metodzie signIn().
Obsługa wyniku logowania Firebase odbywa się w metodach callIfSucessful() i callIfUnsucessful()
wołanych w klasie obsługującej logowanie Firebase (implementującej interfejs FirebaseAuthSupport).
 */
public class MockCustomTokenOwnAuthSupport
        implements
        AuthSupport,
        AuthSupport.LoginCallback {

// ----------------------------------------------------------
// Wykorzystywane w implementacji metod interfejsu logowania AuthSupport
    private Boolean loggedIn = false;
    LoginCallback loginCallback = null;

    // ----------------------------------------------------------
// Obsługa usługi udającej serwer logowania
    private ExternalServicesSupport externalServicesSupport;

// ----------------------------------------------------------
// obsługa Firebase
    private FirebaseAuthSupport firebaseAuthSupport;

// ----------------------------------------------------------
// konstruktor :-)
    public MockCustomTokenOwnAuthSupport() {
        externalServicesSupport = new ExternalServicesSupport(AppHandle.getHandle());
        externalServicesSupport.setBroadcastReceiverWrapper(
                new BroadcastReceiverWrapper(
                    new MockAuthServiceBroadcastReceiver(),
                    "DATA_FROM_MOCKAUTH_READY"
                )
        );
        externalServicesSupport.startBroadcastListening();
        externalServicesSupport.bindToService(
                "com.dev4lazy.pricecollectormockauth",
                "com.dev4lazy.pricecollectormockauth.MockAuthService");
        // todo tutaj można podmienić rodzaj autentykacji Firebase
        firebaseAuthSupport = CustomTokenFirebaseAuthSupport.getInstance();
        firebaseAuthSupport.setLoginCallback(this);
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

        // TODO
        // if (!externalServicesSupport.isBoundToMockAuthService()) {
        if (!externalServicesSupport.isBoundToService()) {
            callIfUnsuccessful();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("userId", getCredential("USER_ID") );
        bundle.putString("password", getCredential("USER_PASSWORD") );
        externalServicesSupport.sendDataToService(bundle);
        //todo czy token jest case sensitive, tzn czy dla nowak_j i NOWAK_J będzie taki sam?
    }

    // wylogowuje użytkownika z własnego serwera logowania
    @Override
    public void signOut() {
        firebaseAuthSupport.signOut();
        // TODO: jeśli zrobisz to, co opisałeś w komentarzach do signIn(), to tutaj też musisz
        // TODO
        externalServicesSupport.unbindFromService();
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
    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    @Override
    public LoginCallback getLoginCallback() {
        return loginCallback;
    }

    @Override
    public void callIfSuccessful() {
        setLoggedIn(true);
        loginCallback.callIfSuccessful();
    }

    @Override
    public void callIfUnsuccessful() {
        setLoggedIn(false);
        loginCallback.callIfUnsuccessful();
    }

// ---------------------------------------------------------------------------------
     public void callIfDataReceived(boolean authenticated, String token) {
         if (authenticated) {
             firebaseAuthSupport.addCredential("TOKEN", token);
             firebaseAuthSupport.signIn();
         } else {
             callIfUnsuccessful();
         }
     }

// Odbiornik brodcastów z inforomacją o rezultacie logowaania
    private class MockAuthServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DATA_FROM_MOCKAUTH_READY")) {
                if (intent.getBooleanExtra("AUTHENTICATED", false )) {
                    callIfDataReceived(true, intent.getStringExtra("TOKEN") );
                } else {
                    callIfDataReceived(false,"" );
                }
            }
        }
    }
}