package com.dev4lazy.pricecollector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Klasa, w której zebrałem pomocne metody, które nigdzi indziej nie pasowały.
 * Trochę śmietnik, ale przynajmniej w jednym miejscu... :-P
 */
public class AppUtils {

    public static AppCompatActivity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) return (AppCompatActivity) context;
        if (context instanceof ContextWrapper) return getActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }
}
