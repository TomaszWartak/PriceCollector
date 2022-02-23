package com.dev4lazy.pricecollector;

import android.app.Application;

import com.dev4lazy.pricecollector.model.logic.DataRepository;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.logic.auth.AppAuthSupport;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.remote_model.db.RemoteDatabase;
import com.dev4lazy.pricecollector.utils.AppSettings;
import com.dev4lazy.pricecollector.utils.BatteryStateMonitor;
import com.dev4lazy.pricecollector.utils.NetworkAvailabilityMonitor;
import com.dev4lazy.pricecollector.view.utils.MessageSupport;
import com.dev4lazy.pricecollector.view.utils.ToastMessageWrapper;

import androidx.lifecycle.LifecycleObserver;

public class AppHandle extends Application {

    private static AppHandle appHandle = null;
    private MessageSupport messageSupport = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appHandle = this;
        messageSupport = new MessageSupport( new ToastMessageWrapper() );
    }

    public static AppHandle getHandle() {
        return appHandle;
    }

    public AuthSupport getAuthSupport() {
        return AppAuthSupport.getInstance().getSupport();
    }

    public LocalDatabase getLocalDatabase() {
        return LocalDatabase.getInstance();
    }

    public RemoteDatabase getRemoteDatabase() {
        return RemoteDatabase.getInstance();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance();
    }

    public AppSettings getSettings() {
        return AppSettings.getInstance();
    }

    public NetworkAvailabilityMonitor getNetworkAvailabilityMonitor() {
        return NetworkAvailabilityMonitor.getInstance();
    }

    public BatteryStateMonitor getBatteryStateMonitor() {
        return BatteryStateMonitor.getInstance();
    }

    public MessageSupport getMessageSupport() {
        return messageSupport;
    }

    public void shutdown() {
        getSettings().commit();
        getAuthSupport().signOut();
    }

}
