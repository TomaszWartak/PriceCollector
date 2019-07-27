package com.dev4lazy.pricecollector.model.logic;

public class UserPreferences {

    private static final UserPreferences ourInstance = new UserPreferences();

    private String language;

    public static UserPreferences getInstance() {
        return ourInstance;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
