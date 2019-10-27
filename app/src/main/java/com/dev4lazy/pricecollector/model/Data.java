package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.view.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

public class Data<D> {

    // TODO Data zrób, jako klasę/interfejs? abstrakcyjną, z której wywodzi się np. RoomData
    // TODO albo bardziej może _Dao (RoomDao, ...)
    private _Dao dao;

    Data(_Dao dao) {
        setDao(dao);
    }

    public void setDao(_Dao dao) {
        this.dao = dao;
    }

    public void getNumberOfData(MutableLiveData<Integer> result ) {
        new GetNumberOfAsyncTask(dao, result).execute();
    }

    public void insertData(D data ) {
        insertData(data, null );
    }

    public void insertData(D data, MutableLiveData<Long> result ) {
        new InsertAsyncTask(dao, result).execute(data);
    }

    public void insertDataList(ArrayList<D> dataList, ProgressPresenter progressPresenter ) {
        // TODO tutaj raczej PagedList...
        new InsertListAsyncTask(dao, progressPresenter, null).execute(dataList);
    }

    public void updateData(D data, MutableLiveData<Integer> resultLD) {
        new UpdateAsyncTask(dao, resultLD).execute(data);
    }

    public void deleteData(D data, MutableLiveData<Integer> resultLD) {
        new DeleteAsyncTask(dao, resultLD).execute(data);
    }

    public void getAllData( MutableLiveData<List<D>> resultLD ) {
        new getAllDataAsyncTask(dao, resultLD).execute();
    }

    public void deleteAllData( MutableLiveData<Integer> resultLD ) {
        new deleteAllDataAsyncTask( dao, resultLD).execute();
    }

    public void getViaQuery(String query, MutableLiveData<List<D>> resultLD) {
        new getViaQueryAsyncTask(dao, resultLD).execute(query);
    }

    public void findDataById(Integer id, MutableLiveData<List<D>> resultLD) {
        new findDataByIdAsyncTask(dao, resultLD).execute(id);
    }

    private class GetNumberOfAsyncTask extends AsyncTask<Void,Void,Integer> {

        private _Dao dao;
        private MutableLiveData<Integer> resultLD;

        GetNumberOfAsyncTask(_Dao dao, MutableLiveData<Integer> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Integer doInBackground ( Void ...params){
            Integer numberOf = dao.getNumberOf();
            return numberOf;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class InsertAsyncTask extends AsyncTask<D,Void,Long> {

        private _Dao dao;
        private MutableLiveData<Long> resultLD;

        InsertAsyncTask(_Dao dao, MutableLiveData<Long> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Long doInBackground ( D ...params){
            Long id = dao.insert(params[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class InsertListAsyncTask extends AsyncTask<ArrayList<D>,Void,Void> {

        private _Dao dao;
        private MutableLiveData<Void> resultLD;
        private ProgressPresenter progressPresenter;


        InsertListAsyncTask(_Dao dao, ProgressPresenter progressPresenter, MutableLiveData<Void> resultLD ) {
            this.dao = dao;
            this.progressPresenter = progressPresenter;
            this.resultLD = resultLD;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressPresenter!=null){
                progressPresenter.start();
            }
        }

        @Override
        protected Void doInBackground ( ArrayList<D> ...params){
            for (D data : params[0]) {
                Long id = dao.insert(data);
                if (progressPresenter!=null){
                    publishProgress();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressPresenter.stepUp();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
            if (progressPresenter!=null) {
                progressPresenter.stop();
            }
        }
    }

    private class UpdateAsyncTask extends AsyncTask<D,Void,Integer> {

        private _Dao dao;
        private MutableLiveData<Integer> resultLD;

        UpdateAsyncTask(_Dao dao, MutableLiveData<Integer> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Integer doInBackground ( D ...params){
            int countOfUpdated = dao.update(params[0]);
            return countOfUpdated;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

     private class DeleteAsyncTask extends AsyncTask<D,Void,Integer> {

        private _Dao dao;
        private MutableLiveData<Integer> resultLD;

        DeleteAsyncTask(_Dao dao, MutableLiveData<Integer> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Integer doInBackground ( D ...params){
            int countOfDeleted = dao.delete(params[0]);
            return countOfDeleted;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class findDataByIdAsyncTask extends AsyncTask< Integer,Void,List<D> >{

        private _Dao dao;
        private MutableLiveData<List<D> > resultLD;

        findDataByIdAsyncTask(_Dao dao, MutableLiveData<List<D> > resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<D> doInBackground ( Integer ...params){
            return dao.findById(params[0]);
        }

        @Override
        protected void onPostExecute(List<D>  result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    public void findDataByName(String name, MutableLiveData<List<D>> resultLD) {
        new findDataByNameAsyncTask(dao, resultLD).execute(name);
    }

     private class findDataByNameAsyncTask extends AsyncTask<String,Void,List<D>>{

        private _Dao dao;
        private MutableLiveData<List<D> > resultLD;

        findDataByNameAsyncTask(_Dao dao, MutableLiveData<List<D>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<D> doInBackground ( String ...params){
            return dao.findByName(params[0]);
        }

        @Override
        protected void onPostExecute(List<D> result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class getAllDataAsyncTask extends AsyncTask< Void,Void,List<D> >{

        private _Dao dao;
        private MutableLiveData<List<D> > resultLD;

        getAllDataAsyncTask(_Dao dao, MutableLiveData<List<D> > resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<D> doInBackground ( Void ...params){
            return dao.getAll();
        }

        @Override
        protected void onPostExecute(List<D>  result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class deleteAllDataAsyncTask extends AsyncTask< Void,Void,Integer >{

        private _Dao dao;
        private MutableLiveData<Integer > resultLD;

        deleteAllDataAsyncTask(_Dao dao, MutableLiveData<Integer> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Integer doInBackground ( Void ...params){
            return dao.deleteAll();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

    private class getViaQueryAsyncTask extends AsyncTask<String,Void,List<D>>{

        private _Dao dao;
        private MutableLiveData<List<D> > resultLD;

        getViaQueryAsyncTask(_Dao dao, MutableLiveData<List<D>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<D> doInBackground ( String ...params){
            return dao.getViaQuery(new SimpleSQLiteQuery(params[0]));
        }

        @Override
        protected void onPostExecute(List<D> result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
        }
    }

}
