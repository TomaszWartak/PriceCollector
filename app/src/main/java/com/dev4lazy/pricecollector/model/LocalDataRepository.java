package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.db.AnalysisCompetitorSlotDao;
import com.dev4lazy.pricecollector.model.db.CompanyDao;
import com.dev4lazy.pricecollector.model.db.CountryDao;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.db.OwnStoreDao;
import com.dev4lazy.pricecollector.model.db.StoreDao;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

    private AnalysisCompetitorSlotDao analysisCompetitorSlotDao = AppHandle.getHandle().getLocalDatabase().analysisCompetitorSlotDao();

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
    private Data<AnalysisCompetitorSlot> slots = new Data<>(analysisCompetitorSlotDao);
    //-----------------------------------------------------------------------
// Country
    private CountryDao countryDao = AppHandle.getHandle().getLocalDatabase().countryDao();
    private Data<Country> countries = new Data<>(countryDao);



//-----------------------------------------------------------------------
// Analysis

//-----------------------------------------------------------------------
// AnalysisCompetitorSlot
//-----------------------------------------------------------------------
// OwnStore
    private OwnStoreDao ownStoreDao = AppHandle.getHandle().getLocalDatabase().ownStoreDao();
    private Data<OwnStore> ownStores = new Data<>(ownStoreDao);
//-----------------------------------------------------------------------
// Store
    private StoreDao storeDao = AppHandle.getHandle().getLocalDatabase().storeDao();
    private Data<Store> stores = new Data<>(storeDao);

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

    public void insertAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Long> result ) {
        slots.insertData(analysisCompetitorSlot,result);
    }

    public void updateAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Integer> result  ) {
        slots.updateData(analysisCompetitorSlot, result);
    }

    public void deleteAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Integer> result   ) {
        slots.deleteData(analysisCompetitorSlot, result);
    }
//-----------------------------------------------------------------------
// Article

//-----------------------------------------------------------------------
// Company
    private CompanyDao companyDao = AppHandle.getHandle().getLocalDatabase().companyDao();

    private Data<Company> companies = new Data<>(companyDao);

    public void insertCompany( Company company, MutableLiveData<Long> result ) {
        companies.insertData(company,result);
    }

    public void updateCompany( Company company, MutableLiveData<Integer> result  ) {
        companies.updateData(company, result);
    }

    public void deleteCompany( Company company, MutableLiveData<Integer> result   ) {
        companies.deleteData(company, result);
    }

    public LiveData<List<AnalysisCompetitorSlot>> getAllSlotsLD() {
        return analysisCompetitorSlotDao.getAllLiveData();
    }

    /* todo ?
    public LiveData<List<Company>> getFilteredCompaniesLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return companyDao.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return companyDao.getAllLiveData();
    }
    */

    public void findAnalysisCompetitorSlotById(int id, MutableLiveData<List<AnalysisCompetitorSlot>> result) {
        slots.findDataById( id, result);
    }

    public void findAnalysisCompetitorSlotByName(String analysisCompetitorSlotName, MutableLiveData<List<AnalysisCompetitorSlot>> result ) {
        slots.findDataByName(analysisCompetitorSlotName, result);
    }

    public void getAllAnalysisCompetitorSlotsSortedBySlotNr(MutableLiveData<List<AnalysisCompetitorSlot>> result) {
        slots.getViaQuery(
                "SELECT * from competitor_slots ORDER BY slot_nr ASC",
                result );
        /* todo
        AppHandle.getHandle().getLocalDatabase().analysisCompetitorSlotDao().getViaQueryLiveData(
                new SimpleSQLiteQuery("SELECT * from competitor_slots ORDER BY slot_nr ASC")
        );
        */
    }

    public LiveData<List<Company>> getAllCompaniesLD() {
        return companyDao.getAllLiveData();
    }

    public void insertCountry(Country country, MutableLiveData<Long> result ) {
        countries.insertData(country,result);
    }

    public void updateCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.updateData(country,result);
    }

    public void deleteCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.deleteData(country,result);
    }

    /*
    public LiveData<List<Country>> getCountryByIdLD(int countryId) {
        return countryDao.findByIdLD(countryId);
    }


    public LiveData<List<Country>> getCountryByNameLD(String countryName) {
        return countryDao.findByNameLD(countryName);
    }
    */

    public void findCountryById(int id, MutableLiveData<List<Country>> result) {
        countries.findDataById( id, result);
    }

    public void findCountryByName(String countryName, MutableLiveData<List<Country>> result) {
        countries.findDataByName(countryName, result);
    }

    public void findCompanyById(int id, MutableLiveData<List<Company>> result) {
        companies.findDataById( id, result);
    }

    /*
    public LiveData<List<Country>> getFilteredCountriesLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return countries.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return countryDao.getAllLiveData();
    }
    */

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

    public void findCompanyByName(String companyName, MutableLiveData<List<Company>> result ) {
        companies.findDataByName(companyName, result);
    }

    public LiveData<List<Country>> getAllCountriesLD() {
        return countryDao.getAllLiveData();
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

    public void findOwnStoreById(int id, MutableLiveData<List<OwnStore>> result) {
        ownStores.findDataById( id, result);
    }

    public void findOwnStoreByName(String ownStoreName, MutableLiveData<List<OwnStore>> result ) {
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
        return ownStoreDao.getAllLiveData();
    }

    /*
    public LiveData<List<OwnStore>> getFilteredOwnStoresLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return ownStoreDao.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return ownStoreDao.getAllLiveData();
    }
    */

    /* usuń ?
    public LiveData<List<Company>> getCompanyByIdLD(int companyId) {
        return companyDao.findByIdLD(companyId);
    }

    public LiveData<List<Company>> getCompanyByNameLD(String companyName) {
        return companyDao.findByNameLD(companyName);
    }
    */
    public void findStoreById(int id, MutableLiveData<List<Store>> result) {
        stores.findDataById( id, result);
    }

    public void getAllStores( MutableLiveData<List<Store>> result ) {
        stores.getAllData( result );
    }

    public void getStoresForCompany( int companyId, MutableLiveData<List<Store>> result ) {
        stores.getViaQuery(
            "SELECT * from stores WHERE company_id="+companyId+" ORDER BY name ASC",
            result );
    }

    public void findStoreByName(String storeName, MutableLiveData<List<Store>> result ) {
        stores.findDataByName(storeName, result);
    }

    public void insertStore(Store store, MutableLiveData<Long> result ) {
        stores.insertData(store,result);
    }

    public void updateStore( Store store, MutableLiveData<Integer> result  ) {
        stores.updateData(store,result);
    }

    public void deleteStore( Store store, MutableLiveData<Integer> result  ) {
        stores.deleteData(store,result);
    }

    public LiveData<List<Store>> getAllStoresLD() {
        return storeDao.getAllLiveData();
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

    /*
    public LiveData<List<Store>> getFilteredStoresLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return storeDao.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return storeDao.getAllLiveData();
    }
    */

//-----------------------------------------------------------------------
// Sector

//-----------------------------------------------------------------------
// UOProject

}
