package com.dev4lazy.pricecollector.view.utils;

import com.dev4lazy.pricecollector.utils.DoItCallback;

/**
 * Klasa służąca do stereowania prezetacją postępu zadań iteracyjnych
 * (np. zapis listy obiektów do bazy).
 * Wykorzystuje:
 *      klasę ProgressPresenter - do prezentacji postępu
 *      interfejs MessageViewWrapper - do prezentacji informacji opisujących postępu (np. rodzaj zadania).
 *      interfejs AfterAllCalback - do zamknięcia widoku, w którym odbywa się prezentacja.
 */
public class ProgressPresentingManager {

    private ProgressPresenter progressPresenter;
    private MessageViewWrapper messageViewWrapper;
    private DoItCallback afterAllCallback;
    private int resetCounter = 0;

    public ProgressPresentingManager(
            ProgressPresenter progressPresenter,
            MessageViewWrapper messageViewWrapper,
            DoItCallback afterAllCallback ) {
        this.progressPresenter = progressPresenter;
        this.progressPresenter.setProgressPresentingManager( this );
        this.messageViewWrapper = messageViewWrapper;
        this.afterAllCallback = afterAllCallback;
    }

    public ProgressPresenter getProgressPresenterWrapper() {
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
        if (afterAllCallback!=null) {
            afterAllCallback.doIt(null);
        }
    }

    public int getResetCounter() {
        return resetCounter;
    }

    public void setMessageToPresent( String messageToPresent ) {
        if (isMessagePresenterNotNull()) {
            messageViewWrapper.setMessage(messageToPresent);
        }
    }
    /**
     * Wyświetla MessageViewWrapper.
     */
    public void showMessagePresenter() {
        if (isMessagePresenterNotNull()) {
            messageViewWrapper.show();
        }
    }

    /**
     * Wyświetla MessageViewWrapper z komunikatem messageToPresent
     * @param messageToPresent: komunikat do wyświetlenia.
     */
    public void showMessagePresenter( String messageToPresent ) {
        if (isMessagePresenterNotNull()) {
            messageViewWrapper.show( messageToPresent );
        }
    }

    /**
     * Ukrywa MessageViewWrapper.
     */
    public void hideMessagePresenter() {
        if (isMessagePresenterNotNull()) {
            messageViewWrapper.hide();
        }
    }

    public boolean isMessagePresenterNotNull() {
        return messageViewWrapper != null;
    }

}

