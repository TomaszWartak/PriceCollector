package com.dev4lazy.pricecollector.utils;

import android.content.SharedPreferences;

import com.dev4lazy.pricecollector.model.logic.User;
import com.dev4lazy.pricecollector.model.utils.DateConverter;

import java.util.Date;

/**
 * AppSettings
 * Służy do zapisu i odczytu ustawień aplikacji.
 * Warstwa nad AppPrefernces, gdzie odbywają się zapisy i odczyty do SharedPreferences
 */
public class AppSettings {

    // Ustawienia językowe -------------------------------------------------------------------------
    private final String COUNTRY_NAME_KEY = "COUNTRY_NAME";
    private final String ENGLISH_COUNTRY_NAME_KEY = "ENGLISH_COUNTRY_NAME";

    // Uprawnienia aplikacji -----------------------------------------------------------------------
    private final String ANALYSIS_COMPETITORS_NUMBER_KEY = "ANALYSIS_COMPETITORS_NUMBER";
    public int MAX_ANALYSIS_COMPETITORS_NUMBER = 5; //-1 oznacza dowolną ilość
    private final String LAST_ANALYSIS_DOWNLOAD_DATE_KEY = "LAST_ANALYSIS_DOWNLOAD_DATE";

    // Lokalna baza danych-------------------------------------------------------------------------
    private final String LOCAL_DATABASE_INITIALIZED_KEY = "LOCAL_DATABASE_INITIALIZED";

// Kontrola etapów inicjalizacji

    private final String ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY = "ANALYSIS_COMPETITORS_SLOTS_INITIALIZED";
    private final String INITIALISATION_STAGE_KEY = "INITIALISATION_STAGE";
    public static final int LOCAL_DATA_NOT_INITIALIZED = 0;
    public static final int COUNTRIES_INITIALIZED = 1;
    public static final int COMPANIES_INITIALIZED = 2;
    public static final int OWN_STORES_INITIALIZED = 3;
    public static final int LM_STORES_INITIALIZED = 4;
    public static final int OBI_STORES_INITIALIZED = 5;
    public static final int CASTORAMA_STORES_INITIALIZED = 6;
    public static final int LOCAL_COMPETITORS_STORES_INITIALIZED = 7;
    public static final int COMPETITORS_SLOTS_INITIALIZED = 8;
    public static final int SECTORS_DEPARTMENTS_INITIALIZED = 9;
    public static final int SECTORS_INITIALIZED = 10;
    public static final int DEPARTMENTS_INITIALIZED = 11;
    public static final int DUMMY_FAMILY_INITIALIZED = 12;
    public static final int DUMMY_MARKET_INITIALIZED = 13;
    public static final int DUMMY_MODULE_INITIALIZED = 14;
    public static final int UOPROJECT_INITIALIZED = 15;

    public static final int LOCAL_DATA_INITIALIZED = UOPROJECT_INITIALIZED; // <- !!!!

    // SharedPreferences ---------------------------------------------------------------------------
    private SharedPreferences prefs = null;
    private SharedPreferences.Editor prefsEditor = null;

    // todo language
    private String language;

    private static AppSettings instance = new AppSettings();

