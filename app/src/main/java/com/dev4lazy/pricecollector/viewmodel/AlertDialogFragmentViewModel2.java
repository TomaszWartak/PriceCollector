package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.AndroidViewModel;

// TODO XXX jest AlertDialogFragmentViewModel i AlertDialogFragmentViewModel2
//  oba są potrzebne?
public class AlertDialogFragmentViewModel2 extends AndroidViewModel {

    private AlertDialog alertDialog;

    public AlertDialogFragmentViewModel2(Application application) {
        super(application);
    }

    public void setAlertDialog( AlertDialog alertDialog ) {
        this.alertDialog = alertDialog;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

}
