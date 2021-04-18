package com.dev4lazy.pricecollector.utils;

import com.dev4lazy.pricecollector.model.logic.User;

import java.util.Date;

import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_NOT_INITIALIZED;

/**
 * AppSettings
 * Służy do zapisu i odczytu ustawień aplikacji.
 * Warstwa nad AppPrefernces, gdzie odbywają się zapisy i odczyty do SharedPreferences
 */
public class AppSettings {

    private static AppSettings instance = new AppSettings();

    private static final AppPreferences appPreferences = AppHandle.getHandle().getPrefs();

    private User user;

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

    public void setUp() {
        if (isFirstRun()) {
            // Inicjalizacja przy pierwszym uruchomieniu
            setLocale();
            setLocalDatabaseNotInitialized();
            setAnalysisCompetitorsScreen();
            setLastAnalysisCreationDate( new Date( 0 ) );
        }
        setSetSet(); // todo lol
    }

    private boolean isFirstRun() {
        return appPreferences.getCountryName().isEmpty();
    }

    private void setLocale( /* todo idLocale */ ) {
        // todo sprawdzenie usatwień lokalizacyjnych i interakcja z użytkownikiem
        appPreferences.saveCountryName("Polska");
        appPreferences.saveEnglishCountryName("Poland");
        appPreferences.setLanguage("polski");
    }

    public void setLocalDatabaseInitialized() {
        appPreferences.saveLocalDatabaseInitialized(true);
        appPreferences.saveInitialisationStage(LOCAL_DATA_INITIALIZED);
    }

    private void setLocalDatabaseNotInitialized() {
        appPreferences.saveLocalDatabaseInitialized(false);
        appPreferences.saveInitialisationStage(LOCAL_DATA_NOT_INITIALIZED);
    }

    private void setAnalysisCompetitorsScreen() {
        appPreferences.saveMaxAnalysisCompetitorsNumber(appPreferences.MAX_ANALYSIS_COMPETITORS_NUMBER);
    }

    private void setSetSet() {

    }

    // Daty ostatniego pobrania danych z serwera ---------------------------------------------------
    public void setLastAnalysisCreationDate(Date date ) {
        appPreferences.saveLastAnalysisDownloadDate( date );
    }

    public Date getLastAnalysisCreationDate() {
        return appPreferences.getLastAnalysisDownloadDate();
    }


    // Użytkownik ----------------------------------------------------------------------------------
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
