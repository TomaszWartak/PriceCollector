package com.dev4lazy.pricecollector.unused;

import com.dev4lazy.pricecollector.view.utils.MessageViewWrapper;

/**
 * Klasa służąca do stereowania prezetacją postępu zadań iteracyjnych
 * (np. zapis listy obiektów do bazy).
 * Wykorzystuje dwa interefejsy:
 *      ProgressPresenter - do prezentacji postępu
 *      MessageViewWrapper - do prezentacji informacji opisujących postępu (np. rodzaj zadania).
 */
public class ProgressPresentingManager_Old {

    private ProgressPresenter_old progressPresenter;
    private MessageViewWrapper messageViewWrapper;
    private int resetCounter = 0;

    public ProgressPresentingManager_Old(
            ProgressPresenter_old progressPresenter,
            MessageViewWrapper messageViewWrapper) {
        this.progressPresenter = progressPresenter;
        progressPresenter.setProgressPresentingManager( this );
        this.messageViewWrapper = messageViewWrapper;
    }

    public ProgressPresenter_old getProgressPresenter() {
        return progressPresenter;
    }

    public void startPresenting() {
        progressPresenter.start();
    }

    /**
     * Powtórnie inicjuje ProgressPresenter
     * @param maxValue: maksymalna wartość, jaka ma być zaprezentowana
     * @param toHideWhenFinished:
     *      true: jeśli ProgressPresenter ma zostac ukryty po zakończeniu prezentacji
     *      false: jeśli ma zostać wykorzystany do kolejnej prezentacji postępu.
     */
    public void resetProgressPresenter( int maxValue, boolean toHideWhenFinished  ) {
        progressPresenter.reset( maxValue, toHideWhenFinished );
        resetCounter++;
    }

    public void stopPresenting() {
        progressPresenter.stop();
    }

    public void hideProgressPresenter() {
        progressPresenter.hide();
    }

    public void killProgressPresenter() {
        progressPresenter.kill();
    }

    public void progressPresenterHasBeenHidden() {
        hideMessagePresenter();
    }

    public int getResetCounter() {
        return resetCounter;
    }

    public void setMessageToPresent( String messageToPresent ) {
        messageViewWrapper.setMessage( messageToPresent );
    }

    /**
     * Wyświetla MessageViewWrapper.
     */
    public void showMessagePresenter() {
        messageViewWrapper.show();
    }

    /**
     * Wyświetla MessageViewWrapper z komunikatem messageToPresent
     * @param messageToPresent: komunikat do wyświetlenia.
     */
    public void showMessagePresenter( String messageToPresent ) {
        messageViewWrapper.show( messageToPresent );
    }

    /**
     * Ukrywa MessageViewWrapper.
     */
    public void hideMessagePresenter() {
        messageViewWrapper.hide();
    }

}

