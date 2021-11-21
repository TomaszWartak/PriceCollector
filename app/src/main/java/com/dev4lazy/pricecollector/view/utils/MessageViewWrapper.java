package com.dev4lazy.pricecollector.view.utils;

/**
 * Interface wykorzystywany do opakowania widoku prezentacji komunikatu.
 * Zob. implementację -> TextViewMessageWrapper
 */
public interface MessageViewWrapper {

    /**
     * Ustawia komuniakt, który ma zostać zaprezentowany.
     * Wołany przez obiekt klasy ProgressPresentingManager, który kontroluje prezentację.
     * @param message
     */
    void setMessage( String message );

    /**
     * Rozpoczyna wyświetlanie widoku odpowiedzialnego za prezentację komunikatu.
     * Widok wyświetla napis ustawiony za pomocą metody setMessage()
     * Wołany przez obiekt klasy ProgressPresentingManager, który kontroluje prezentację.
     */
    void show();

    /**
     * Ustawia komuniakt, który ma zostać zaprezentowany oraz rozpoczyna wyświetlanie
     * widoku odpowiedzialnego za prezentację komunikatu.
     * Wołany przez obiekt klasy ProgressPresentingManager, który kontroluje prezentację.
     * @param message
     */
    void show( String message );

    /**
     * Ukrywa widok odpowiedzialny za prezentację komunikatu.
     * Wołany przez obiekt klasy ProgressPresentingManager, który kontroluje prezentację.
     */
    void hide();

}
