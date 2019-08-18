package com.dev4lazy.pricecollector.model.logic;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;

/*
Klasa obsługująca logowanie na podstawie identyfikatora użytkownika i hasła, z wykorzystaniem tokena Firebase.
Realizowane jest to tak, że serwer logowania po otrzymaniu danych uwierzytelniających zwraca token Firebase,
który służy do uwierzytelnienie użytkownika w Firebase.
W tym konkretnym przypadku serwer logowania jest udawany przez usługę.
Dane logowania wysyłane są do usługi w metodzie signIn().
Resultat odbierany jest przez mockAuthServiceBroadcastReceiver w metodzie onReceive(),
która woła metodę serveReceivedData(), gdzie:
- jako LoginCallback do obsługi rezultatu logowania w Firebase ustawiany jest this instancji niniejszej klasy, oraz
- wysyłane są dane do logowania Firebase.
Obsługa wyniku logowania Firebase odbywa się w metodach callIfSucessful() i callIfUnsucessful()
wołanych w klasie obsługującej logowanie Firebase (implementującej interfejs FirebaseAuthSupport).
 */
public class MockCustomTokenOwnAuthSupport implements AuthSupport, AuthSupport.LoginCallback {

// ----------------------------------------------------------
// Wykorzystywane w implementacji metod interfejsu logowania AuthSupport
    private Boolean loggedIn = false;
    private AuthSupport.LoginCallback loginCallbackService = null;

// ----------------------------------------------------------
// obsługa Firebase
    private FirebaseAuthSupport firebaseAuthServices = CustomTokenFirebaseAuthSupport.getInstance();

    // Token Firebase zwracany przez własny serwer logowania, na podstawie klucza prywatnego Firebase
    private String customToken = null;

// ----------------------------------------------------------
// Obsługa usługi udającej serwer logowania i oddającej token
    // messenger do wysyłania danych do serwisu, udającego własny serwer logowania
    private Messenger mockAuthService = null;

    // true, jeśli udało się podłączy do serwisu, udającego własny serwer logowania
    private boolean isBoundToMockAuthService = false;

    // połączenie z serwisem, udającym własny serwer logowania
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(
                ComponentName className,
                IBinder service) {
            mockAuthService = new Messenger(service);
            isBoundToMockAuthService = true;
            registerMockAuthServiceBroadcastReceiver();
        }

        @Override
        public void onServiceDisconnected(
                ComponentName className) {
            mockAuthService = null;
            isBoundToMockAuthService = false;
            unregisterMockAuthServiceBroadcastReceiver();
        }
    };

    // odbiornik danych z serwisu, udającego własny serwer logowania
    private MockAuthServiceBroadcastReceiver mockAuthServiceBroadcastReceiver = null;

// ----------------------------------------------------------
// konstruktor :-)
    public MockCustomTokenOwnAuthSupport() {
        bindToMockAuthService();
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu logowania AuthSupport
// wysyła dane logowania do własnego serwera logowania
    @Override
    public void signIn() /*todo throws FileNotFoundException */ {
        String userId = getCredential("USER_ID");
        String userPassword = getCredential("USER_PASSWORD");
        // todo !!!
        // na potrzeby mockowania serwera jeśli w haśle jest "x" lub "X" to serwer odrzuca
        if (userPassword.contains("x") || (userPassword.contains("X"))) {
            customToken = null;
            return;
        }
        if (!isBoundToMockAuthService)
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
        //todo czy token jest case sensitive, tzn czy dla nowak_j i NOWAK_J będzie taki sam?
    }

    // wylogowuje użytkownika z własnego serwera logowania
    @Override
    public void signOut() {
        unbindFromMockAuthService();
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
// obsługa Firebase
    // zwraca token Firebase uzyskany z własnego serwera logowania
    public String getCustomToken() {
        return customToken;
    }

// ----------------------------------------------------------
// Implementacja metod interfejsu calbaków logowania AuthSupport.LoginCallback
// Obsługa callbacków logowania
    @Override
    public void setLoginCallbackService(LoginCallback loginCallback) {
        loginCallbackService = loginCallback;
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

    @Override
    public LoginCallback getLoginCallback() {
        return loginCallbackService;
    }

// ----------------------------------------------------------
// Obsługa usługi udającej serwer logowania i oddającej token
    private void bindToMockAuthService() {
        Intent intent = new Intent();
        intent.setClassName(
                "com.dev4lazy.pricecollectormockauth",
                "com.dev4lazy.pricecollectormockauth.MockAuthService");
        boolean result = AppHandle.getAppHandle().bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindFromMockAuthService() {
        AppHandle.getAppHandle().unbindService(myConnection);
    }

    // todo to raczej do wywalenia
    public boolean isServiceRunning(String serviceClassName){
        final ActivityManager activityManager = (ActivityManager) AppHandle.getAppHandle().getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }

    private void serveReceivedData(Intent intent) {
        customToken = intent.getStringExtra("TOKEN");
        firebaseAuthServices.addCredential("TOKEN", customToken );
        firebaseAuthServices.setLoginCallbackService(this);
        firebaseAuthServices.signIn();
    }

    private class MockAuthServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DATA_FROM_MOCKAUTH_READY")) {
                serveReceivedData(intent);
            }
        }
    }

    private void registerMockAuthServiceBroadcastReceiver() {
        mockAuthServiceBroadcastReceiver = new MockAuthServiceBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DATA_FROM_MOCKAUTH_READY");
        AppHandle.getAppHandle().registerReceiver(mockAuthServiceBroadcastReceiver, intentFilter);
    }

    private void unregisterMockAuthServiceBroadcastReceiver() {
        AppHandle.getAppHandle().unregisterReceiver(mockAuthServiceBroadcastReceiver);
    }
}