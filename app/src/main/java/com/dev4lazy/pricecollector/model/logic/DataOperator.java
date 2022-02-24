package com.dev4lazy.pricecollector.model.logic;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

public class DataOperator<D> {

    /** TO SIĘ NADAJE DO REFAKTORINGU **/

    /* TODO ok DataOperator zrób, jako klasę/interfejs? abstrakcyjną, z której wywodzi się np. RoomDataOperator
        Chyba nie...
     TODO ok albo bardziej może _Dao (RoomDao, ...) <-- TAK!!!
        tylko że w _dao są annotacje Room
        To chyba musiałoby byc tak, że jest jakiś interfejs który dziedziczy po _dao (bez annotacji)
        W sumie tak jest że poszczególne dao dziedziczą po dao, tylko że te annotacje sa w _dao...,
        a nie powinny, żeby to było abstrakcyjne...
        Chyba że jest tak _dao (bez adnotacji) -> RoomDao (z adnotacjami) -> pozostałe Dao
        Zob. też OneNote
        Studia/ .. / Kodowanie/!! Współpraca z biblioteką Room/Stworzenie DAO/!! Przykład  uniwersalnego abstrakcyjnego DAO
    */

    /*
    TODO ok powinno byc: interfejs _Dao określa abstrakcyjne metody operacji na danych
    TODO ok powinno być: interfejs AsyncAccess określa asynchroniczny sposób działania
     !!! niestety tak nie jest - dla każdej moetody dostepu z dao jest oddzielny AsynkTask
     TODO ok:
      - Zrobić abstrakcyjny DataOperator
      - z DataOperator wywieść AssynTaskDataOperator (później ew. ExecutorDataOperator)
      - Zrobić jeden AsyncTaskDataAccess, którym obsługuje się wszystkie metody AssynTaskDataOperatora - LOL
      - AssynTaskDataOperator jest inicjowany dwoma obiekatmi implementującymi odpowiednio:
        - _Dao - który określi metody dostepu do danych (czyli np. RoomDao implments _dao)
        - AsyncAccess - który okresli sposób dostępu asynchornicznego (AsyncTaskDataAccess impl...AsyncAccess)
      - Każdy metoda AssynTaskDataOperatora będzie wołać AsyncAccess, który w doInBackground wywoła odpowiednią metodę _Dao
        UWAGA: skąd ma wiedzieć, którą metodę _Dao ma wywołać? <- trzeba zrobic interfejs DaoMethod
        z metodą doIt() np. GetNumberOfDataDaoMethod.doIt(), w której wołana jest realizacja
        _dao.getNumberOfData(). I wtedy AssynTaskDataOperator w swojej metodzie getNumberOfData()
        woła AsyncAccess.doIt(), które de facto jest wywołaniem GetNumberOfDataDaoMethod.doIt().
        LOL ale wtedy masz naście róznych implmentacji AsyncAccesów... Skąd AssyncTaskDataOperator
        ma wiedzieć, który zawołać? Jak podać właściwy obiekt?
    */

    private _Dao dao;

    DataOperator(_Dao dao) {
        setDao(dao);
    }

    public void setDao(_Dao dao) {
        this.dao = dao;
    }

    public _Dao getDao() {
        return dao;
    }

    public void getNumberOfData(MutableLiveData<Integer> result ) {
        new GetNumberOfAsyncTask(dao, result).execute();
    }

    public void getNumberOfData(SimpleSQLiteQuery query, MutableLiveData<Integer> result ) {
        new GetNumberOfViaQueryAsyncTask(dao, result).execute(query);
    }

    public void insertData(D data ) {
        insertData(data, null );
    }

    public void insertData(D data, MutableLiveData<Long> result ) {
        new InsertAsyncTask(dao, result).execute(data);
    }

    public void insertDataList(ArrayList<D> dataList, ProgressPresenter progressPresenter) {
         insertDataList( dataList, null, progressPresenter);
    }

    public void insertDataList( List<D> dataList, MutableLiveData<Long> resultLD, ProgressPresenter progressPresenter) {
        new InsertListAsyncTask(dao, resultLD, progressPresenter).execute(dataList);
    }
    public void updateData(D data, MutableLiveData<Integer> resultLD) {
        new UpdateAsyncTask(dao, resultLD).execute(data);
    }

