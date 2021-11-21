package com.dev4lazy.pricecollector.unused;

/**
 * Interface wykorzystywany do prezentacji postepu dla zadań wykonywanych w tle
 * (np. zapis listy obiektów do bazy).
 * Zob. implementację -> ProgressBarPresenter
 */
public interface ProgressPresenter_old {

    int DATA_SIZE_UNKNOWN = -1;
    boolean HIDE_WHEN_FINISHED = true;
    boolean DONT_HIDE_WHEN_FINISHED = false;
    ProgressPresenter_old NO_PROGRESS_PRESENTER = null;

    public void setProgressPresentingManager( ProgressPresentingManager_Old progressPresentingManager_old );

    void init( boolean toHideWhenFinished);

    void init( int maxValue, boolean toHideWhenFinished );

    void init( int maxValue, int stepValue, boolean toHideWhenFinished  );

    void reset( int maxValue, boolean toHideWhenFinished ) ;

    void show();

    void start();

    void update();

    void stepUp();

    void stepUp( int otherStepValue );

    void hide();

    void stop();

    void kill();

    public boolean shouldBeHiddenWhenFinished();

    default void aaa() {

    }
}
