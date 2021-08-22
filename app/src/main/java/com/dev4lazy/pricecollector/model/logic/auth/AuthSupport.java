package com.dev4lazy.pricecollector.model.logic.auth;

import java.util.Map;
import java.util.TreeMap;

/**
Interfejsy obsługujące logowanie.
Wykorzystywane kaskadowo, począwszy od klasy, która np. jedynie pobiera dane logowania od użytkownika,
aż do klasy, która dokonuje autentykacji. Każda z tych klas musi implementować AuthSupport
oraz AuthSupport.LoginCallback.
Aby zaimplementować logowanie:
- należy utworzyć klasę obsługującą konkretny sposób logowania (np. poprzez userid i hasło)
- klasa ta musi implementować następujące interfejsy
    - AuthSupport
        - signIn() - metoda, która odczytuje dane logowania credentials za pomocą metody AuthSupport.getCredential(),
          a następinie wysyła je do usługi autentykacji
        - signOut() - wysyła zlecenie wylogowania do usługi autentykacji; musi wołać AuthSupport.setLoggedIn() z parametrem false,
          jeśli wylogowanie się powiedzie
        - setLoggedIn() - ustawia informację, czy logowanie się powiodło, czy nie
        - isLoggedIn() - podaje informację, czy logowanie się powiodło, czy nie
    - AuthSupport.LoginCallback
        - callIfSucessful() - callback, który jest wołany, gdy logowanie się powiedzie. Musi wołać metodę
          AuthSupport.setLoggedIn() z parametrem true, oraz callback callIfSucessful() klasy wyższej warstwy
        - callIfUnsucessful() - callback, który jest wołany, gdy logowanie się nie powiedzie. Musi wołać metodę
          AuthSupport.setLoggedIn() z parametrem false, oraz callback callIfUnucessful() klasy wyższej warstwy,
          jeśli to konieczne
- w konstruktorze prywatnym klasy statycznej AppAuthSupport
  należy zainicjować włąściwość authSupport, poprzez przyporządkowanie do niej
  instancji klasy obsługującej konkretny sposób logowania
- klasa obsługjąca logowanie musi:
    - wywołać metodę  klasie statycznej AppAuth... O matko... co dalej...
 */
public interface AuthSupport {

//-----------------------------------------------------------------------
// Obsługa danyach uwierzytelniających na zasadzie klucz-wartość,
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

    // Ma podjąć próbę wylogowania
    void signOut();

    void setLoggedIn(Boolean loggedIn);

    boolean isLoggedIn();

//-----------------------------------------------------------------------
// Obsługa callbacku do obsługi rezultatu logowania
    LoginCallback getLoginCallback();

    void setLoginCallback(LoginCallback loginCallback);

    interface LoginCallback {
        void callIfSuccessful();
        void callIfUnsuccessful();
    }

}
