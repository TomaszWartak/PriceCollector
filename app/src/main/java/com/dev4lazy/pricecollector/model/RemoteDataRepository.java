package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisDao;
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

import java.util.Date;
import java.util.List;

public class RemoteDataRepository {

    private static RemoteDataRepository sInstance;
    private RemoteDatabase remoteDatabase;

    private RemoteAnalysisDao remoteAnalysisDao = AppHandle.getHandle().getRemoteDatabase().remoteAnalysisDao();
    private Data<RemoteAnalysis> analyzes = new Data<>(remoteAnalysisDao);

    private RemoteAnalysisRowDao remoteAnalysisRowDao = AppHandle.getHandle().getRemoteDatabase().analysisRowDao();
    private Data<RemoteAnalysisRow> analysisRows = new Data<>(remoteAnalysisRowDao);

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
        mObservableAnalysisRows = new MediatorLiveData<>();
        // todo start test
        // List<RemoteAnalysisRow> testAnalysisRows = RemoteAnalysisRowDao.getAllAnalysisRows().getValue();
        //testAnalysisRows = RemoteAnalysisRowDao.getAllAnalysisRowsList();
        // todo end test
        mObservableAnalysisRows.addSource(
            remoteAnalysisRowDao.getAllLiveData(),
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
        new ClearDatabaseAsyncTask().execute();
    }

//-----------------------------------------------------------------------------------
// getAnalysisRowsCount
    public LiveData<Integer> getAnalysisRowsCount() {
        return remoteAnalysisRowDao.getNumberOfLiveData();
    }

    public void askAnalysisRowsCount() {
        getRowsCount();
    }

    private void getRowsCount() {
        new GetRowsCountAsyncTask( remoteAnalysisRowDao ).execute();
    }

    public LiveData<List<RemoteAnalysisRow>> loadAnalysisRow(final int analysisRowId) {
        return remoteAnalysisRowDao.findByIdLiveData(analysisRowId);
    }

//-----------------------------------------------------------------------------------
// insertAnalysisRow
    public void insertAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        // RemoteAnalysisRowDao.insert(remoteAnalysisRow);
        insert(remoteAnalysisRow);
    }

    private void insert(RemoteAnalysisRow remoteAnalysisRow) {
        new InsertRowAsyncTask(remoteAnalysisRowDao).execute(remoteAnalysisRow);
    }

//-----------------------------------------------------------------------------------
    public void updateAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteAnalysisRowDao.update(remoteAnalysisRow);
    }

    public void deleteAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteAnalysisRowDao.delete(remoteAnalysisRow);
    }

    public void deleteAllAnalysisRows() {
        remoteAnalysisRowDao.deleteAll();
    }

    public LiveData<List<RemoteAnalysisRow>> getAnalysisRowsLiveData() {
        return mObservableAnalysisRows;
    }

    public void getAllAnalysisRows( MutableLiveData<List<RemoteAnalysisRow>> result ) {
        analysisRows.getAllData( result );
    }

    public LiveData<List<RemoteAnalysisRow>> loadAnalysisRows(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return remoteAnalysisRowDao.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return remoteAnalysisRowDao.getAllLiveData();
    }

    public void getAllDepartments( MutableLiveData<List<RemoteDepartment>> result ) {
        departments.getAllData( result );
    }

//-----------------------------------------------------------------------------------
    public void insertAnalysis( RemoteAnalysis remoteAnalysis, MutableLiveData<Long> result ) {
        analyzes.insertData( remoteAnalysis, result );
    }

    public void deleteAllAnalyzes(MutableLiveData<Integer> result) {
        analyzes.deleteAllData(result);
    }

    public void findAnalysisNewerThen( Date lastCheckDate, MutableLiveData<List<RemoteAnalysis>> result) {
        analyzes.getViaQuery( "SELECT * from analyzes WHERE creation_date>='"+lastCheckDate+"'", result);
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

    public void getAllSectors( MutableLiveData<List<RemoteSector>> result ) {
        sectors.getAllData( result );
    }

    public void deleteAllDepartments(MutableLiveData<Integer> result) {
        departments.deleteAllData(result);
    }

//-----------------------------------------------------------------------------------
    public void insertSector( RemoteSector remoteSector, MutableLiveData<Long> result) {
        sectors.insertData( remoteSector, result);
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private RemoteDatabase asyncTaskAnalyzesDatabase;

        ClearDatabaseAsyncTask() {
            asyncTaskAnalyzesDatabase = AppHandle.getHandle().getRemoteDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }
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

    public void getAllEanCodes( MutableLiveData<List<RemoteEanCode>> result ) {
        eanCodes.getAllData( result );
    }


//-----------------------------------------------------------------------------------

    private static class GetRowsCountAsyncTask extends AsyncTask<Void,Void,Integer> {
        private RemoteAnalysisRowDao mAssyncTaskDAO;

        GetRowsCountAsyncTask(RemoteAnalysisRowDao dao) {
            mAssyncTaskDAO = dao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return mAssyncTaskDAO.getNumberOf();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    private static class InsertRowAsyncTask extends AsyncTask<RemoteAnalysisRow,Void,Void> {
        private RemoteAnalysisRowDao mAssyncTaskDAO;

        InsertRowAsyncTask(RemoteAnalysisRowDao dao) {
            mAssyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(final RemoteAnalysisRow... params) {
            mAssyncTaskDAO.insert(params[0]);
            return null;
        }
    }
}
