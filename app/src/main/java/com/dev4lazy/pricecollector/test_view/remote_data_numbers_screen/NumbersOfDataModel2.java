package com.dev4lazy.pricecollector.test_view.remote_data_numbers_screen;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dev4lazy.pricecollector.remote_model.db.RemoteDatabase;


public class NumbersOfDataModel2 extends AndroidViewModel {

    private LiveData<Integer> numberOfRemoteAnalysis;
    private LiveData<Integer> numberOfRemoteAnalysisRow;
    private LiveData<Integer> numberOfRemoteDepartment;
    private LiveData<Integer> numberOfRemoteEanCode;
    private LiveData<Integer> numberOfRemoteFamily;
    private LiveData<Integer> numberOfRemoteMarket;
    private LiveData<Integer> numberOfRemoteModule;
    private LiveData<Integer> numberOfRemoteSector;
    private LiveData<Integer> numberOfRemoteUser;
    private LiveData<Integer> numberOfRemoteUOProject;

    public NumbersOfDataModel2(Application application) {
        super(application);
        RemoteDatabase remoteDatabase = RemoteDatabase.getInstance();
        numberOfRemoteAnalysis = remoteDatabase.remoteAnalysisDao().getNumberOfLiveData();
        numberOfRemoteAnalysisRow = remoteDatabase.remoteAnalysisRowDao().getNumberOfLiveData();
        numberOfRemoteDepartment = remoteDatabase.remoteDepartmentDao().getNumberOfLiveData();
        numberOfRemoteEanCode = remoteDatabase.remoteEanCodeDao().getNumberOfLiveData();
        numberOfRemoteFamily = remoteDatabase.remoteFamilyDao().getNumberOfLiveData();
        numberOfRemoteMarket = remoteDatabase.remoteMarketDao().getNumberOfLiveData();
        numberOfRemoteModule = remoteDatabase.remoteModuleDao().getNumberOfLiveData();
        numberOfRemoteSector = remoteDatabase.remoteSectorDao().getNumberOfLiveData();
        numberOfRemoteUser = remoteDatabase.remoteUserDao().getNumberOfLiveData();
        numberOfRemoteUOProject = remoteDatabase.remoteUOProjectDao().getNumberOfLiveData();
    }

    public LiveData<Integer> getNumberOfRemoteAnalysis() {
        return numberOfRemoteAnalysis;
    }

    public LiveData<Integer> getNumberOfRemoteAnalysisRow() {
        return numberOfRemoteAnalysisRow;
    }

    public LiveData<Integer> getNumberOfRemoteDepartment() {
        return numberOfRemoteDepartment;
    }

    public LiveData<Integer> getNumberOfRemoteEanCode() {
        return numberOfRemoteEanCode;
    }

    public LiveData<Integer> getNumberOfRemoteFamily() {
        return numberOfRemoteFamily;
    }

    public LiveData<Integer> getNumberOfRemoteMarket() {
        return numberOfRemoteMarket;
    }

    public LiveData<Integer> getNumberOfRemoteModule() {
        return numberOfRemoteModule;
    }

    public LiveData<Integer> getNumberOfRemoteSector() {
        return numberOfRemoteSector;
    }


    public LiveData<Integer> getNumberOfRemoteUser() {
        return numberOfRemoteUser;
    }

    public LiveData<Integer> getNumberOfRemoteUOProject() {
        return numberOfRemoteUOProject;
    }

}
