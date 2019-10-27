package com.dev4lazy.pricecollector.view;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarPresenter implements ProgressPresenter {

    private final int startValue = 0;
    private int progressValue = 1;
    private int endValue = -1;
    private int stepValue = 0;
    private int supportCounter = 0;

    private ProgressBar progressBar;

    ProgressBarPresenter(ProgressBar progressBar, int endValue ) {
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
    public void init(int endValue) {
        if (endValue!=100) {
            stepValue = endValue / 100;
            init( endValue, stepValue );
        } else {
            init(endValue, 1);
        }
    }

    @Override
    public void init(int endValue, int stepValue) {
        this.endValue = endValue;
        this.stepValue = stepValue;
    }

    @Override
    public void show() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void start() {
        update();
        show();
    }

    @Override
    public void update() {
        progressBar.setProgress(progressValue);
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
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stop() {
        hide();
    }

}
