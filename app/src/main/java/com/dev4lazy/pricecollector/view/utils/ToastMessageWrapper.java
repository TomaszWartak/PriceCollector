package com.dev4lazy.pricecollector.view.utils;

import android.widget.Toast;

import com.dev4lazy.pricecollector.AppHandle;

public class ToastMessageWrapper implements MessageViewWrapper {
    @Override
    public void setMessage(String message) {
    }

    @Override
    public void show() {
    }

    @Override
    public void show(String message) {
        Toast.makeText(
                AppHandle.getHandle(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hide() {
    }
}
