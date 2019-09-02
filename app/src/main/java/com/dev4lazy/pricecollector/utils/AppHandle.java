package com.dev4lazy.pricecollector.utils;

import android.app.Application;

import com.dev4lazy.pricecollector.model.DataRepository;
import com.dev4lazy.pricecollector.model.logic.auth.AppAuthSupport;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;

public class AppHandle extends Application{

    private static AppHandle appHandle = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appHandle = this;
    }

    public static AppHandle getHandle() {

        return appHandle;
    }

    public AuthSupport getAuthSupport() {
        return AppAuthSupport.getInstance().getSupport();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance();
    }

}
