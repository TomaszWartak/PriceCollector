package com.dev4lazy.pricecollector.model;

public class RemoteDataRepository {

    private static RemoteDataRepository instance = new RemoteDataRepository();

    private RemoteDataRepository() {
        // todo incializajca baz
    }

    public static RemoteDataRepository getInstance() {
        if (instance == null) {
            synchronized (RemoteDataRepository.class) {
                if (instance == null) {
                    instance = new RemoteDataRepository();
                }
            }
        }
        return instance;
    }


}