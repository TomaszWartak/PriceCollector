package com.dev4lazy.pricecollector.view;

public interface ProgressPresenter {

    Object NO_PROGRESS_PRESENTER = null;

    void init();

    void init( int endValue );

    void init( int endValue, int stepValue );

    void show();

    void start();

    void update();

    void stepUp();

    void stepUp( int otherStepValue );

    void hide();

    void stop();

}
