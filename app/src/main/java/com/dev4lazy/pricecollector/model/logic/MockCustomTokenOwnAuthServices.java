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

public class MockCustomTokenOwnAuthServices implements OwnServerAuthServices{

    // Logowanie/wylogowanie - Firebase
    //private CustomTokenFirebaseAuthServices firebaseAuthServices = null;
    private FirebaseAuthServices firebaseAuthServices = CustomTokenFirebaseAuthServices.getInstance();

    // Token Firebase zwracany przez własny serwer logowania, na podstawie klucza prywatnego Firebase
    private String customToken = null;

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

    public MockCustomTokenOwnAuthServices() {
        bindToMockAuthService();
    }

    // wysyła dane logowania do własnego serwera logowania
    @Override
    public void signInOwnServer() /*todo throws FileNotFoundException */ {
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
    public void signOutFromOwnServer() {
        unbindFromMockAuthService();
    }

    // zwraca token Firebase uzyskany z własnego serwera logowania
    public String getCustomToken() {
        return customToken;
    }

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

    private class MockAuthServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DATA_FROM_MOCKAUTH_READY")) {
                customToken = intent.getStringExtra("TOKEN");
                firebaseAuthServices.addCredential("TOKEN", customToken );
                firebaseAuthServices.signInFirebase();            }
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