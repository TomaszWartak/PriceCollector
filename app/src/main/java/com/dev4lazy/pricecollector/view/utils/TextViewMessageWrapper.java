package com.dev4lazy.pricecollector.view.utils;

import android.view.View;
import android.widget.TextView;

public class TextViewMessageWrapper implements MessageViewWrapper {

    private TextView textView;

    public TextViewMessageWrapper(TextView textView) {
        this.textView = textView;
    }

    public void setMessage( String message ){
        textView.setText( message );
    }

    @Override
    public void show() {
        if (isTextViewNotNull()) {
            textView.setVisibility( View.VISIBLE );
        }
    }

    @Override
    public void show( String message) {
        if (isTextViewNotNull()) {
            textView.setText( message );
            textView.setVisibility( View.VISIBLE );
        }
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
