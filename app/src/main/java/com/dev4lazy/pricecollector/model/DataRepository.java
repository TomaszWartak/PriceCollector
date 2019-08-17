package com.dev4lazy.pricecollector.model;

public class DataRepository {

    private static DataRepository instance = new DataRepository();

    private DataRepository() {
        // todo incializajca repozytoriów
    }

    public static DataRepository getInstance() {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }

}
