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
public class CustomTokenFirebaseAuthServices implements FirebaseAuthServices {

    private static final String TAG = "CustomTokenFirebaseAuthServices";

    private static final CustomTokenFirebaseAuthServices ourInstance = new CustomTokenFirebaseAuthServices();

    private String customToken = null;

    private CustomTokenFirebaseAuthServices() {
    }

    public static CustomTokenFirebaseAuthServices getInstance() { return ourInstance; }

    /*public void setCustomToken(String customToken) {
        this.customToken = customToken;
    }
*/
    @Override
    public void signInFirebase() {
        customToken = getCredential("TOKEN");
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

    @Override
    public void signOutFromFirebase() {
        firebaseAuthServices.signOut();
        customToken = null;
    }

}
