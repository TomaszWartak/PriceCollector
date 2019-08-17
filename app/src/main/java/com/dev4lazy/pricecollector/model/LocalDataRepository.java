package com.dev4lazy.pricecollector.model;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

    private LocalDataRepository() {
        // todo incializajca baz
    }

    public static LocalDataRepository getInstance() {
        if (instance == null) {
            synchronized (LocalDataRepository.class) {
                if (instance == null) {
                    instance = new LocalDataRepository();
                }
            }
        }
        return instance;
    }

}