    // todo private static final AppPreferences appPreferences = AppHandle.getHandle().getPrefs();

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

// SharedPreferences ---------------------------------------------------------------------------
    public void setPrefs( SharedPreferences prefs ) {
        this.prefs = prefs;
        prefsEditor = prefs.edit();
    }
    public void commit() {
        prefsEditor.commit();
    }

// ----------------------------------------------------------------------------------------------
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
        return getCountryName().isEmpty();
    }

    private void setLocale( /* todo idLocale */ ) {
        // todo sprawdzenie usatwień lokalizacyjnych i interakcja z użytkownikiem
        saveCountryName("Polska");
        saveEnglishCountryName("Poland");
        setLanguage("polski");
    }

    public void setLocalDatabaseInitialized() {
        saveLocalDatabaseInitialized(true);
        saveInitialisationStage(LOCAL_DATA_INITIALIZED);
    }

    private void setLocalDatabaseNotInitialized() {
        saveLocalDatabaseInitialized(false);
        saveInitialisationStage(LOCAL_DATA_NOT_INITIALIZED);
    }

    private void setAnalysisCompetitorsScreen() {
        saveMaxAnalysisCompetitorsNumber(MAX_ANALYSIS_COMPETITORS_NUMBER);
    }

    private void setSetSet() {

    }

    // Daty ostatniego pobrania danych z serwera ---------------------------------------------------
    public void setLastAnalysisCreationDate(Date date ) {
        saveLastAnalysisDownloadDate( date );
    }

    public Date getLastAnalysisCreationDate() {
        return getLastAnalysisDownloadDate();
    }


    // Użytkownik ----------------------------------------------------------------------------------
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Uprawnienia aplikacji -----------------------------------------------------------------------
    public void saveFirstTimeAskingPermission(String permission, boolean isFirstTime ) {
        prefsEditor.putBoolean( permission, isFirstTime );
        prefsEditor.commit();
    }

    public boolean isFirstTimeAskingPermission( String permission ) {
        return prefs.getBoolean( permission,true );
    }

    // Ustawienia językowe -------------------------------------------------------------------------
    public String getLanguage() {
        // todo prefsEditor.get...
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountryName() {
        return prefs.getString( COUNTRY_NAME_KEY, "");  // defValue może być null
    }

    public void saveCountryName(String value) {
        prefsEditor.putString( COUNTRY_NAME_KEY, value);
        prefsEditor.apply();
    }

    public String getEnglishCountryName() {
        return prefs.getString( ENGLISH_COUNTRY_NAME_KEY, "");  // defValue może być null
    }

    public void saveEnglishCountryName(String value) {
        prefsEditor.putString(ENGLISH_COUNTRY_NAME_KEY, value);
        prefsEditor.apply();
    }

    // Lokalna baza danych-------------------------------------------------------------------------
    public boolean isLocalDatabaseInitialized() {
        return prefs.getBoolean(LOCAL_DATABASE_INITIALIZED_KEY, false);
    }

    public boolean isLocalDatabaseNotInitialized() {
        return !isLocalDatabaseInitialized();
    }

    public void saveLocalDatabaseInitialized(boolean value) {
        prefsEditor.putBoolean(LOCAL_DATABASE_INITIALIZED_KEY, value);
        prefsEditor.apply();
    }

    public int getLocalDatabaseInitialisationStage() {
        return prefs.getInt(INITIALISATION_STAGE_KEY, LOCAL_DATA_NOT_INITIALIZED);
    }

    public void saveInitialisationStage(int value) {
        prefsEditor.putInt(INITIALISATION_STAGE_KEY, value);
        prefsEditor.commit();
    }

// Daty ostatniego pobrania danych z serwera ---------------------------------------------------

    public void saveLastAnalysisDownloadDate( Date date ) {
        prefsEditor.putLong( LAST_ANALYSIS_DOWNLOAD_DATE_KEY, new DateConverter().date2Long( date ));
        prefsEditor.commit();
    }

    public Date getLastAnalysisDownloadDate() {
        long longDate = prefs.getLong( LAST_ANALYSIS_DOWNLOAD_DATE_KEY, 0L);
        return new DateConverter().long2Date( longDate );
    }

// Czy od ostaniego pobrania danych zostały zmienione dane lokalne -----------------------------

// Konfiguracja okna ze sklepami konukernycjnymi wybranymi do analizy --------------------------

    public void saveMaxAnalysisCompetitorsNumber( int value ) {
        MAX_ANALYSIS_COMPETITORS_NUMBER = value; // <-- todo to jest wartość "stałej", więc raczej do AppSettings
    }

    // TODO ostatni raz, kiedy tu zaglądałem to poniższe metody (do tagu XXX) nie były używane
    public int getMaxAalysisCompetitorsNumber() {
        return MAX_ANALYSIS_COMPETITORS_NUMBER;
    }

    public boolean isCompetitorsSlotsInitialized() {
        return prefs.getBoolean(ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY, false);
    }

    public void saveCompetitorsSlotsInitialized(boolean value) {
        prefsEditor.putBoolean(ANALYSIS_COMPETITORS_SLOTS_INITIALIZED_KEY, value);
        prefsEditor.apply();
    }

    public int getAnalysisCompetitorsNumber() {
        return prefs.getInt(ANALYSIS_COMPETITORS_NUMBER_KEY, 5);
    }

    public void saveAnalysisCompetitorsNumber(int value) {
        prefsEditor.putInt(ANALYSIS_COMPETITORS_NUMBER_KEY, value);
        prefsEditor.commit();
    }


}
