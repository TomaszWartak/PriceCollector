package com.dev4lazy.pricecollector.model.logic.auth;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;

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
            // TODO jak zrobić oczekiwanie przez określony czas bo czasami wygląda tak jakby się Firebase zawieszał...
            firebaseAuthServices.signInWithCustomToken(customToken)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "signInFirebase:success");
                            }
                            // TODO TEST
                            FirebaseUser user = firebaseAuthServices.getCurrentUser();
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
                            if (BuildConfig.DEBUG) {
                                Log.w( TAG, "signInFirebase:failure", exception);
                            }
                            setLoggedIn( false );
                            callIfUnsuccessful( AppHandle.getHandle().getResources().getString(R.string.firebase_login_problem) );
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
    public void callIfUnsuccessful( String reasonMessage ) {
        loginCallbackService.callIfUnsuccessful( reasonMessage );
    }


}
