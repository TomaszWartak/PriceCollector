package com.dev4lazy.pricecollector.model.logic;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_model.db.RemoteAnalysisDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.db.RemoteAnalysisRowDao;
import com.dev4lazy.pricecollector.remote_model.db.RemoteDatabase;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_model.db.RemoteDepartmentDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_model.db.RemoteEanCodeDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteFamily;
import com.dev4lazy.pricecollector.remote_model.db.RemoteFamilyDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteMarket;
import com.dev4lazy.pricecollector.remote_model.db.RemoteMarketDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteModule;
import com.dev4lazy.pricecollector.remote_model.db.RemoteModuleDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;
import com.dev4lazy.pricecollector.remote_model.db.RemoteSectorDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUOProject;
import com.dev4lazy.pricecollector.remote_model.db.RemoteUOProjectDao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.remote_model.db.RemoteUserDao;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteDataRepository {

    private static RemoteDataRepository sInstance;
    private RemoteDatabase remoteDatabase;

    private final RemoteAnalysisDao remoteAnalysisDao = AppHandle.getHandle().getRemoteDatabase().remoteAnalysisDao();
    private final DataOperator<RemoteAnalysis> analyzes = new DataOperator<>(remoteAnalysisDao);

    private final RemoteAnalysisRowDao remoteAnalysisRowDao = AppHandle.getHandle().getRemoteDatabase().remoteAnalysisRowDao();
    private final DataOperator<RemoteAnalysisRow> analysisRows = new DataOperator<>(remoteAnalysisRowDao);

    private final RemoteEanCodeDao remoteEanCodeDao = AppHandle.getHandle().getRemoteDatabase().remoteEanCodeDao();
    private final DataOperator<RemoteEanCode> eanCodes = new DataOperator<>(remoteEanCodeDao);
    
    private final RemoteDepartmentDao departmentDao = AppHandle.getHandle().getRemoteDatabase().remoteDepartmentDao();
    private final DataOperator<RemoteDepartment> departments = new DataOperator<>(departmentDao);

    private final RemoteFamilyDao remoteFamilyDao = AppHandle.getHandle().getRemoteDatabase().remoteFamilyDao();
    private final DataOperator<RemoteFamily> remoteFamilies = new DataOperator<>( remoteFamilyDao );

    private final RemoteMarketDao remoteMarketDao = AppHandle.getHandle().getRemoteDatabase().remoteMarketDao();
    private final DataOperator<RemoteMarket> remoteMarkets = new DataOperator<>( remoteMarketDao );

    private final RemoteModuleDao remoteModuleDao = AppHandle.getHandle().getRemoteDatabase().remoteModuleDao();
    private final DataOperator<RemoteModule> remoteModules = new DataOperator<>( remoteModuleDao );

    private final RemoteSectorDao sectorDao = AppHandle.getHandle().getRemoteDatabase().remoteSectorDao();
    private final DataOperator<RemoteSector> sectors = new DataOperator<>(sectorDao);

    private final RemoteUOProjectDao remoteUOProjectDao = AppHandle.getHandle().getRemoteDatabase().remoteUOProjectDao();
    private final DataOperator<RemoteUOProject> remoteUOProjects = new DataOperator<>( remoteUOProjectDao );

    private final RemoteUserDao userDao = AppHandle.getHandle().getRemoteDatabase().remoteUserDao();
    private final DataOperator<RemoteUser> users = new DataOperator<>(userDao);

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

    public void askRemoteAnalysisRowsCount() {
        getRowsCount();
    }

    private void getRowsCount() {
        new GetRowsCountAsyncTask( remoteAnalysisRowDao ).execute();
    }

    public LiveData<List<RemoteAnalysisRow>> loadRemoteAnalysisRow(final int analysisRowId) {
        return remoteAnalysisRowDao.findByIdLiveData(analysisRowId);
    }

//-----------------------------------------------------------------------------------
// insertAnalysisRow
    public void insertRemoteAnalysisRow(RemoteAnalysisRow remoteAnalysisRow) {
        // RemoteAnalysisRowDao.insert(remoteAnalysisRow);
        insert(remoteAnalysisRow);
    }

    public void insertRemoteAnalysisRows(
        List<RemoteAnalysisRow> remoteAnalysisRows,
        MutableLiveData<Long> result,
        ProgressPresenter progressPresenter ) {
        analysisRows.insertDataList( remoteAnalysisRows, result, progressPresenter );
    }

    private void insert(RemoteAnalysisRow remoteAnalysisRow) {
        new InsertRowAsyncTask(remoteAnalysisRowDao).execute(remoteAnalysisRow);
    }

    public void updateRemoteAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteAnalysisRowDao.update(remoteAnalysisRow);
    }

    public void updateRemoteAnalysisRow(RemoteAnalysisRow remoteAnalysisRow, MutableLiveData<Integer> result ) {
        analysisRows.updateData( remoteAnalysisRow, result );
    }

    public void deleteRemoteAnalysisRow( RemoteAnalysisRow remoteAnalysisRow) {
        remoteAnalysisRowDao.delete(remoteAnalysisRow);
    }

    public void deleteAllRemoteAnalysisRows() {
        remoteAnalysisRowDao.deleteAll();
    }

    public LiveData<List<RemoteAnalysisRow>> getAnalysisRowsLiveData() {
        return mObservableAnalysisRows;
    }

    public void getAllRemoteAnalysisRows( MutableLiveData<List<RemoteAnalysisRow>> result ) {
        analysisRows.getAllData( result );
    }

    public LiveData<List<RemoteAnalysisRow>> loadRemoteAnalysisRows(String queryString) {
        if ((queryString !=null) && (!queryString.isEmpty())) {
            return remoteAnalysisRowDao.getViaQueryLiveData(new SimpleSQLiteQuery(queryString));
        }
        return remoteAnalysisRowDao.getAllLiveData();
    }

    public void getRemoteAnalysisRowViaQuery( String stringQuery, MutableLiveData<List<RemoteAnalysisRow>> resultLD ) {
        new GetRemoteAnalysisRowViaStringQueryTask(remoteAnalysisRowDao, resultLD).execute(stringQuery);
    }

