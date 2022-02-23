package com.dev4lazy.pricecollector.view.utils;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppUtils;

public class MessageSupport {

    private MessageViewWrapper messageViewWrapper;

    public MessageSupport( MessageViewWrapper messageViewWrapper ) {
        this.messageViewWrapper = messageViewWrapper;
    }

    public void showMessage( String message ) {
        messageViewWrapper.show( message );
    }

    public void showMessage( int messageID ) {
        messageViewWrapper.show( AppHandle.getHandle().getString(messageID) );
    }

}
