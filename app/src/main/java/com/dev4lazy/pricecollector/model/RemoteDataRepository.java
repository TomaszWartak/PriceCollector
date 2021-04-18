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
import com.dev4lazy.pricecollector.remote_data.RemoteFamily;
import com.dev4lazy.pricecollector.remote_data.RemoteFamilyDao;
import com.dev4lazy.pricecollector.remote_data.RemoteMarket;
import com.dev4lazy.pricecollector.remote_data.RemoteMarketDao;
import com.dev4lazy.pricecollector.remote_data.RemoteModule;
import com.dev4lazy.pricecollector.remote_data.RemoteModuleDao;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteSectorDao;
import com.dev4lazy.pricecollector.remote_data.RemoteUOProject;
import com.dev4lazy.pricecollector.remote_data.RemoteUOProjectDao;
import com.dev4lazy.pricecollector.remote_data.RemoteUser;
import com.dev4lazy.pricecollector.remote_data.RemoteUserDao;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.view.ProgressPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteDataRepository {

    private static RemoteDataRepository sInstance;
    private RemoteDatabase remoteDatabase;

    private final RemoteAnalysisDao remoteAnalysisDao = AppHandle.getHandle().getRemoteDatabase().remoteAnalysisDao();
    private final Data<RemoteAnalysis> analyzes = new Data<>(remoteAnalysisDao);

    private final RemoteAnalysisRowDao remoteAnalysisRowDao = AppHandle.getHandle().getRemoteDatabase().remoteAnalysisRowDao();
    private final Data<RemoteAnalysisRow> analysisRows = new Data<>(remoteAnalysisRowDao);

    private final RemoteEanCodeDao remoteEanCodeDao = AppHandle.getHandle().getRemoteDatabase().remoteEanCodeDao();
    private final Data<RemoteEanCode> eanCodes = new Data<>(remoteEanCodeDao);
    
    private final RemoteDepartmentDao departmentDao = AppHandle.getHandle().getRemoteDatabase().remoteDepartmentDao();
    private final Data<RemoteDepartment> departments = new Data<>(departmentDao);

    private final RemoteFamilyDao remoteFamilyDao = AppHandle.getHandle().getRemoteDatabase().remoteFamilyDao();
    private final Data<RemoteFamily> remoteFamilies = new Data<>( remoteFamilyDao );

    private final RemoteMarketDao remoteMarketDao = AppHandle.getHandle().getRemoteDatabase().remoteMarketDao();
    private final Data<RemoteMarket> remoteMarkets = new Data<>( remoteMarketDao );

    private final RemoteModuleDao remoteModuleDao = AppHandle.getHandle().getRemoteDatabase().remoteModuleDao();
    private final Data<RemoteModule> remoteModules = new Data<>( remoteModuleDao );

    private final RemoteSectorDao sectorDao = AppHandle.getHandle().getRemoteDatabase().remoteSectorDao();
    private final Data<RemoteSector> sectors = new Data<>(sectorDao);

    private final RemoteUOProjectDao remoteUOProjectDao = AppHandle.getHandle().getRemoteDatabase().remoteUOProjectDao();
    private final Data<RemoteUOProject> remoteUOProjects = new Data<>( remoteUOProjectDao );

    private final RemoteUserDao userDao = AppHandle.getHandle().getRemoteDatabase().remoteUserDao();
    private final Data<RemoteUser> users = new Data<>(userDao);

    private final MediatorLiveData<List<RemoteAnalysisRow>> mObservableAnalysisRows;

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


//-----------------------------------------------------------------------------------
    public void insertAnalysis( RemoteAnalysis remoteAnalysis, MutableLiveData<Long> result ) {
        analyzes.insertData( remoteAnalysis, result );
    }

    public void deleteAllAnalyzes(MutableLiveData<Integer> result) {
        analyzes.deleteAllData(result);
    }

    /*
    public void findAnalyzesNewerThen( Date lastCheckDate, MutableLiveData<List<RemoteAnalysis>> result) {
        // analyzes.getViaQuery( "SELECT * from analyzes WHERE creation_date >= "+lastCheckDate+, result);
        analyzes.getViaQuery( "SELECT * from analyzes WHERE creation_date >= "+lastCheckDate, result);

        // todo?  analyzes.getViaQuery( "SELECT * from analyzes WHERE" , result);
    }

     */
//-----------------------------------------------------------------------------------


    public void findAnalyzesNewerThen( Date lastCheckDate, MutableLiveData<List<RemoteAnalysis>> result ) {
        List<Object> queryArguments = new ArrayList<>();
        queryArguments.add( lastCheckDate.getTime() );
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * from analyzes WHERE creation_date > ?", queryArguments.toArray() );
        analyzes.getViaQuery( query, result);
    }

//-----------------------------------------------------------------------------------

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


    public void getAllDepartments( MutableLiveData<List<RemoteDepartment>> result ) {
        departments.getAllData( result );
    }

    public void deleteAllDepartments(MutableLiveData<Integer> result) {
        departments.deleteAllData(result);
    }

//-----------------------------------------------------------------------------------
    public void insertSector( RemoteSector remoteSector, MutableLiveData<Long> result) {
        sectors.insertData( remoteSector, result);
    }

    public void getAllSectors( MutableLiveData<List<RemoteSector>> result ) {
        sectors.getAllData( result );
    }
    public void deleteAllSectors(MutableLiveData<Integer> result) {
        sectors.deleteAllData(result);
    }



//-----------------------------------------------------------------------------------

    public void askRemoteFamiliesNumberOf( MutableLiveData<Integer> result ) {
        remoteFamilies.getNumberOfData( result );
    }

    public void insertRemoteFamily( RemoteFamily remoteFamily, MutableLiveData<Long> result ) {
        remoteFamilies.insertData( remoteFamily, result );
    }
    public void insertRemoteFamilies( ArrayList<RemoteFamily> remoteFamiliesList, ProgressPresenter progressPresenter ) {
        remoteFamilies.insertDataList( remoteFamiliesList, progressPresenter );
    }

    public void updateRemoteFamily( RemoteFamily remoteFamily, MutableLiveData<Integer> result ) {
        remoteFamilies.updateData( remoteFamily, result );
    }

    public void deleteRemoteFamily( RemoteFamily remoteFamily, MutableLiveData<Integer> result ) {
        remoteFamilies.deleteData( remoteFamily, result );
    }

    public void deleteAllRemoteFamilies( MutableLiveData<Integer> result ) {
        remoteFamilies.deleteAllData( result );
    }

    public void getAllRemoteFamilies( MutableLiveData<List<RemoteFamily>> result ) {
        remoteFamilies.getAllData( result );
    }

    public void findRemoteFamilyById( int id, MutableLiveData<List<RemoteFamily>> result ) {
        remoteFamilies.findDataById( id, result );
    }

    //-----------------------------------------------------------------------------------

    public void askRemoteMarketsNumberOf( MutableLiveData<Integer> result ) {
        remoteMarkets.getNumberOfData( result );
    }

    public void insertRemoteMarket( RemoteMarket remoteMarket, MutableLiveData<Long> result ) {
        remoteMarkets.insertData( remoteMarket, result );
    }

    public void insertRemoteMarkets( ArrayList<RemoteMarket> remoteMarketsList, ProgressPresenter progressPresenter ) {
        remoteMarkets.insertDataList( remoteMarketsList, progressPresenter );
    }

    public void updateRemoteMarket( RemoteMarket remoteMarket, MutableLiveData<Integer> result ) {
        remoteMarkets.updateData( remoteMarket, result );
    }

    public void deleteRemoteMarket( RemoteMarket remoteMarket, MutableLiveData<Integer> result ) {
        remoteMarkets.deleteData( remoteMarket, result );
    }

    public void deleteAllRemoteMarkets( MutableLiveData<Integer> result ) {
        remoteMarkets.deleteAllData( result );
    }

    public void getAllRemoteMarkets( MutableLiveData<List<RemoteMarket>> result ) {
        remoteMarkets.getAllData( result );
    }

    public void findRemoteMarketById( int id, MutableLiveData<List<RemoteMarket>> result ) {
        remoteMarkets.findDataById( id, result );
    }

    //-----------------------------------------------------------------------------------

    public void askRemoteModulesNumberOf( MutableLiveData<Integer> result ) {
        remoteModules.getNumberOfData( result );
    }

    public void insertRemoteModule( RemoteModule remoteModule, MutableLiveData<Long> result ) {
        remoteModules.insertData( remoteModule, result );
    }

    public void insertRemoteModules( ArrayList<RemoteModule> remoteModulesList, ProgressPresenter progressPresenter ) {
        remoteModules.insertDataList( remoteModulesList, progressPresenter );
    }

    public void updateRemoteModule( RemoteModule remoteModule, MutableLiveData<Integer> result ) {
        remoteModules.updateData( remoteModule, result );
    }

    public void deleteRemoteModule( RemoteModule remoteModule, MutableLiveData<Integer> result ) {
        remoteModules.deleteData( remoteModule, result );
    }

    public void deleteAllRemoteModules( MutableLiveData<Integer> result ) {
        remoteModules.deleteAllData( result );
    }

    public void getAllRemoteModules( MutableLiveData<List<RemoteModule>> result ) {
        remoteModules.getAllData( result );
    }

    public void findRemoteModuleById( int id, MutableLiveData<List<RemoteModule>> result ) {
        remoteModules.findDataById( id, result );
    }

    //-----------------------------------------------------------------------------------

    public void askRemoteUOProjectsNumberOf( MutableLiveData<Integer> result ) {
        remoteUOProjects.getNumberOfData( result );
    }

    public void insertRemoteUOProject( RemoteUOProject remoteUOProject, MutableLiveData<Long> result ) {
        remoteUOProjects.insertData( remoteUOProject, result );
    }

    public void insertRemoteUOProjects( ArrayList<RemoteUOProject> remoteUOProjectsList, ProgressPresenter progressPresenter ) {
        remoteUOProjects.insertDataList( remoteUOProjectsList, progressPresenter );
    }

    public void updateRemoteUOProject( RemoteUOProject remoteUOProject, MutableLiveData<Integer> result ) {
        remoteUOProjects.updateData( remoteUOProject, result );
    }

    public void deleteRemoteUOProject(RemoteUOProject remoteUOProject, MutableLiveData<Integer> result ) {
        remoteUOProjects.deleteData( remoteUOProject, result );
    }

    public void deleteAllRemoteUOProjects( MutableLiveData<Integer> result ) {
        remoteUOProjects.deleteAllData( result );
    }

    public void getAllRemoteUOProjects( MutableLiveData<List<RemoteUOProject>> result ) {
        remoteUOProjects.getAllData( result );
    }

    public void findRemoteUOProjectById( int id, MutableLiveData<List<RemoteUOProject>> result ) {
        remoteUOProjects.findDataById( id, result );
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
    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private final RemoteDatabase asyncTaskAnalyzesDatabase;

        ClearDatabaseAsyncTask() {
            asyncTaskAnalyzesDatabase = AppHandle.getHandle().getRemoteDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }
    }

//-----------------------------------------------------------------------------------

    private static class GetRowsCountAsyncTask extends AsyncTask<Void,Void,Integer> {
        private final RemoteAnalysisRowDao mAssyncTaskDAO;

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
        private final RemoteAnalysisRowDao mAssyncTaskDAO;

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
