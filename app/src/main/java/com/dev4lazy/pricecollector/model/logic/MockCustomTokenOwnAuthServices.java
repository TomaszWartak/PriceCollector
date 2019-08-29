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

public class MockCustomTokenOwnAuthServices /* todo implements MockCustomTokenOwnAuthServices.OnReceiveCallback */{

// ----------------------------------------------------------
// callback do obsługi onReceive() odbiornika
    private OnReceiveCallback onReceiveCallback = null;

// ----------------------------------------------------------
// obsługa Firebase
    // Token Firebase zwracany przez własny serwer logowania, na podstawie klucza prywatnego Firebase
    private String customToken = null;

// ----------------------------------------------------------
// Obsługa usługi udającej serwer logowania i oddającej token
    // messenger do wysyłania danych do serwisu, udającego własny serwer logowania
    private Messenger mockAuthService = null;

    // true, jeśli udało się podłączy do serwisu, udającego własny serwer logowania
    private boolean boundToMockAuthService = false;

    // połączenie z serwisem, udającym własny serwer logowania
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(
                ComponentName className,
                IBinder service) {
            mockAuthService = new Messenger(service);
            setBoundToMockAuthService(true);
            registerMockAuthServiceBroadcastReceiver();
        }

        @Override
        public void onServiceDisconnected(
                ComponentName className) {
            mockAuthService = null;
            setBoundToMockAuthService(false);
            unregisterMockAuthServiceBroadcastReceiver();
        }
    };

    public void setBoundToMockAuthService(Boolean bound) {
        boundToMockAuthService = bound;
    }

    public Boolean isBoundToMockAuthService() {
        return boundToMockAuthService;
    }

    // odbiornik danych z serwisu, udającego własny serwer logowania
    private MockAuthServiceBroadcastReceiver mockAuthServiceBroadcastReceiver = null;

    public void bindToMockAuthService() {
        Intent intent = new Intent();
        intent.setClassName(
                "com.dev4lazy.pricecollectormockauth",
                "com.dev4lazy.pricecollectormockauth.MockAuthService");
        boolean result = AppHandle.getAppHandle().bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindFromMockAuthService() {
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

    public void sendDataToService(Bundle bundle) {
        Message msg = Message.obtain();
        msg.setData(bundle);
        try {
            mockAuthService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

// ----------------------------------------------------------
// obsługa odbiornika
    private class MockAuthServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DATA_FROM_MOCKAUTH_READY")) {
                customToken = intent.getStringExtra("TOKEN");
                onReceiveCallback.callIfDataReceived(customToken);
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

// ----------------------------------------------------------
// callback do obsługi onReceive() odbiornika
    interface OnReceiveCallback {
        void callIfDataReceived(String token);
    }

    void setOnReceiveCallback( OnReceiveCallback onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
    }

}
