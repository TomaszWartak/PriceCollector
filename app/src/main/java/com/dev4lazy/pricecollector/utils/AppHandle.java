package com.dev4lazy.pricecollector.utils;

import android.app.Application;

import com.dev4lazy.pricecollector.model.DataRepository;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.logic.auth.AppAuthSupport;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.remote_data.RemoteDatabase;

public class AppHandle extends Application{

    private static AppHandle appHandle = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appHandle = this;
    }

    public static AppHandle getHandle() { return appHandle; }

    public void shutDown() {
        // todo tutaj zapisanie preferences, bazy danych itp...
        getPrefs().commit();
        getAuthSupport().signOut();
    }

    public AuthSupport getAuthSupport() {
        return AppAuthSupport.getInstance().getSupport();
    }

    public LocalDatabase getLocalDatabase() { return LocalDatabase.getInstance();}

    public RemoteDatabase getRemoteDatabase() { return RemoteDatabase.getInstance();}

    public DataRepository getRepository() { return DataRepository.getInstance(); }

    public AppPreferences getPrefs() { return AppPreferences.getInstance(); }

    public AppSettings getSettings() { return AppSettings.getInstance(); }


}
