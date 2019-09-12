package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db.CompanyDao;
import com.dev4lazy.pricecollector.model.db.CountryDao2;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.db.OwnStoreDao;
import com.dev4lazy.pricecollector.model.db.StoreDao;
import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

//-----------------------------------------------------------------------
// OwnStore
private OwnStoreDao ownStoreDao = AppHandle.getHandle().getLocalDatabase().ownStoreDao();

    public static LocalDataRepository getInstance() {
        if (instance == null) {
            synchronized (LocalDataRepository.class) {
                if (instance == null) {
                    instance = new LocalDataRepository();
                }
            }
        }
        return instance;
    }
    private Data<OwnStore> ownStores = new Data<>(ownStoreDao);
//-----------------------------------------------------------------------
// Store
private StoreDao storeDao = AppHandle.getHandle().getLocalDatabase().storeDao();
    private Data<Store> stores = new Data<>(storeDao);
//-----------------------------------------------------------------------------------


    //**********************************************************************
    private void initializeStores(){

    }

    private void initializeDepartments(){

    }

    private void initializeFamilies(){

    }

    private void initializeModules(){

    }

    private void initializeMarkets(){

    }

    private void initializeSectores(){

    }

    private void initializeUOProjects(){

    }

//-----------------------------------------------------------------------
// Analysis

//-----------------------------------------------------------------------
// Article

//-----------------------------------------------------------------------
// Company
    private CompanyDao companyDao = AppHandle.getHandle().getLocalDatabase().companyDao();

    private Data<Company> companies = new Data<>(companyDao);

    private LocalDataRepository() {

    }
    
//-----------------------------------------------------------------------------------
// clearDatabaseAsync
    public void clearDatabase(AfterDatabaseClearedCallback callback) {
        new ClearDatabaseAsyncTask(
                AppHandle.getHandle().getLocalDatabase(),
                callback
        ).execute();
    }

    public void insertCompany( Company company, MutableLiveData<Long> result ) {
        companies.insertData(company,result);
    }

    public void updateCompany( Company company, MutableLiveData<Integer> result  ) {
        companies.updateData(company, result);
    }

    public void deleteCompany( Company company, MutableLiveData<Integer> result   ) {
        companies.deleteData(company, result);
    }

    /* usuń ?
    public LiveData<List<Company>> getCompanyByIdLD(int companyId) {
        return companyDao.findByIdLD(companyId);
    }

    public LiveData<List<Company>> getCompanyByNameLD(String companyName) {
        return companyDao.findByNameLD(companyName);
    }
    */

    public LiveData<List<Company>> getAllCompaniesLD() {
        return companyDao.getAll();
    }

    public LiveData<List<Company>> getFilteredCompaniesLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return companyDao.getViaQuery(new SimpleSQLiteQuery(filter));
        }
        return companyDao.getAll();
    }    
    
    
    
    private MutableLiveData<Long> companyInsertResult = new MutableLiveData<>();



//-----------------------------------------------------------------------
// Country
    private CountryDao2 countryDao2 = AppHandle.getHandle().getLocalDatabase().countryDao();

    private Data<Country> countries = new Data<>(countryDao2);

    public void getCompanyById( int id, MutableLiveData<List<Company>> result) {
        companies.findDataById( id, result);
    }

    public void getCompanyByName( String companyName, MutableLiveData<List<Company>> result ) {
        companies.findDataByName(companyName, result);
    }

    public void insertCountry( Country country ) {
        countries.insertData(country);
    }

    public void insertCountry(Country country, MutableLiveData<Long> result ) {
        countries.insertData(country,result);
    }

    public void updateCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.updateData(country,result);
    }

    /*
    public LiveData<List<Country>> getCountryByIdLD(int countryId) {
        return countryDao2.findByIdLD(countryId);
    }
    */

    public LiveData<List<Country>> getCountryByNameLD(String countryName) {
        return countryDao2.findByNameLD(countryName);
    }

    public List<Country> getCountryByName(String countryName) {
        return countryDao2.findByName(countryName);
    }

    public LiveData<List<Country>> getAllCountriesLD() {
        return countryDao2.getAll();
    }

    public LiveData<List<Country>> getFilteredCountriesLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return countryDao2.getViaQuery(new SimpleSQLiteQuery(filter));
        }
        return countryDao2.getAll();
    }

