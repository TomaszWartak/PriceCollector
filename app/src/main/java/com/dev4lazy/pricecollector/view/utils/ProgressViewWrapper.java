package com.dev4lazy.pricecollector.view.utils;

/**
 * Interface wykorzystywany do opakowania widoku prezentacji postepu np. dla zadań wykonywanych w tle
 * (np. zapis listy obiektów do bazy).
 * Zob. implementację -> ProgressBarWrapper
 */
public interface ProgressViewWrapper {

    /**
     * Ustawia maksymalną wartość prezentacji (np. wielokość listy obiektów do zapisania w bazie)
     * w widoku odpowiadajacym za prezentację wskaźnika postępu (np. ProgressBar).
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     * @param maxValueToPresent
     */
    void setMaxValueToPresent( int maxValueToPresent );

    /**
     * Ustawia aktualną wartość wskaźnika postępu (np. ilość obiektów zapisanych w bazie).
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     * @param progressValue
     */
    void setProgress( int progressValue );

    /**
     * Rozpoczyna wyświetlanie widoku odpowiedzialnego za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    void show();

    /**
     * Ukrywa widok odpowiedzialny za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    void hide();

    /**
     * Nadaje wartość null widokowi odpowiedzialnemu za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    void kill();

}
