package com.dev4lazy.pricecollector.model.logic;

import android.os.AsyncTask;

import com.dev4lazy.pricecollector.model.db.AnalysisArticleJoinDao;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoinForPricesUpload;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

public class AnlysisArticleJoinDataAccess {

    private AnalysisArticleJoinDao dao;

    public AnlysisArticleJoinDataAccess(AnalysisArticleJoinDao dao) {
       this.dao = dao;
    }

    public void getAnalysisArticleJoinsViaStringQuery(String stringQuery, MutableLiveData<List<AnalysisArticleJoin>> resultLD) {
        new GetAnalysisArticleJoinsViaStringQueryTask(dao, resultLD).execute(stringQuery);
        // new GetAnalysisArticleJoinsViaStringQueryTask(dao, resultLD).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, stringQuery );
    }

    public void getAnalysisArticleJoinsForPricesUploadViaStringQuery(String stringQuery, MutableLiveData<List<AnalysisArticleJoinForPricesUpload>> resultLD) {
        new GetAnalysisArticleJoinsForPricesUploadViaStringQueryTask(dao, resultLD).execute(stringQuery);
        // new GetAnalysisArticleJoinsViaStringQueryTask(dao, resultLD).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, stringQuery );
    }

    private class GetAnalysisArticleJoinsViaStringQueryTask extends AsyncTask<String, Void, List<AnalysisArticleJoin> > {

        private final AnalysisArticleJoinDao dao;
        private final MutableLiveData<List<AnalysisArticleJoin>> resultLD;

        GetAnalysisArticleJoinsViaStringQueryTask(AnalysisArticleJoinDao dao, MutableLiveData<List<AnalysisArticleJoin>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<AnalysisArticleJoin> doInBackground ( String ...params){
            return dao.getAnalysisArticlesJoinsViaQuery( new SimpleSQLiteQuery(params[0]));
        }

        @Override
        protected void onPostExecute( List<AnalysisArticleJoin> result ) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class GetAnalysisArticleJoinsForPricesUploadViaStringQueryTask extends AsyncTask<String, Void, List<AnalysisArticleJoinForPricesUpload> > {

        private final AnalysisArticleJoinDao dao;
        private final MutableLiveData<List<AnalysisArticleJoinForPricesUpload>> resultLD;

        GetAnalysisArticleJoinsForPricesUploadViaStringQueryTask(AnalysisArticleJoinDao dao, MutableLiveData<List<AnalysisArticleJoinForPricesUpload>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<AnalysisArticleJoinForPricesUpload> doInBackground ( String ...params){
            return dao.getAnalysisArticlesJoinsForPricesUploadViaQuery( new SimpleSQLiteQuery(params[0]));
        }

        @Override
        protected void onPostExecute( List<AnalysisArticleJoinForPricesUpload> result ) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

}
