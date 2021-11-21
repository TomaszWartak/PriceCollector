package com.dev4lazy.pricecollector.view.utils;

/**
 * Implementacja prezentacji postępu.
 * Kontrolowany przez ProgressPresentingManager
 *
 * Użycie:
 * Na poziomie fragmentu lub aktywności:
 *       new(...)
 * W klasie, gdzie ma być aktualizowany:
 *       start(), aby zainicjować, uruchomić i wyświetlić
 *       stepUp(), aby zaktualizować
 *       stop(), aby zatrzymać
 *       hide(), aby ukryć (i zakończyć, jeśli toHideWhenFinished=true );
 *
 * Uwagi:
 *  - Jeśli w chwili inicjalizacji (new(...)) nie jest znana ilość danych do "przemielenia",
 *      to, gdy wartość ta będzie znana, jeszcze przed wywołaniem start(), należy wywołać reset(...),
 *      gdzie paramterem jest ilość danych.
 *  - Jeśli nie ma potrzeby powoływania nowej instancji, to może być użyty wielokrotnie (bez new(...)),
 *      gdyż, po zakończeiu działania nie nadaje null widokowi odpowiedzialnego za prezentację...
 *      Wystarczy wywołac metodę reset(...).
 *  - Jeśli chcesz zwolnić pamięć widoku odpowiedzialnego za prezentację, to wywołaj metodę kill().
 *
 *  Zob. użycie w DataAccess.InsertListAsyncTask
 */

public class ProgressPresenter {

    private ProgressViewWrapper progressViewWrapper;
    private ProgressPresentingManager progressPresentingManager;

    private int progressValue = 1;
    private int maxValue = -1;
    private int stepValue = 0;
    private int supportCounter = 0;
    private boolean stopped = false;
    private boolean toHideWhenFinished = true;

    public static final ProgressPresenter NO_PROGRESS_PRESENTER = null;
    public static final int DATA_SIZE_UNKNOWN = -1;
    public static final boolean HIDE_WHEN_FINISHED = true;
    public static final boolean DONT_HIDE_WHEN_FINISHED = false;

    /**
     * Konstruktor.
     * @param progressViewWrapper: klasa implementująca interface progressViewWrapper (np. obsługująca
     *      ProgressBar)
     * @param maxValue: maksymalna wartość, jaka ma być zaprezentowana
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public ProgressPresenter(
            ProgressViewWrapper progressViewWrapper,
            int maxValue,
            boolean toHideWhenFinished ) {
        this.progressViewWrapper = progressViewWrapper;
        init( maxValue, toHideWhenFinished );
    }

    /**
     * Ustawia klasę kontrolująca prezentację postępu.
     * @param progressPresentingManager: klasa kontrolująca prezentację postępu
     */
    public void setProgressPresentingManager( ProgressPresentingManager progressPresentingManager ) {
        this.progressPresentingManager = progressPresentingManager;
    }

    /**
     * Inicjuje wartośść maksymalną prezentacji wartością domyślną = 100, oraz ustawia,
     * czy widok odpowiadajacy za prezentację ma zostać ukryty po zakończeniu prezentacji.
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public void init( boolean toHideWhenFinished ) {
        init( 100, toHideWhenFinished );
    }

    /**
     * Inicjuje wartość maksymalną prezentacji, oraz ustawia, czy widok odpowiadajacy za prezentację
     * ma zostać ukryty po zakończeniu prezentacji.
     * @param maxValue: maksymalna wartość prezentacji
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public void init( int maxValue, boolean toHideWhenFinished) {
        if (maxValue >=100) {
            setMaxValueToPresent( 100 );
            init( maxValue, maxValue / 100, toHideWhenFinished );
        } else {
            if (maxValue < 0) {
                maxValue = 0;
            }
            setMaxValueToPresent( maxValue );
            init( maxValue, 1, toHideWhenFinished );
        }
    }

    private void setMaxValueToPresent( int maxValueToPresent ) {
        progressViewWrapper.setMaxValueToPresent( maxValueToPresent );
    }

    /**
     * Inicjuje wartość maksymalną prezentacji, krok postepu prezentacji, po którym ma następować
     * aktualizacja widoku prezentacji, oraz ustawia, czy widok odpowiadajacy za prezentację
     * ma zostać ukryty po zakończeniu prezentacji.
     * @param maxValue: maksymalna wartość prezentacji
     * @param stepValue: krok, po którym następuje aktualizacja widoku prezentacji
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public void init(int maxValue, int stepValue, boolean toHideWhenFinished) {
        progressValue = 1;
        this.maxValue = maxValue;
        this.stepValue = stepValue;
        this.toHideWhenFinished = toHideWhenFinished;
    }

    /**
     * Resetuje parametry prezentacji, czyli ustawia nową wartość maksymalną prezentacji,
     * oraz ustawia, czy widok odpowiadajacy za prezentację  ma zostać ukryty po zakończeniu prezentacji.
     * @param maxValue: maksymalna wartość prezentacji
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public void reset( int maxValue, boolean toHideWhenFinished) {
        init( maxValue, toHideWhenFinished) ;
    }

    /**
     * Uruchamia (wyświetla) prezentację
     */
    public void start() {
        stopped = false;
        update();
        show();
    }

