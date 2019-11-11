package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;
import android.content.DialogInterface;

import androidx.lifecycle.AndroidViewModel;

public class AlertDialogFragmentViewModel extends AndroidViewModel {

    // TODO !! zamiast używać każdego pola oddzielnie, przez ViewModel przekazuj AlertDialog.Builder

    private String title;
    private String message;
    private String positiveButtonLabel;
    private DialogInterface.OnClickListener postiveButtonOnClickListener;
    private String negativeButtonLabel;
    private DialogInterface.OnClickListener negativeButtonOnClickListener;
    private boolean cancelable;

    public AlertDialogFragmentViewModel(Application application) {
        super(application);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPositiveButtonLabel() {
        return positiveButtonLabel;
    }

    public void setPositiveButtonLabel(String positiveButtonLabel) {
        this.positiveButtonLabel = positiveButtonLabel;
    }

    public DialogInterface.OnClickListener getPostiveButtonOnClickListener() {
        return postiveButtonOnClickListener;
    }

    public void setPostiveButtonOnClickListener(DialogInterface.OnClickListener postiveButtonOnClickListener) {
        this.postiveButtonOnClickListener = postiveButtonOnClickListener;
    }

    public String getNegativeButtonLabel() {
        return negativeButtonLabel;
    }

    public void setNegativeButtonLabel(String negativeButtonLabel) {
        this.negativeButtonLabel = negativeButtonLabel;
    }

    public DialogInterface.OnClickListener getNegativeButtonOnClickListener() {
        return negativeButtonOnClickListener;
    }

    public void setNegativeButtonOnClickListener(DialogInterface.OnClickListener negativeButtonOnClickListener) {
        this.negativeButtonOnClickListener = negativeButtonOnClickListener;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }
}
