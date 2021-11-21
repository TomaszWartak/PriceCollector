package com.dev4lazy.pricecollector.utils;

import android.content.DialogInterface;
import android.widget.Toast;

import com.dev4lazy.pricecollector.AppHandle;

import androidx.fragment.app.FragmentActivity;

public class LogoutDialogListener implements DialogInterface.OnClickListener {

    private FragmentActivity activity;

    public LogoutDialogListener(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        finishApp();
    }

    private void finishApp() {
        // TODO promotor: czy to można bardziej elegancko zrobić?
        AppHandle.getHandle().shutdown();
        activity.finishAndRemoveTask();
        System.exit(0);
    }

}
