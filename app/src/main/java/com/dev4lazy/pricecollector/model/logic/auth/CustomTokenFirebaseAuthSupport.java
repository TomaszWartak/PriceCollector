package com.dev4lazy.pricecollector.model.logic.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/*
Singleton do autentykacji na podstawie własnego id użytkownika i hasla.
Przed wywołaniem smetody ignInFirebase() należy ustawić wartość tokena
w wywołaniu addCredential("TOKEN", tokenString.
 */
public class CustomTokenFirebaseAuthSupport implements FirebaseAuthSupport, AuthSupport.LoginCallback {

    private static final String TAG = "CustomTokenFirebaseAuthSupport";

    private static final CustomTokenFirebaseAuthSupport instance = new CustomTokenFirebaseAuthSupport();

    private AuthSupport.LoginCallback loginCallbackService = null;
    private Boolean loggedIn = false;

    private String customToken = null;

    private CustomTokenFirebaseAuthSupport() {
    }

    public static CustomTokenFirebaseAuthSupport getInstance() { return instance; }

    @Override
    public void signIn() {
        customToken = getCredential("TOKEN");
        if (customToken!=null) {
            // TODO jak zrobić oczekiwanie przez określony czas bo czasamie wygląda tak jakby się Firebase zawieszał...
            firebaseAuthServices.signInWithCustomToken(customToken)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            // TODO !!! wszystkie użycia LOg zrób
                            //  if (BuildConfig.DEBUG) {
                            //	    Log.d( TAG, ".."+var );
                            //  }
                            Log.d(TAG, "signInFirebase:success");
                            FirebaseUser user = firebaseAuthServices.getCurrentUser();
                            // TODO TEST
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            // TODO ENDTEST
                            setLoggedIn(true);
                            callIfSuccessful();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInFirebase:failure", exception);
                            setLoggedIn(false);
                            callIfUnsuccessful();
                        }
                    });
        }
    }

    @Override
    public void signOut() {
        firebaseAuthServices.signOut();
        customToken = null;
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
        this.loginCallbackService = loginCallback;
    }

    @Override
    public LoginCallback getLoginCallback() {
        return loginCallbackService;
    }

    @Override
    public void callIfSuccessful() {
        loginCallbackService.callIfSuccessful();
    }

    @Override
    public void callIfUnsuccessful() {
        loginCallbackService.callIfUnsuccessful();
    }


}
