package com.dev4lazy.pricecollector.unused;

import android.content.SharedPreferences;

/**
 * AppPrefernces
 * Służy do zapisu i odczytu ustawień w SharedPreferences
 */
public class AppPreferences {

// AppPreferences -----------------------------------------------------------------------------
    private static AppPreferences instance = new AppPreferences();

// SharedPreferences ---------------------------------------------------------------------------
    private SharedPreferences prefs = null;
    private SharedPreferences.Editor prefsEditor = null;



    public static AppPreferences getInstance() {
        if (instance == null) {
            synchronized (AppPreferences.class) {
                if (instance == null) {
                    instance = new AppPreferences();
                }
            }
        }
        return instance;
    }

// AppPreferences -----------------------------------------------------------------------------
    private AppPreferences() { }

    public void commit() {
        prefsEditor.commit();
    }

    public void clear() {
        prefsEditor.clear();
    }

// SharedPreferences ---------------------------------------------------------------------------
    public void setPrefs( SharedPreferences prefs ) {
        this.prefs = prefs;
        prefsEditor = prefs.edit();
    }

}
