package com.dev4lazy.pricecollector.model.logic;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/*
singleton do autentykacji na podstawie wasnego id użytkownika i hasla
 */
public class CustomTokenFirebaseAuthenticationServices  {

    private static final String TAG = "CustomTokenFirebaseAuthenticationServices";

    private static final CustomTokenFirebaseAuthenticationServices ourInstance = new CustomTokenFirebaseAuthenticationServices();

    private FirebaseAuth authServices;

    private String customToken = null;

    Messenger mockAuthService = null;

    boolean isBound;

    private ServiceConnection myConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(
                ComponentName className,
                IBinder service) {
            mockAuthService = new Messenger(service);
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(
                ComponentName className) {
            mockAuthService = null;
            isBound = false;
        }
    };

    Activity activity = null;   //todo...

    MockAuthServiceBroadcastReceiver mockAuthServiceBroadcastReceiver = null;

    private CustomTokenFirebaseAuthenticationServices() {
        authServices = FirebaseAuth.getInstance();
        // todo a może tutaj zrobić getCurrentUser? i z niego, jeśli się da pobrać customToken
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void bindToMockAuthService() {
        Intent intent = new Intent();
        intent.setClassName(
                "com.dev4lazy.pricecollectormockauth",
                "com.dev4lazy.pricecollectormockauth.MockAuthService");
        // nazwa serwisu = ComponentInfo{com.dev4lazy.pricecollectormockauth/com.dev4lazy.pricecollectormockauth.MockAuthService}
        //activity.startForegroundService(intent);
        boolean result = activity.bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindFromMockAuthService() {
        /*
        Intent intent = new Intent();
        intent.setClassName(
                "com.dev4lazy.pricecollectormockauth",
                "com.dev4lazy.pricecollectormockauth.MockAuthService");
        // nazwa serwisu = ComponentInfo{com.dev4lazy.pricecollectormockauth/com.dev4lazy.pricecollectormockauth.MockAuthService}
        activity.stopService(intent);
        */
        activity.unbindService(myConnection);
    }

    public class MockAuthServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            customToken = "";
        }
    }


    public boolean isServiceRunning(String serviceClassName){
        final ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        // com.dev4lazy.pricecollectormockauth:mock_auth_process
        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    public static CustomTokenFirebaseAuthenticationServices getInstance() { return ourInstance; }

    public void signInCustomAuthServer(String userId, String userPassword) /*throws FileNotFoundException */{
        // todo !!!
        // na potrzeby mockowania serwera jeśli w haśle jest "x" lub "X" to serwer odrzuca
        if (userPassword.contains("x")||(userPassword.contains("X"))) {
            customToken = null;
            return;
        }
        if (!isBound)
            return;
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("password", userPassword);
        msg.setData(bundle);
        try {
            mockAuthService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        /*
        String projectID = "price-collector-c1410";
        //customToken = authServices.createCustomToken(userId);
        customToken = userId+"@"+projectID+".iam.gserviceaccount.com";
        */
        //todo czy token jest case sensitive, tzn czy dla nowak_j i NOWAK_J będzie taki sam?
    }

    public void signOutCustomAuthServer() {
        //todo
    }

    public void signInFirebase() {
        if (customToken!=null) {
                  /* todo tu masz problem, ze w addOnCompleteListener paramterem jest this aktywnoci, a jestes we fragmencie
            authServices.signInWithCustomToken(customToken).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInFirebase:success");
                            FirebaseUser user = authServices.getCurrentUser();
                            // todo updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInFirebase:failure", task.getException());
                            // todo toast z Fragmentu Toast.makeText(CustomAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            // todo updateUI(null);
                        }
                    }
                });
            ---- poniżej wersja dla Fragmentu
            */
            authServices.signInWithCustomToken(customToken)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInFirebase:success");
                            FirebaseUser user = authServices.getCurrentUser();
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
        authServices.signOut();
        customToken = null;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        //todo - to powinno być wołane przy inicjalizacji aktywności - When initializing your Activity, check to see if the user is currently signed in.
        return authServices.getCurrentUser();
    }


}
