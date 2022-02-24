package com.dev4lazy.pricecollector.unused;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Implementacja prezentacji postępu z wykorzystaniem ProgressBar.
 *
 * Użycie:
 * Na poziomie fragmentu lub aktywności:
 *       new(...)
 * W klasie, gdzie ma być aktualizowany:
 *       start()
 *       stepUp()
 *       stop()
 *
 * Uwagi:
 *  - Jeśli w chwili inicjalizacji (new(...)) nie jest znana ilość danych do "przemielenia",
 *      to, gdy wartość ta będzie znana, jeszcze przed wywołaniem start(), należy wywołać reset(...),
 *      gdzie paramterem jest ilość danych.
 *  - Jeśli nie ma potrzeby powoływania nowej instancji, to może być użyty wielokrotnie (bez new(...)),
 *      gdyż, po zakończeiu działania nie nadaje null progressBarowi... Wystarczy wywołac metodę reset(...).
 *  - Jeśli chcesz zwolnić pamięć progressBara, to wywołaj metodę kill()
 *
 *  Zob. użycie w DataAccess.InsertListAsyncTask
 */

public class ProgressBarPresenter_old implements ProgressPresenter_old {

    private ProgressPresentingManager_Old progressPresentingManager;
    private int progressValue = 1;
    private int maxValue = -1;
    private int stepValue = 0;
    private int supportCounter = 0;
    private boolean stopped = false;
    private boolean toHideWhenFinished = true;

    private ProgressBar progressBar;

    /**
     *
     * @param progressBar: widget ProgressBar
     * @param maxValue: maksymalna wartość, jaka ma być zaprezentowana
     * @param toHideWhenFinished:
     *      true: jeśli ProgressPresenter ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public ProgressBarPresenter_old(ProgressBar progressBar, int maxValue, boolean toHideWhenFinished ) {
        this.progressBar = progressBar;
        init( maxValue, toHideWhenFinished );
    }


    @Override
    public void setProgressPresentingManager( ProgressPresentingManager_Old progressPresentingManager ) {
        this.progressPresentingManager = progressPresentingManager;
    }

    @Override
    public void init( boolean toHideWhenFinished ) {
        init( 100, toHideWhenFinished );
    }

    @Override
    public void init( int maxValue, boolean toHideWhenFinished) {
        if (maxValue >=100) {
            progressBar.setMax( 100 );
            stepValue = maxValue / 100;
            init( maxValue, stepValue, toHideWhenFinished );
        } else {
            if (maxValue < 0) {
                maxValue = 0;
            }
            progressBar.setMax( maxValue );
            init( maxValue, 1, toHideWhenFinished );
        }
    }

    @Override
    public void init(int maxValue, int stepValue, boolean toHideWhenFinished) {
        progressValue = 1;
        this.maxValue = maxValue;
        this.stepValue = stepValue;
        this.toHideWhenFinished = toHideWhenFinished;
    }

    @Override
    public void reset( int maxValue, boolean toHideWhenFinished) {
        init( maxValue, toHideWhenFinished);
    }

    @Override
    public void start() {
        stopped = false;
        update();
        show();
    }

    @Override
    public void update() {
        if (isNotStopped()) {
            if (progressBarIsNotNull()) {
                progressBar.setProgress(progressValue);
            }
        }
    }

    @Override
    public void show() {
        if ( progressBarIsNotNull() ) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stepUp() {
        stepUp(1);
    }

    @Override
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

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public void hide() {
        if ( progressBarIsNotNull() ) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            if (progressPresentingManagerIsNotNull()) {
                progressPresentingManager.progressPresenterHasBeenHidden();
            }
        }
    }

    @Override
    public void kill() {
        hide();
        progressBar = null;
    }

    @Override
    public boolean shouldBeHiddenWhenFinished() {
        return toHideWhenFinished;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isNotStopped() {
        return !stopped;
    }

    public void setToHideWhenFinished(boolean toHideWhenFinished) {
        this.toHideWhenFinished = toHideWhenFinished;
    }

    private boolean progressBarIsNotNull() {
        return progressBar!=null;
    }

    public boolean progressPresentingManagerIsNotNull() {
        return progressPresentingManager != null;
    }

}