    /**
     * W resultLD zwraca ilość usuniętych wierszy
     */
    public void deleteData(D data, MutableLiveData<Integer> resultLD) {
        new DeleteAsyncTask(dao, resultLD).execute(data);
    }

    public void getAllData( MutableLiveData<List<D>> resultLD ) {
        new getAllDataAsyncTask(dao, resultLD).execute();
    }

    public void deleteAllData( MutableLiveData<Integer> resultLD ) {
        new deleteAllDataAsyncTask( dao, resultLD).execute();
    }

    public void getViaQuery(String stringQuery, MutableLiveData<List<D>> resultLD) {
        new getViaStringQueryAsyncTask(dao, resultLD).execute(stringQuery);
    }

    public void getViaQuery(SimpleSQLiteQuery query, MutableLiveData<List<D>> resultLD) {
        new getViaQueryAsyncTask(dao, resultLD).execute( query );
    }

    public void findDataById(Integer id, MutableLiveData<List<D>> resultLD) {
        new findDataByIdAsyncTask(dao, resultLD).execute(id);
    }

    private class GetNumberOfAsyncTask extends AsyncTask<Void,Void,Integer> {

        private final _Dao dao;
        private final MutableLiveData<Integer> resultLD;

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

    private class GetNumberOfViaQueryAsyncTask extends AsyncTask<SimpleSQLiteQuery,Void,Integer> {

        private final _Dao dao;
        private final MutableLiveData<Integer> resultLD;

        GetNumberOfViaQueryAsyncTask(_Dao dao, MutableLiveData<Integer> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected Integer doInBackground ( SimpleSQLiteQuery ...params){
            return dao.getNumberOfViaQuery( params[0] );
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

        private final _Dao dao;
        private final MutableLiveData<Long> resultLD;

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

    private class InsertListAsyncTask extends AsyncTask<List<D>,Void,Long> {

        private final _Dao dao;
        private final MutableLiveData<Long> resultLD;
        private final ProgressPresenter progressPresenter;


        InsertListAsyncTask(_Dao dao, MutableLiveData<Long> resultLD, ProgressPresenter progressPresenter) {
            this.dao = dao;
            this.progressPresenter = progressPresenter;
            this.resultLD = resultLD;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressPresenter !=null){
                progressPresenter.start();
            }
        }

        @Override
        protected Long doInBackground ( List<D> ...params){
            Long id = null;
            for (D data : params[0]) {
                id = dao.insert(data);
                if (progressPresenter !=null){
                    publishProgress();
                }
            }
            return id;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (progressPresenter !=null) {
                progressPresenter.stepUp();
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (resultLD!=null) {
                resultLD.postValue(result);
            }
            if ((progressPresenter !=null) && (progressPresenter.shouldBeHiddenWhenFinished())) {
                progressPresenter.hide();
            }
        }
    }

    private class UpdateAsyncTask extends AsyncTask<D,Void,Integer> {

        private final _Dao dao;
        private final MutableLiveData<Integer> resultLD;

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

        private final _Dao dao;
        private final MutableLiveData<Integer> resultLD;

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

        private final _Dao dao;
        private final MutableLiveData<List<D> > resultLD;

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

        private final _Dao dao;
        private final MutableLiveData<List<D> > resultLD;

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

        private final _Dao dao;
        private final MutableLiveData<List<D> > resultLD;

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

        private final _Dao dao;
        private final MutableLiveData<Integer > resultLD;

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

    private class getViaStringQueryAsyncTask extends AsyncTask<String,Void,List<D>>{

        private final _Dao dao;
        private final MutableLiveData<List<D> > resultLD;

        getViaStringQueryAsyncTask(_Dao dao, MutableLiveData<List<D>> resultLD ) {
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

    private class getViaQueryAsyncTask extends AsyncTask<SimpleSQLiteQuery,Void,List<D>>{

        private final _Dao dao;
        private final MutableLiveData<List<D> > resultLD;

        getViaQueryAsyncTask(_Dao dao, MutableLiveData<List<D>> resultLD ) {
            this.dao = dao;
            this.resultLD = resultLD;
        }

        @Override
        protected List<D> doInBackground ( SimpleSQLiteQuery ...params){
            return dao.getViaQuery(params[0]);
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
