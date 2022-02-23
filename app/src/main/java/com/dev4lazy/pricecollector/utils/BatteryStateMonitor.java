package com.dev4lazy.pricecollector.utils;

import android.content.Context;
import android.os.BatteryManager;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

public class BatteryStateMonitor /* TODO TEST */ implements DoItCallback /**/ {

    private static BatteryStateMonitor instance = new BatteryStateMonitor();
    private BroadcastReceiverWrapper broadcastReceiverWrapper;
    private Boolean batteryStateLow = false;
    private final int BATTERY_LOW_LEVEL = 15;

    public BatteryStateMonitor() {
        broadcastReceiverWrapper = new BroadcastReceiverWrapper(
                new BatteryLevelReceiver( this ),
                ACTION_BATTERY_LOW, ACTION_BATTERY_OKAY
        );
    }

    public static BatteryStateMonitor getInstance() {
        if (instance == null) {
            synchronized (BatteryStateMonitor.class) {
                if (instance == null) {
                    instance = new BatteryStateMonitor();
                }
            }
        }
        return instance;
    }

    public void startMonitoring( Context applicationContext) {
        broadcastReceiverWrapper.registerReceiver( applicationContext );
    }

    public void stopMonitoring( Context applicationContext) {
        broadcastReceiverWrapper.registerReceiver( applicationContext );
    }

    public synchronized boolean isBatteryStateLow() {
        // TODO XXX batteryStateLow = ((BatteryLevelReceiver)broadcastReceiverWrapper.getBroadcastReceiver()).isBatteryStateLow();
        return batteryStateLow;
    }

    public int getBatteryLevelPercentage( Context applicationContext ) {
        BatteryManager batteryManager = (BatteryManager)applicationContext.getSystemService(BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    /**
     * Sprawdza % naładowania baterii.
     * Jesli jest mniejszy lub równy BATTERY_LOW_LEVEL ustawia batteryStateLow na true.
     * @param applicationContext
     */
    public synchronized void checkBatteryLevelPercentage( Context applicationContext ) {
        batteryStateLow = getBatteryLevelPercentage( applicationContext )<= BATTERY_LOW_LEVEL;
    }

    public synchronized void setBatteryStateLow( boolean batteryStateLow ) {
        this.batteryStateLow = batteryStateLow;
    }

    /* TODO TEST */
    @Override
    public synchronized void doIt( Object... parameters ) {
        batteryStateLow = (Boolean)parameters[0];
        if (batteryStateLow) {
            AppHandle.getHandle().getMessageSupport().showMessage( R.string.battery_state_low );
        }
    }
    /* */

}
