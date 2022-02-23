package com.dev4lazy.pricecollector.model.utils;

import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;

public class InitializeAfterLocalDatabaseClearedCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
    @Override
    public void call() {
        LocalDataInitializer.getInstance().initializeLocalDatabase();
    }
}
