package com.dev4lazy.pricecollector.view.utils;

import android.content.Context;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.utils.LogoutDialogListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.fragment.app.FragmentActivity;

public class LogoutQuestionDialog {

    private Context context;
    private FragmentActivity activity;

    public LogoutQuestionDialog( Context context, FragmentActivity activity ) {
        this.context = context;
        this.activity = activity;
    }

    public void get() {
        new MaterialAlertDialogBuilder( context, R.style.PC_AlertDialogStyle_Overlay )
                .setTitle("")
                .setMessage(R.string.question_close_app)
                .setPositiveButton( activity.getString(R.string.caption_yes), new LogoutDialogListener( activity ) )
                .setNegativeButton( activity.getString(R.string.caption_no),null)
                .show();
    }
}
