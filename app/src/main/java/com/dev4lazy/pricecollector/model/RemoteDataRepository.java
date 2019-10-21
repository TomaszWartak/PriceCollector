package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRowDao;
import com.dev4lazy.pricecollector.remote_data.RemoteDatabase;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartmentDao;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCodeDao;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteSectorDao;
import com.dev4lazy.pricecollector.remote_data.RemoteUser;
import com.dev4lazy.pricecollector.remote_data.RemoteUserDao;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;

public class RemoteDataRepository {

    private static RemoteDataRepository sInstance;
    private RemoteDatabase remoteDatabase;

    private RemoteAnalysisRowDao analysisRowDao;

    private RemoteEanCodeDao remoteEanCodeDao = AppHandle.getHandle().getRemoteDatabase().eanCodeDao();
    private Data<RemoteEanCode> eanCodes = new Data<>(remoteEanCodeDao);
    
    private RemoteUserDao userDao = AppHandle.getHandle().getRemoteDatabase().userDao();
    private Data<RemoteUser> users = new Data<>(userDao);

    private RemoteDepartmentDao departmentDao = AppHandle.getHandle().getRemoteDatabase().departmentDao();
    private Data<RemoteDepartment> departments = new Data<>(departmentDao);

    private RemoteSectorDao sectorDao = AppHandle.getHandle().getRemoteDatabase().sectorDao();
    private Data<RemoteSector> sectors = new Data<>(sectorDao);
    
    private MediatorLiveData<List<RemoteAnalysisRow>> mObservableAnalysisRows;

    private RemoteDataRepository() {
        remoteDatabase = AppHandle.getHandle().getRemoteDatabase();
        analysisRowDao = remoteDatabase.analysisRowDao();
        mObservableAnalysisRows = new MediatorLiveData<>();
        // todo start test
        // List<RemoteAnalysisRow> testAnalysisRows = RemoteAnalysisRowDao.getAllAnalysisRows().getValue();
        //testAnalysisRows = RemoteAnalysisRowDao.getAllAnalysisRowsList();
        // todo end test
        mObservableAnalysisRows.addSource(
            analysisRowDao.getAllAnalysisRows(),
            new Observer<List<RemoteAnalysisRow>>() {
                @Override
                public void onChanged(List<RemoteAnalysisRow> remoteAnalysisRows) {
                    if (remoteDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableAnalysisRows.postValue(remoteAnalysisRows);
                    }
                }
            });
    }

    public static RemoteDataRepository getInstance() {
        if (sInstance == null) {
            synchronized (RemoteDataRepository.class) {
                if (sInstance == null) {
                    sInstance = new RemoteDataRepository();
                }
            }
        }
        return sInstance;
    }

//-----------------------------------------------------------------------------------
// clearDatabaseAsync
    public void clearDatabase() {
        new ClearDatabaseAsyncTask(remoteDatabase).execute();
    }

//-----------------------------------------------------------------------------------
// getAnalysisRowsCount
    public LiveData<Integer> getAnalysisRowsCount() {
        return analysisRowDao.getCountLiveData();
    }

    public void askAnalysisRowsCount(int callNr) {
        getRowsCount(callNr);
    }

    private void getRowsCount(int callNr) {
        new GetRowsCountAsyncTask(analysisRowDao, callNr).execute();
    }

//-----------------------------------------------------------------------------------
// insertAnalysisRow
    public void insertAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        // RemoteAnalysisRowDao.insert(remoteAnalysisRow);
        insert(remoteAnalysisRow);
    }

    private void insert(RemoteAnalysisRow remoteAnalysisRow) {
        new InsertAsyncTask(analysisRowDao).execute(remoteAnalysisRow);
    }

//-----------------------------------------------------------------------------------
    public void updateAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        analysisRowDao.update(remoteAnalysisRow);
    }

    public void deleteAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        analysisRowDao.delete(remoteAnalysisRow);
    }

    public void deleteAllAnalysisRows() {
        analysisRowDao.deleteAll();
    }

    public LiveData<List<RemoteAnalysisRow>> loadAnalysisRow(final int analysisRowId) {
        return analysisRowDao.findAnalysisRowById(String.valueOf(analysisRowId));
    }

    public LiveData<List<RemoteAnalysisRow>> getAnalysisRows() {
        return mObservableAnalysisRows;
    }

    public LiveData<List<RemoteAnalysisRow>> loadAnalysisRows(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return analysisRowDao.getAnalysisRowsViaQuery(new SimpleSQLiteQuery(filter));
        }
        return analysisRowDao.getAllAnalysisRows();
    }

//-----------------------------------------------------------------------------------
    public void insertUser( RemoteUser remoteUser, MutableLiveData<Long> result) {
        users.insertData( remoteUser, result);
    }

    public void deleteAllUsers(MutableLiveData<Integer> result) {
        users.deleteAllData(result);
    }

    public void findUserByLogin( String login, MutableLiveData<List<RemoteUser>> result) {
        users.getViaQuery( "SELECT * from users WHERE login='"+login+"'", result);
    }

//-----------------------------------------------------------------------------------
    public void insertDepartment( RemoteDepartment remoteDepartment, MutableLiveData<Long> result) {
        departments.insertData( remoteDepartment, result);
    }

    public void deleteAllDepartments(MutableLiveData<Integer> result) {
        departments.deleteAllData(result);
    }

//-----------------------------------------------------------------------------------
    public void insertSector( RemoteSector remoteSector, MutableLiveData<Long> result) {
        sectors.insertData( remoteSector, result);
    }

    public void deleteAllSectors(MutableLiveData<Integer> result) {
        sectors.deleteAllData(result);
    }

//-----------------------------------------------------------------------------------
    public void askEanCodesNumberOf(MutableLiveData<Integer> result ) {
        eanCodes.getNumberOfData(result);
    }

    public void insertEanCode( RemoteEanCode eanCode ) {
        eanCodes.insertData( eanCode, null );
    }

    public void insertEanCode( RemoteEanCode eanCode, MutableLiveData<Long> result) {
        eanCodes.insertData( eanCode, result);
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private RemoteDatabase assyncTaskAnalyzesDatabase;

        ClearDatabaseAsyncTask(RemoteDatabase database) {
            assyncTaskAnalyzesDatabase = database;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }
    }

    private static class GetRowsCountAsyncTask extends AsyncTask<Void,Void,Integer> {
        private RemoteAnalysisRowDao mAssyncTaskDAO;
        private int callNr;

        GetRowsCountAsyncTask(RemoteAnalysisRowDao dao, int callNr) {
            mAssyncTaskDAO = dao;
            this.callNr = callNr;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return mAssyncTaskDAO.getCountInteger();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<RemoteAnalysisRow,Void,Void> {
        private RemoteAnalysisRowDao mAssyncTaskDAO;

        InsertAsyncTask(RemoteAnalysisRowDao dao) {
            mAssyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(final RemoteAnalysisRow... params) {
            mAssyncTaskDAO.insert(params[0]);
            return null;
        }
    }
}
