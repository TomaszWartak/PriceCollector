package com.dev4lazy.pricecollector.utils;

import android.content.Context;
import android.os.BatteryManager;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

public class BatteryStateMonitor implements DoItCallback {

    private static BatteryStateMonitor instance = new BatteryStateMonitor();
    private BroadcastReceiverWrapper broadcastReceiverWrapper;
    private Boolean batteryStateLow = false;
    private final int BATTERY_LOW_LEVEL = 10;

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

    public boolean isBatteryStateLow() {
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
    public void checkBatteryLevelPercentage( Context applicationContext ) {
        batteryStateLow = getBatteryLevelPercentage( applicationContext )<= BATTERY_LOW_LEVEL;
    }

    @Override
    public void doIt( Object... parameters ) {
        batteryStateLow = (Boolean)parameters[0];
    }

}
