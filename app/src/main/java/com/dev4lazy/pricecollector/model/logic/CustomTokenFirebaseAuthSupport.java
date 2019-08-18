package com.dev4lazy.pricecollector.model.logic;

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
public class CustomTokenFirebaseAuthSupport implements FirebaseAuthSupport, FirebaseAuthSupport.LoginCallback {

    private static final String TAG = "CustomTokenFirebaseAuthSupport";

    private static final CustomTokenFirebaseAuthSupport ourInstance = new CustomTokenFirebaseAuthSupport();

    private AuthSupport.LoginCallback loginCallbackService = null;

    private String customToken = null;

    private Boolean loggedIn = false;

    private CustomTokenFirebaseAuthSupport() {
    }

    public static CustomTokenFirebaseAuthSupport getInstance() { return ourInstance; }

    /*public void setCustomToken(String customToken) {
        this.customToken = customToken;
    }
*/
    @Override
    public void signIn() {
        customToken = getCredential("TOKEN");
        if (customToken!=null) {
            firebaseAuthServices.signInWithCustomToken(customToken)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInFirebase:success");
                            FirebaseUser user = firebaseAuthServices.getCurrentUser();
                            setLoggedIn(true);
                            callIfSucessful();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInFirebase:failure", exception);
                            setLoggedIn(false);
                            callIfUnsucessful();
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
    public void setLoginCallbackService(LoginCallback loginCallback) {
        this.loginCallbackService = loginCallback;
    }

    @Override
    public void callIfSucessful() {
        loginCallbackService.callIfSucessful();
    }

    @Override
    public void callIfUnsucessful() {
        loginCallbackService.callIfUnsucessful();
    }

    @Override
    public LoginCallback getLoginCallback() {
        return loginCallbackService;
    }
}
