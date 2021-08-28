package com.dev4lazy.pricecollector.unused;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AppEventsObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void afterApplicationON_CREATE() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void afterApplicationON_STOP() {

    }
}
