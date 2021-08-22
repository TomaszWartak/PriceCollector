package com.dev4lazy.pricecollector.utils;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class BroadcastReceiverWrapper {
    BroadcastReceiver broadcastReceiver;
    String expectedAction;
    IntentFilter intentFilter;

    public BroadcastReceiverWrapper(
            BroadcastReceiver broadcastReceiverToSet,
            String actionToSet ) {
        setBroadcastReceiverAndIntentFilter(
                broadcastReceiverToSet,
                actionToSet );
    }

    public void setBroadcastReceiverAndIntentFilter(
            BroadcastReceiver broadcastReceiverToSet,
            String actionToSet ) {
        broadcastReceiver = broadcastReceiverToSet;
        expectedAction = actionToSet;
        intentFilter  = new IntentFilter();
        intentFilter.addAction( expectedAction );
    };

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public String getExpectedAction() {
        return expectedAction;
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
