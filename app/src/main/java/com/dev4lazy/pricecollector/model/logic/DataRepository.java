package com.dev4lazy.pricecollector.model.logic;

public class DataRepository {

    private static DataRepository instance = null;

    private LocalDataRepository localDataRepository = null;

    private RemoteDataRepository remoteDataRepository = null;

    private DataRepository() {
        localDataRepository = LocalDataRepository.getInstance();
        remoteDataRepository = RemoteDataRepository.getInstance();
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

    public LocalDataRepository getLocalDataRepository() {
        return localDataRepository;
    }

    public RemoteDataRepository getRemoteDataRepository() {
        return remoteDataRepository;
    }

}