    /**
     * Aktualizuje postęp w widoku odpowiadającuy za prezentację, jeśli prezentacja nie jest zatrzymana
     */
    public void update() {
        if (isNotStopped()) {
            if (progressPresenterIsNotNull()) {
                // TODO masz wyciagnąć stąd ProgressBAr, więc ta metoda nie powinna dotykac progress bara
                progressViewWrapper.setProgress(progressValue);
            }
        }
    }

    /**
     * Wyświetla widoku odpowiadającuy za prezentację
     */
    public void show() {
        if ( progressPresenterIsNotNull() ) {
            progressViewWrapper.show();
        }
    }

    /**
     * Aktualizuje wartość postepu o 1 i woła aktualizację widoku prezentującego postęp.
     */
    public void stepUp() {
        stepUp(1);
    }

    /**
     * Aktualizuje wartość postepu o podaną wartość i woła aktualizację widoku prezentującego postęp.
     * @param otherStepValue: wartośc zmiany postępu
     */
    public void stepUp( int otherStepValue ) {
        supportCounter++;
        if (supportCounter<=stepValue) {
            supportCounter = 0;
            int result = progressValue + otherStepValue;
            if (result< maxValue) {
                progressValue = progressValue + otherStepValue;
            } else {
                progressValue = maxValue;
            }
            update();
        }
    }

    /**
     * Ustawia znacznik zatrzymania prezentacji na true
     */
    public void stop() {
        stopped = true;
    }

    /**
     * Ukrywa widok prezentujacy postep.
     * Woła progressPresentingManager.progressPresenterHasBeenHidden() (np. w celu zamknięcia
     * okna, w którym odbywała się prezentacja.
     */
    public void hide() {
        if ( progressPresenterIsNotNull() ) {
            if ( progressPresenterIsNotNull() ) {
                progressViewWrapper.hide();
            }
            if ( progressPresentingManagerIsNotNull() ) {
                progressPresentingManager.progressPresenterHasBeenHidden();
            }
        }
    }

    /**
     * Ukrywa widok prezentujacy postep, a nastepnie go "zamyka".
     */
    public void kill() {
        hide();
        if ( progressPresenterIsNotNull() ) {
            progressViewWrapper.kill();
            progressViewWrapper = null;
        }
    }

    /**
     * Zwraca infromację, czy progressPresenter powinien być zamknięty po zakońćzeniu prezentacji.
     * @return toHideWhenFinished
     */
    public boolean shouldBeHiddenWhenFinished() {
        return toHideWhenFinished;
    }

    /**
     * Zwraca infromację, czy prezentacja jest zatrzymana
     * @return stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Zwraca infromację, czy prezentacja nie jest zatrzymana
     * @return !stopped
     */
    public boolean isNotStopped() {
        return !stopped;
    }

    /**
     * Ustawia  czy widok odpowiadajacy za prezentację ma zostać ukryty po zakończeniu prezentacji.
     * @param toHideWhenFinished:
     *      true: jeśli ProgressViewWrapper ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
        public void setToHideWhenFinished(boolean toHideWhenFinished) {
        this.toHideWhenFinished = toHideWhenFinished;
    }

    private boolean progressPresenterIsNotNull() {
        return progressViewWrapper !=null;
    }

    private boolean progressPresentingManagerIsNotNull() {
        return progressPresentingManager != null;
    }

}