//-----------------------------------------------------------------------
// Department

//-----------------------------------------------------------------------
// EanCode

//-----------------------------------------------------------------------
// Family

//-----------------------------------------------------------------------
// Market

//-----------------------------------------------------------------------
// Module

//-----------------------------------------------------------------------
// OwnArticleInfo

    public void deleteCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.deleteData(country,result);
    }

    public void getCountryById( int id, MutableLiveData<List<Country>> result) {
        countries.findDataById( id, result);
    }

    public void insertOwnStore(OwnStore ownStore, MutableLiveData<Long> result ) {
        ownStores.insertData(ownStore,result);
    }

    public void updateOwnStore( OwnStore ownStore, MutableLiveData<Integer> result  ) {
        ownStores.updateData(ownStore,result);
    }

    public void deleteOwnStore( OwnStore ownStore, MutableLiveData<Integer> result  ) {
        ownStores.deleteData(ownStore,result);
    }

    public void getOwnStoreById( int id, MutableLiveData<List<OwnStore>> result) {
        ownStores.findDataById( id, result);
    }

    public void getOwnStoreByName( String ownStoreName, MutableLiveData<List<OwnStore>> result ) {
        ownStores.findDataByName(ownStoreName, result);
    }

    /* todo czy to jest potrzebne?
    public LiveData<List<OwnStore>> getOwnStoreByIdLD(int ownStoreId) {
        return ownStoreDao.findByIdLD(ownStoreId);
    }

    public LiveData<List<OwnStore>> getOwnStoreByNameLD(String ownStoreName) {
        return ownStoreDao.findByNameLD(ownStoreName);
    }
    */

    public LiveData<List<OwnStore>> getAllOwnStoresLD() {
        return ownStoreDao.getAll();
    }

    public LiveData<List<OwnStore>> getFilteredOwnStoresLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return ownStoreDao.getViaQuery(new SimpleSQLiteQuery(filter));
        }
        return ownStoreDao.getAll();
    }
    
//-----------------------------------------------------------------------
// Sector

    public void insertStore(Store store, MutableLiveData<Long> result ) {
        stores.insertData(store,result);
    }

    public void updateStore( Store store, MutableLiveData<Integer> result  ) {
        stores.updateData(store,result);
    }

    public void deleteStore( Store store, MutableLiveData<Integer> result  ) {
        stores.deleteData(store,result);
    }

    public void getStoreById( int id, MutableLiveData<List<Store>> result) {
        stores.findDataById( id, result);
    }

    public void getStoreByName( String storeName, MutableLiveData<List<Store>> result ) {
        stores.findDataByName(storeName, result);
    }

    public LiveData<List<Store>> getAllStoresLD() {
        return storeDao.getAll();
    }

    public LiveData<List<Store>> getFilteredStoresLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return storeDao.getViaQuery(new SimpleSQLiteQuery(filter));
        }
        return storeDao.getAll();
    }

    public interface AfterDatabaseClearedCallback {
        void call();
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private LocalDatabase assyncTaskAnalyzesDatabase;
        private AfterDatabaseClearedCallback afterDatabaseClearedCallback;

        ClearDatabaseAsyncTask(LocalDatabase database, AfterDatabaseClearedCallback callback) {
            assyncTaskAnalyzesDatabase = database;
            afterDatabaseClearedCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (afterDatabaseClearedCallback!=null) {
                afterDatabaseClearedCallback.call();
            }
        }
    }

//-----------------------------------------------------------------------
// UOProject

}
