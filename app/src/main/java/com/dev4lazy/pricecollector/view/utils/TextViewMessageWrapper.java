package com.dev4lazy.pricecollector.view.utils;

import android.view.View;
import android.widget.TextView;

public class TextViewMessageWrapper implements MessageViewWrapper {

    private TextView textView;

    public TextViewMessageWrapper(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void setMessage( String message ){
        textView.setText( message );
    }

    @Override
    public void show() {
        if (isTextViewNotNull()) {
            setVisible();
        }
    }

    @Override
    public void show( String message) {
        if (isTextViewNotNull()) {
            textView.setText( message );
            setVisible();
        }
    }

    private void setVisible() {
        if (isTextViewNotVisible()) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isTextViewNotVisible() {
        return textView.getVisibility() != View.VISIBLE;
    }


    @Override
    public void hide() {
        if ( isTextViewNotNull() ) {
            textView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    public boolean isTextViewNotNull() {
        return textView!=null;
    }

}
