package com.dev4lazy.pricecollector.model.logic;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
singleton do autentykacji na podstawie wasnego id użytkownika i hasla
 */
public class CustomTokenFirebaseAuthServices {

    private static final String TAG = "CustomTokenFirebaseAuthServices";

    private static final CustomTokenFirebaseAuthServices ourInstance = new CustomTokenFirebaseAuthServices();

    private FirebaseAuth firebaseAuthServices;

    private String customToken = null;

    private CustomTokenFirebaseAuthServices() {
        firebaseAuthServices = FirebaseAuth.getInstance();
    }

    public static CustomTokenFirebaseAuthServices getInstance() { return ourInstance; }

    public void setCustomToken(String customToken) {
        this.customToken = customToken;
    }

    public void signInFirebase() {
        if (customToken!=null) {
            firebaseAuthServices.signInWithCustomToken(customToken)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInFirebase:success");
                            FirebaseUser user = firebaseAuthServices.getCurrentUser();
                            // todo updateUI(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInFirebase:failure", exception);
                            // todo toast z Fragmentu Toast.makeText(CustomAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            // todo updateUI(null);
                        }
                    });
        }
    }

    public void signOutFromFirebase() {
        firebaseAuthServices.signOut();
        customToken = null;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        //todo - to powinno być wołane przy inicjalizacji aktywności - When initializing your Activity, check to see if the user is currently signed in.
        return firebaseAuthServices.getCurrentUser();
    }


}
