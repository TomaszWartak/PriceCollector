package com.dev4lazy.pricecollector.utils;

import android.app.Application;

public class AppHandle extends Application{

    private static AppHandle appHandle = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appHandle = this;
    }

    public static AppHandle getAppHandle() {

        return appHandle;
    }

}
