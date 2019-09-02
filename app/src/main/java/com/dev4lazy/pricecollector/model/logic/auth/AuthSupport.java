package com.dev4lazy.pricecollector.model.logic.auth;

import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

public interface AuthSupport {

//-----------------------------------------------------------------------
// Obsługa danych uwierzytelniających na zasadzie klucz-wartość,
    // Np. dla logowania po identyfikatorze i haśle:
    // klucz = "USER_ID", wartość = "nowak_j"
    // klucz = "USER_PASSWORD", wartość = "xyz"
    // Dla logowania z tokenem:
    // klucz = "TOKEN", wartość = "GjJhjBjhbIUGiuGiugiuGIUg"

    Map<String,String> credentials = new TreeMap<>();

    default void addCredential(String key, String value) {
        credentials.put(key, value);
    }

    default String getCredential(String key) {
        return credentials.get(key);
    }

//-----------------------------------------------------------------------
// Metody związane z procesowaniem logowania

    // Ma podjąć próbę zalogowania na podstawie wcześniej ustawionych danych uwierzytelniających
    void signIn();

    void signOut();

    void setLoggedIn(Boolean loggedIn);

    boolean isLoggedIn();

//-----------------------------------------------------------------------
// Obsługa callbacku do obsługi rezultatu logowania
    LoginCallback getLoginCallback();

    interface LoginCallback {
        void setLoginCallbackService(LoginCallback loginCallback);
        void callIfSucessful();
        void callIfUnsucessful();
    }

}
