package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.db._Dao;

public class Data<D> {

    private _Dao dao;

    // todo usuń private DataInsertedCallback dataInsertedCallback = null;

    Data(_Dao dao) {
        setDao(dao);
    }

    /* todo usuń
    public void setDataInsertedCallback(DataInsertedCallback dataInsertedCallback) {
        this.dataInsertedCallback = dataInsertedCallback;
    }
    */

    public void setDao(_Dao dao) {
        this.dao = dao;
    }

    public void insertData(D data /* todo usuń, int callNr*/ ) {
        insertData(data, null );
    }

    public void insertData(D data, MutableLiveData<Long> result ) {
        new InsertAsyncTask(dao, result).execute(data);
    }

    public void updateData(D data) {
        dao.update(data);
    }

    public void deleteData(D data) {
        dao.delete(data);
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

    private class InsertAsyncTask2 extends AsyncTask<D,Void,Void> {
        private _Dao mAssyncTaskDAO;
        // todo usuń private  int callNr;

        InsertAsyncTask2(_Dao dao /* todo usuń, int callNr */ ) {
            mAssyncTaskDAO = dao;
            //todo usuń this.callNr = callNr;
        }

        @Override
        protected Void doInBackground ( D ...params){
            mAssyncTaskDAO.insert(params[0]);
            return null;
        }

        /* todo usuń
        @Override
        protected void onPostExecute(Void nothing) {
            super.onPostExecute(nothing);
            if (dataInsertedCallback!=null) {
                dataInsertedCallback.afterInsert();
            }
        }
        */
    }

    /* todo ususń
    interface DataInsertedCallback {
        void afterInsert();
    }
    */
}
