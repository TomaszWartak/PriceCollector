package com.dev4lazy.pricecollector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.view.E2_analyzes_list_screen.AnalyzesListFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Klasa, w której zebrałem pomocne metody, które nigdzi indziej nie pasowały.
 * Trochę śmietnik, ale przynajmniej w jednym miejscu... :-P
 */
public class AppUtils {

    public static AppCompatActivity getActivity(Context context) {
        if (context != null) {
            if (context instanceof AppCompatActivity) return (AppCompatActivity) context;
            if (context instanceof ContextWrapper)
                return getActivity(((ContextWrapper) context).getBaseContext());
        };
        return null;
    }

}
