package com.dev4lazy.pricecollector.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;

import androidx.annotation.NonNull;

public class NetworkAvailabilityMonitor {

    private static NetworkAvailabilityMonitor instance = new NetworkAvailabilityMonitor();
    private ConnectivityManager connectivityManager = null;
    private NetworkAvailabilityCallback networkAvailabilityCallback = null;
    private boolean networkAvailable = false;

    public NetworkAvailabilityMonitor() {
        connectivityManager = (ConnectivityManager)AppHandle.getHandle().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkAvailabilityCallback = new NetworkAvailabilityCallback();
    }

    public static NetworkAvailabilityMonitor getInstance() {
        if (instance == null) {
            synchronized (NetworkAvailabilityMonitor.class) {
                if (instance == null) {
                    instance = new NetworkAvailabilityMonitor();
                }
            }
        }
        return instance;
    }

    public synchronized boolean isNetworkAvailable() {
        return networkAvailable;
    }

    public void startMonitoring() {
        connectivityManager.registerDefaultNetworkCallback( networkAvailabilityCallback );
    }

    public void stopMonitoring() {
        connectivityManager.unregisterNetworkCallback( networkAvailabilityCallback );
    }

    private synchronized void setNetworkAvailable( boolean available ) {
        networkAvailable = available;
        if (!networkAvailable) {
            AppHandle.getHandle().getMessageSupport().showMessage( R.string.network_not_available );
        }
    }

    private class NetworkAvailabilityCallback extends ConnectivityManager.NetworkCallback {

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            setNetworkAvailable( true );
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            setNetworkAvailable( false );
        }

    }

}
