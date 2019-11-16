package com.dev4lazy.pricecollector.view;

// zob. implementację ProgressBarPresenter
public interface ProgressPresenter {

    int DATA_SIZE_UNKNOWN = -1;
    Object NO_PROGRESS_PRESENTER = null;

    void init();

    void init( int endValue );

    void init( int endValue, int stepValue );

    void reset( int endValue ) ;

    void show();

    void start();

    void update();

    void stepUp();

    void stepUp( int otherStepValue );

    void hide();

    void stop();

    void kill();

}
