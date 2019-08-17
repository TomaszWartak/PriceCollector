package com.dev4lazy.pricecollector.utils;

public class UserPreferences {

    private static final UserPreferences ourInstance = new UserPreferences();

     public static UserPreferences getInstance() {
        return ourInstance;
    }

    // Ustawienia językowe -------------------------------------------------------------------------
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    // Data ostatniego pobrania danych z serwera ---------------------------------------------------

    // Czy od ostaniego pobrania danych zostały zmienione dane lokalne -----------------------------



}
