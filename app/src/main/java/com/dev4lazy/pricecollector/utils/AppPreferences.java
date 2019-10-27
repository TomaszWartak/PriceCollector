package com.dev4lazy.pricecollector.utils;

import android.content.SharedPreferences;

public class AppPreferences {

    public static final int LOCAL_DATA_NOT_INITIALIZED = 0;
    public static final int COUNTRIES_INITIALIZED = 1;
    public static final int COMPANIES_INITIALIZED = 2;
    public static final int OWN_STORES_INITIALIZED = 3;
    public static final int LM_STORES_INITIALIZED = 4;
    public static final int OBI_STORES_INITIALIZED = 5;
    public static final int BRICOMAN_STORES_INITIALIZED = 6;
    public static final int LOCAL_COMPETITORS_STORES_INITIALIZED = 7;
    public static final int COMPETITORS_SLOTS_INITIALIZED = 8;
    public static final int LOCAL_DATA_INITIALIZED = COMPETITORS_SLOTS_INITIALIZED;

// Ustawienia językowe -------------------------------------------------------------------------
    private static AppPreferences instance = new AppPreferences();
// countryName
    private final String COUNTRY_NAME_KEY = "COUNTRY_NAME";
// englishCountryName
    private final String ENGLISH_COUNTRY_NAME_KEY = "ENGLISH_COUNTRY_NAME";
// Lokalna baza danych-------------------------------------------------------------------------
// localDatabaseInitialized
    private final String LOCAL_DATABASE_INITIALIZED_KEY = "LOCAL_DATABASE_INITIALIZED";
// Kontrola etapów inicjalizacji
    private final String INITIALISATION_STAGE_KEY = "INITIALISATION_STAGE";
    private final String ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY = "ANALYSIS_COMPETITORS_SLOTS_INITIALIZED";
    private final String ANALYSIS_COMPETITORS_NUMBER_KEY = "ANALYSIS_COMPETITORS_NUMBER";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public int MAX_ANALYSIS_COMPETITORS_NUMBER = 5; //-1 oznacza dowolną ilość
    private SharedPreferences prefs = null;
    private SharedPreferences.Editor prefsEditor = null;
// todo language
    private String language;

    private AppPreferences() { }

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

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
        prefsEditor = prefs.edit();
    }

    public void commit() {
        prefsEditor.commit();
    }

    public void clear() {
        prefsEditor.clear();
    }

    public String getCountryName() {
        return prefs.getString(COUNTRY_NAME_KEY, "");  // defValue może być null
    }

    public void setCountryName(String value) {
        prefsEditor.putString(COUNTRY_NAME_KEY, value);
        prefsEditor.apply();
    }

    public String getEnglishCountryName() {
        return prefs.getString(ENGLISH_COUNTRY_NAME_KEY, "");  // defValue może być null
    }

    public void setEnglishCountryName(String value) {
        prefsEditor.putString(ENGLISH_COUNTRY_NAME_KEY, value);
        prefsEditor.apply();
    }

    public boolean getLocalDatabaseInitialized() {
        return prefs.getBoolean(LOCAL_DATABASE_INITIALIZED_KEY, false);
    }

// Data ostatniego pobrania danych z serwera ---------------------------------------------------

// Czy od ostaniego pobrania danych zostały zmienione dane lokalne -----------------------------

// Konfiguracja okna ze sklepami konukernycjnymi wybranymi do analizy --------------------------

    public void setLocalDatabaseInitialized(boolean value) {
        prefsEditor.putBoolean(LOCAL_DATABASE_INITIALIZED_KEY, value);
        prefsEditor.apply();
    }

    public int getLocalDatabaseInitialisationStage() {
        return prefs.getInt(INITIALISATION_STAGE_KEY, LOCAL_DATA_NOT_INITIALIZED);
    }

    public void setInitialisationStage(int value) {
        prefsEditor.putInt(INITIALISATION_STAGE_KEY, value);
        prefsEditor.commit();
    }

    public void setMaxAnalysisCompetitorsNumber( int value ) {
        MAX_ANALYSIS_COMPETITORS_NUMBER = value;
    }

    public int getMaxAalysisCompetitorsNumber() {
        return MAX_ANALYSIS_COMPETITORS_NUMBER;
    }

    public boolean getCompetitorsSlotsInitialized() {
        return prefs.getBoolean(ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY, false);
    }

    public void setCompetitorsSlotsInitialized(boolean value) {
        prefsEditor.putBoolean(ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY, value);
        prefsEditor.apply();
    }

    public int getAnalysisCompetitorsNumber() {
        return prefs.getInt(ANALYSIS_COMPETITORS_NUMBER_KEY, 5);
    }

    public void setAnalysisCompetitorsNumber(int value) {
        prefsEditor.putInt(ANALYSIS_COMPETITORS_NUMBER_KEY, value);
        prefsEditor.commit();
    }

}