//-----------------------------------------------------------------------------------
    public void insertRemoteAnalysis(RemoteAnalysis remoteAnalysis, MutableLiveData<Long> result ) {
        analyzes.insertData( remoteAnalysis, result );
    }

    public void deleteAllRemoteAnalyzes(MutableLiveData<Integer> result) {
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

    public void countRemoteAnalyzesNewerThen(Date lastCheckDate, MutableLiveData<Integer> result ) {
        List<Object> queryArguments = new ArrayList<>();
        queryArguments.add( lastCheckDate.getTime() );
        String queryStr = new SelectQuery()
                .addCustomColumns(FunctionCall.countAll())
                .addCustomFromTable("analyzes")
                .addCondition( BinaryCondition.greaterThan(new CustomSql("creation_date"), new CustomSql("?") ) )
                .validate()
                .toString();
                                                            // SELECT COUNT(*) FROM analyzes WHERE (creation_date > ?)
        SimpleSQLiteQuery query = new SimpleSQLiteQuery( queryStr, queryArguments.toArray() );
        analyzes.getNumberOfData( query, result);
    }

    public void findRemoteAnalyzesNewerThen(Date lastCheckDate, MutableLiveData<List<RemoteAnalysis>> result ) {
        List<Object> queryArguments = new ArrayList<>();
        queryArguments.add( lastCheckDate.getTime() );
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM analyzes WHERE creation_date > ?"  /**/, queryArguments.toArray() /**/ );
        analyzes.getViaQuery( query, result);
    }

//-----------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------
    public void insertRemoteUser(RemoteUser remoteUser, MutableLiveData<Long> result) {
        users.insertData( remoteUser, result);
    }

    public void insertRemoteUsers(
            List<RemoteUser> remoteUsersList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        users.insertDataList( remoteUsersList, result, progressPresenter );
    }

    public void deleteAllUsers(MutableLiveData<Integer> result) {
        users.deleteAllData(result);
    }

    public void findRemoteUserByLogin(String login, MutableLiveData<List<RemoteUser>> result) {
        users.getViaQuery( "SELECT * from users WHERE login='"+login+"'", result);
    }

//-----------------------------------------------------------------------------------
    public void insertDepartment( RemoteDepartment remoteDepartment, MutableLiveData<Long> result) {
        departments.insertData( remoteDepartment, result);
    }

    public void insertRemoteDepartments(
            List<RemoteDepartment> remoteDepartmentList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        departments.insertDataList( remoteDepartmentList, result, progressPresenter );
    }

    public void getAllRemoteDepartments(MutableLiveData<List<RemoteDepartment>> result ) {
        departments.getAllData( result );
    }

    public void deleteRemoteAllDepartments(MutableLiveData<Integer> result) {
        departments.deleteAllData(result);
    }

//-----------------------------------------------------------------------------------
    public void insertRemoteSector(RemoteSector remoteSector, MutableLiveData<Long> result) {
        sectors.insertData( remoteSector, result);
    }

    public void insertRemoteSectors(
            List<RemoteSector> remoteSectorsList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        sectors.insertDataList(remoteSectorsList, result, progressPresenter );
    }

    public void getAllRemoteSectors(MutableLiveData<List<RemoteSector>> result ) {
        sectors.getAllData( result );
    }
    public void deleteAllRemoteSectors(MutableLiveData<Integer> result) {
        sectors.deleteAllData(result);
    }



//-----------------------------------------------------------------------------------

    public void askRemoteFamiliesNumberOf( MutableLiveData<Integer> result ) {
        remoteFamilies.getNumberOfData( result );
    }

    public void insertRemoteFamily( RemoteFamily remoteFamily, MutableLiveData<Long> result ) {
        remoteFamilies.insertData( remoteFamily, result );
    }

    public void insertRemoteFamilies(
            ArrayList<RemoteFamily> remoteFamiliesList,
            ProgressPresenter progressPresenter) {
        remoteFamilies.insertDataList( remoteFamiliesList, progressPresenter);
    }

    public void insertRemoteFamilies(
            List<RemoteFamily> remoteFamiliesList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter) {
        remoteFamilies.insertDataList( remoteFamiliesList, result, progressPresenter);
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

    public void insertRemoteMarkets(
            ArrayList<RemoteMarket> remoteMarketsList,
            ProgressPresenter progressPresenter) {
        remoteMarkets.insertDataList( remoteMarketsList, progressPresenter);
    }

    public void insertRemoteMarkets(
            List<RemoteMarket> remoteMarketsList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter) {
        remoteMarkets.insertDataList( remoteMarketsList, result, progressPresenter);
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

    public void insertRemoteModules(
            ArrayList<RemoteModule> remoteModulesList,
            ProgressPresenter progressPresenter) {
        remoteModules.insertDataList( remoteModulesList, progressPresenter);
    }

    public void insertRemoteModules(
            List<RemoteModule> remoteModulesList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter) {
        remoteModules.insertDataList( remoteModulesList, result, progressPresenter);
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


    public void insertRemoteUOProjects(
            List<RemoteUOProject> remoteUOProjectsList,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        remoteUOProjects.insertDataList( remoteUOProjectsList, result, progressPresenter );
    }

    public void insertRemoteUOProjects(
            ArrayList<RemoteUOProject> remoteUOProjectsList,
            ProgressPresenter progressPresenter) {
        remoteUOProjects.insertDataList( remoteUOProjectsList, progressPresenter);
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
    public void askRemoteEanCodesNumberOf(MutableLiveData<Integer> result ) {
        eanCodes.getNumberOfData(result);
    }

    public void insertRemoteEanCode(RemoteEanCode eanCode ) {
        eanCodes.insertData( eanCode, null );
    }

    public void insertRemoteEanCode(RemoteEanCode eanCode, MutableLiveData<Long> result) {
        eanCodes.insertData( eanCode, result);
    }


    public void insertRemoteEanCodes(
            List<RemoteEanCode> remoteEanCodes,
            MutableLiveData<Long> result,
            ProgressPresenter progressPresenter ) {
        eanCodes.insertDataList( remoteEanCodes, result, progressPresenter );
    }

    public void getAllRemoteEanCodes(MutableLiveData<List<RemoteEanCode>> result ) {
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

    private class GetRemoteAnalysisRowViaStringQueryTask extends AsyncTask<String, Void, List<RemoteAnalysisRow> > {

        private final RemoteAnalysisRowDao dao;
        private final MutableLiveData<List<RemoteAnalysisRow>> resultLD;

        GetRemoteAnalysisRowViaStringQueryTask(RemoteAnalysisRowDao dao, MutableLiveData<List<RemoteAnalysisRow>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<RemoteAnalysisRow> doInBackground ( String ...params){
            return dao.getViaQuery( new SimpleSQLiteQuery(params[0]) );
        }

        @Override
        protected void onPostExecute( List<RemoteAnalysisRow> result ) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }
}
