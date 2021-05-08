package com.dev4lazy.pricecollector.model.logic.auth;

public class AppAuthSupport {

    private static AppAuthSupport instance = new AppAuthSupport();

    private AuthSupport authSupport = null;

    private AppAuthSupport() {
        // todo! tutaj możesz zmianić sposób logowania
        this.authSupport = new MockCustomTokenOwnAuthSupport();
    }

    public static AppAuthSupport getInstance() {
        if (instance == null) {
            synchronized (AppAuthSupport.class) {
                if (instance == null) {
                    instance = new AppAuthSupport();
                }
            }
        }
        return instance;
    }

    public AuthSupport getSupport() {
        return authSupport;
    }
}
