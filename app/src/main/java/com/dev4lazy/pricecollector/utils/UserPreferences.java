package com.dev4lazy.pricecollector.utils;

public class UserPreferences {

    private static final UserPreferences ourInstance = new UserPreferences();

    private UserPreferences() {
        // todo odczyt z SharedPreferences i ustawienie kraju
        setCountryName("Polska");
        setCountryEnglishName("Poland");
    }

    public static UserPreferences getInstance() {
        return ourInstance;
    }

    // Ustawienia językowe -------------------------------------------------------------------------
    private String countryName;

    public String getCountryName() { return countryName; }

    public void setCountryName(String countryName) { this.countryName = countryName; }

    private String countryEnglishName;

    public String getCountryEnglishName() { return countryEnglishName; }

    public void setCountryEnglishName(String countryEnglishName) { this.countryEnglishName = countryEnglishName; }
    
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
