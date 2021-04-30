package com.dev4lazy.pricecollector.view;

import android.view.View;
import android.widget.ProgressBar;
/**
 * Użycie:
 * Na poziomie fragmentu lub aktywności:
 *       new(...)
 * W klasie, gdzie ma być użyty:
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
 *  Zob. użycie w Data.InsertListAsyncTask
 */

public class ProgressBarPresenter implements ProgressPresenter {

    private int progressValue = 1;
    private int endValue = -1;
    private int stepValue = 0;
    private int supportCounter = 0;

    private ProgressBar progressBar;

    public ProgressBarPresenter(ProgressBar progressBar, int endValue) {
        this.progressBar = progressBar;
        if (endValue<0) {
            endValue=0;
        }
        init(endValue);
    }

    @Override
    public void init() {
        init( 100 );
    }

    @Override
    public void init( int endValue ) {
        if (endValue!=100) {
            stepValue = endValue / 100;
            init( endValue, stepValue );
        } else {
            init( endValue, 1 );
        }
    }

    @Override
    public void init(int endValue, int stepValue) {
        progressValue = 1;
        this.endValue = endValue;
        this.stepValue = stepValue;
    }

    @Override
    public void reset( int endValue ) {
        init( endValue );
    }

    @Override
    public void show() {
        if ( progressBarIsNotNull() ) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void start() {
        update();
        show();
    }

    @Override
    public void update() {
        if ( progressBarIsNotNull() ) {
            progressBar.setProgress(progressValue);
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
            if (result<endValue) {
                progressValue = progressValue + otherStepValue;
            } else {
                progressValue = endValue;
            }
            update();
        }
    }

    @Override
    public void hide() {
        if ( progressBarIsNotNull() ) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        // todo ? progressBar=null;
    }

    @Override
    public void stop() {
        hide();
    }

    @Override
    public void kill() {
        hide();
        progressBar = null;
    }

    private boolean progressBarIsNotNull() {
        return progressBar!=null;
    }
}
