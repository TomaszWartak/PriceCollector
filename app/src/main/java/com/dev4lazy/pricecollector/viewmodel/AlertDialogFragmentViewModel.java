package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;

public class AlertDialogFragmentViewModel extends AndroidViewModel {

    private AlertDialog alertDialog;

    public AlertDialogFragmentViewModel(Application application) {
        super(application);
    }

    public void setAlertDialog( AlertDialog alertDialog ) {
        this.alertDialog = alertDialog;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

}
