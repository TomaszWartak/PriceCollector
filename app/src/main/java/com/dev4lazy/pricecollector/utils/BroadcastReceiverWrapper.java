package com.dev4lazy.pricecollector.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Ułatwia korzystanie z BraodcastReceiver.
 */
public class BroadcastReceiverWrapper {

    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    /**
     * Konstruktor
     * @param broadcastReceiverToSet: BroadcastReceiver
     * @param actionsToSet: zestaw akcji, które ma obsługiwać BroadcastReceiver
     */
    public BroadcastReceiverWrapper(
            BroadcastReceiver broadcastReceiverToSet,
            String... actionsToSet ) {
        broadcastReceiver = broadcastReceiverToSet;
        intentFilter  = new IntentFilter();
        for (String action : actionsToSet) {
            intentFilter.addAction(action);
        }
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    public void registerReceiver( Context context ) {
        context.registerReceiver( getBroadcastReceiver(), getIntentFilter() );
    }

    public void unregisterReceiver( Context context ) {
        context.unregisterReceiver( getBroadcastReceiver() );
    }
}
