package com.dev4lazy.pricecollector.utils;

import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_NOT_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_INITIALIZED;

public class AppSettings {

    private static AppSettings instance = new AppSettings();

    private static AppPreferences appPreferences = AppHandle.getHandle().getPrefs();

    public static AppSettings getInstance() {
        if (instance == null) {
            synchronized (AppSettings.class) {
                if (instance == null) {
                    instance = new AppSettings();
                }
            }
        }
        return instance;
    }

    // Inicjalizacja przy pierwszym uruchomieniu
    public void setUp() {
        if (isFirstRun()) {
            setLocale();
            setLocalDatabaseNotInitialized();
            setAnalysisCompetitorsScreen();
        }
    }

    private boolean isFirstRun() {
        return appPreferences.getCountryName().isEmpty();
    }

    private void setLocale( /* todo idLocale */ ) {
        // todo sprawdzenie usatwień lokalizacyjnych i interakcja z użytkownikiem
        appPreferences.setCountryName("Polska");
        appPreferences.setEnglishCountryName("Poland");
        appPreferences.setLanguage("polski");
    }

    public void setLocalDatabaseInitialized() {
        appPreferences.setLocalDatabaseInitialized(true);
        appPreferences.setInitialisationStage(LOCAL_DATA_INITIALIZED);
    }

    private void setLocalDatabaseNotInitialized() {
        appPreferences.setLocalDatabaseInitialized(false);
        appPreferences.setInitialisationStage(LOCAL_DATA_NOT_INITIALIZED);
    }

    private void setAnalysisCompetitorsScreen() {
        appPreferences.setMaxAnalysisCompetitorsNumber(appPreferences.MAX_ANALYSIS_COMPETITORS_NUMBER);
    }

}
