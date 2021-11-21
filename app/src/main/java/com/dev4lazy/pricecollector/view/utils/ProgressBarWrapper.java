package com.dev4lazy.pricecollector.view.utils;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Implementacja prezentacji postępu z wykorzystaniem ProgressBar.
 *
 * Użycie:
 * Kontrolowany przez ProgressPresenter.
 *
 *  Zob. użycie w DataAccess.InsertListAsyncTask
 */
public class ProgressBarWrapper implements ProgressViewWrapper {

    private ProgressBar progressBar;

    /**
     * Przyjmuje widok ProgressBar
     * @param progressBar
     */
    public ProgressBarWrapper(ProgressBar progressBar ) {
        this.progressBar = progressBar;
    }

    /**
     * Ustawia maksymalną wartość dla wskaźnika postępu (np. wielokość listy obiektów do zapsiania w bazie).
     * Ustawiane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     * @param maxValueToPresent
     */
    @Override
    public void setMaxValueToPresent( int maxValueToPresent ) {
        if (progressBarIsNotNull()) {
            progressBar.setMax(maxValueToPresent);
        }
    }

    /**
     * Ustawia aktualną wartość wskaźnika postępu (np. wielokość listy obiektów do zapsiania w bazie).
     * Ustawiane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     * @param progressValue
     */
    @Override
    public void setProgress( int progressValue ) {
        if (progressBarIsNotNull()) {
            progressBar.setProgress( progressValue );
        }
    }

    /**
     * Rozpoczyna wyświetlanie widoku odpowiedzialnego za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    @Override
    public void show() {
        if (progressBarIsNotNull()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Ukrywa widok odpowiedzialny za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    @Override
    public void hide() {
        if (progressBarIsNotNull()) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Nadaje wartość null widokowi odpowiedzialnemu za prezentację wskaźnika postępu.
     * Wołane przez obiekt klasy ProgressPresenter, który kontroluje prezentację.
     */
    @Override
    public void kill() {
        progressBar = null;
    }

    private boolean progressBarIsNotNull() {
        return progressBar != null;
    }

}
