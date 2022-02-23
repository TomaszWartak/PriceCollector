package com.dev4lazy.pricecollector.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.ACTION_BATTERY_LOW;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private DoItCallback afterReceivingJob;

    public BatteryLevelReceiver( DoItCallback afterReceivingJob ) {
        this.afterReceivingJob = afterReceivingJob;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        afterReceivingJob.doIt( intent.getAction()==ACTION_BATTERY_LOW );
    }

}