package com.dev4lazy.pricecollector.utils;

import android.app.Application;
import android.content.DialogInterface;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.DataRepository;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.logic.auth.AppAuthSupport;
import com.dev4lazy.pricecollector.model.logic.auth.AuthSupport;
import com.dev4lazy.pricecollector.remote_model.db.RemoteDatabase;
import com.dev4lazy.pricecollector.view.E2_analyzes_list_screen.AnalyzesListFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AppHandle extends Application {

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

    public void shutdown() {
        // todo A może jest jakaś metoda onDestroy() ? w której może być shutdown() wołany
        //  Jest metoda onTerminate(), ale ona działa tylko na emulatorze (?)
        getSettings().commit();
        getAuthSupport().signOut();
    }

}
