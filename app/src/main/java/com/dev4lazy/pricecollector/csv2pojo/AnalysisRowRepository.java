package com.dev4lazy.pricecollector.csv2pojo;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

public class AnalysisRowRepository {

    private static AnalysisRowRepository sInstance;
    private AnalyzesDatabase analyzesDatabase;
    private AnalysisRowDao analysisRowDao;
    private MediatorLiveData<List<AnalysisRow>> mObservableAnalysisRows;

    private AnalysisRowRepository(final AnalyzesDatabase database) {
        analyzesDatabase = database;
        analysisRowDao = analyzesDatabase.analysisRowDao();
        mObservableAnalysisRows = new MediatorLiveData<>();
        // todo start test
        // List<AnalysisRow> testAnalysisRows = AnalysisRowDao.getAllAnalysisRows().getValue();
        //testAnalysisRows = AnalysisRowDao.getAllAnalysisRowsList();
        // todo end test
        mObservableAnalysisRows.addSource(
            analysisRowDao.getAllAnalysisRows(),
            new Observer<List<AnalysisRow>>() {
                @Override
                public void onChanged(List<AnalysisRow> analysisRows) {
                    if (database.getDatabaseCreated().getValue() != null) {
                        mObservableAnalysisRows.postValue(analysisRows);
                    }
                }
            });
    }

    public static AnalysisRowRepository getInstance(final AnalyzesDatabase database) {
        if (sInstance == null) {
            synchronized (AnalysisRowRepository.class) {
                if (sInstance == null) {
                    sInstance = new AnalysisRowRepository(database);
                }
            }
        }
        return sInstance;
    }

//-----------------------------------------------------------------------------------
// clearDatabase
    public void clearDatabase() {
        new ClearDatabaseAsyncTask(analyzesDatabase).execute();
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private AnalyzesDatabase assyncTaskAnalyzesDatabase;

        ClearDatabaseAsyncTask(AnalyzesDatabase database) {
            assyncTaskAnalyzesDatabase = database;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }
    }
/*/-----------------------------------------------------------------------------------

    public void askRowsCount() {
        new RowsCountViewModel().getRowsCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                int i = integer.intValue();
            }
        });
    }

    private class RowsCountViewModel extends ViewModel {

        //private LiveData<Integer> rowsCount;
        //private AnalysisRowDao analysisRowDao;
*//*/
    RowsCountViewModel(AnalysisRowDao analysisRowDao) {
        this.analysisRowDao = analysisRowDao;
    }
*//*
    protected LiveData<Integer> getRowsCount() {
        return getAnalysisRowsCount();
    }

}
*/
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

    private static class GetRowsCountAsyncTask extends AsyncTask<Void,Void,Integer> {
        private AnalysisRowDao mAssyncTaskDAO;
        private int callNr;

        GetRowsCountAsyncTask(AnalysisRowDao dao, int callNr) {
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

//-----------------------------------------------------------------------------------
// insertAnalysisRow
    public void insertAnalysisRow( AnalysisRow analysisRow ) {
        // AnalysisRowDao.insert(analysisRow);
        insert(analysisRow);
    }

    private void insert(AnalysisRow analysisRow) {
        new InsertAsyncTask(analysisRowDao).execute(analysisRow);
    }

    private static class InsertAsyncTask extends AsyncTask<AnalysisRow,Void,Void> {
        private AnalysisRowDao mAssyncTaskDAO;

        InsertAsyncTask(AnalysisRowDao dao) {
            mAssyncTaskDAO = dao;
        }

        @Override
        protected Void doInBackground(final AnalysisRow... params) {
            mAssyncTaskDAO.insert(params[0]);
            return null;
        }
    }

//-----------------------------------------------------------------------------------
    public void updateAnalysisRow( AnalysisRow analysisRow ) {
        analysisRowDao.update(analysisRow);
    }

//-----------------------------------------------------------------------------------
    public void deleteAnalysisRow( AnalysisRow analysisRow ) {
        analysisRowDao.delete(analysisRow);
    }

//-----------------------------------------------------------------------------------
    public void deleteAllAnalysisRows() {
        analysisRowDao.deleteAll();
    }

//-----------------------------------------------------------------------------------
    public LiveData<List<AnalysisRow>> loadAnalysisRow(final int analysisRowId) {
        return analysisRowDao.findAnalysisRowById(String.valueOf(analysisRowId));
    }

//-----------------------------------------------------------------------------------
    public LiveData<List<AnalysisRow>> getAnalysisRows() {
        return mObservableAnalysisRows;
    }

//-----------------------------------------------------------------------------------
    public LiveData<List<AnalysisRow>> loadAnalysisRows(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return analysisRowDao.getAnalysisRowsViaQuery(new SimpleSQLiteQuery(filter));
        }
        return analysisRowDao.getAllAnalysisRows();
    }


}
