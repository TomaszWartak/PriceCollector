package com.dev4lazy.pricecollector.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.dev4lazy.pricecollector.AppHandle;

/**
 * Obsługuje komunikację z serwisami zewnętrznymi
 */
public class ExternalServicesSupport {

    // Context potrzebny do rejestrowania i wyrejestrowania odbiornika
    Context context;

    // połączenie z serwisem
    private ServiceConnection serviceConnection;

    // true, jeśli udało się podłączyć do serwisu
    private boolean boundToService = false;

    // messenger do wysyłania danych do serwisu
    private Messenger messenger = null;

    // Opakowany odbiornik danych z serwisu
    private BroadcastReceiverWrapper broadcastReceiverWrapper;

    public ExternalServicesSupport(Context context) {
        this.context = context;
    }

    private void setBoundToService(Boolean bound) {
        boundToService = bound;
    }

    public Boolean isBoundToService() {
        return boundToService;
    }

    public void bindToService( String packageName, String className ) {
        Intent intent = new Intent();
        intent.setClassName( packageName, className );
        // TODO co z obsługą niepowodzenia?
        boolean result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindFromService() {
        AppHandle.getHandle().unbindService(serviceConnection);
    }

    public void sendDataToService(Bundle bundle) {
        Message msg = Message.obtain();
        msg.setData(bundle);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

// ----------------------------------------------------------
// obsługa odbiornika
    public void setBroadcastReceiverWrapper(BroadcastReceiverWrapper broadcastReceiverWrapperToSet) {
        broadcastReceiverWrapper = broadcastReceiverWrapperToSet;
    }

    private void registerServiceBroadcastReceiver( ) {
        context.registerReceiver(
                broadcastReceiverWrapper.getBroadcastReceiver(),
                broadcastReceiverWrapper.getIntentFilter()
        );
    }

    public void startBroadcastListening() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(
                    ComponentName className,
                    IBinder service) {
                messenger = new Messenger(service);
                setBoundToService(true);
                registerServiceBroadcastReceiver( );
            }

            @Override
            public void onServiceDisconnected(
                    ComponentName className) {
                messenger = null;
                setBoundToService(false);
                unregisterServiceBroadcastReceiver();
            }
        };
    }

    private void unregisterServiceBroadcastReceiver() {
        context.unregisterReceiver(broadcastReceiverWrapper.getBroadcastReceiver());
    }

}
