package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.db.AnalysisCompetitorSlotDao;
import com.dev4lazy.pricecollector.model.db.AnalysisDao;
import com.dev4lazy.pricecollector.model.db.ArticleDao;
import com.dev4lazy.pricecollector.model.db.CompanyDao;
import com.dev4lazy.pricecollector.model.db.CompetitorPriceDao;
import com.dev4lazy.pricecollector.model.db.CountryDao;
import com.dev4lazy.pricecollector.model.db.DepartmentDao;
import com.dev4lazy.pricecollector.model.db.DepartmentInSectorDao;
import com.dev4lazy.pricecollector.model.db.EanCodeDao;
import com.dev4lazy.pricecollector.model.db.FamilyDao;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.db.MarketDao;
import com.dev4lazy.pricecollector.model.db.ModuleDao;
import com.dev4lazy.pricecollector.model.db.OwnArticleInfoDao;
import com.dev4lazy.pricecollector.model.db.OwnStoreDao;
import com.dev4lazy.pricecollector.model.db.SectorDao;
import com.dev4lazy.pricecollector.model.db.StoreDao;
import com.dev4lazy.pricecollector.model.db.UOProjectDao;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.view.ProgressPresenter;

import java.util.ArrayList;
import java.util.List;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

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

    /*private LocalDataRepository() {

    }
     */

// po getInstance -----------------------------------------------------------------------
    
// Analysis
    private AnalysisDao analysisDao = AppHandle.getHandle().getLocalDatabase().analysisDao();
    private Data<Analysis> analyzes  = new Data<>( analysisDao );

    public void askAnalyzesNumberOf( MutableLiveData<Integer> result ) {
        analyzes.getNumberOfData( result );
    }

    public void insertAnalysis( Analysis analysis, MutableLiveData<Long> result ) {
        analyzes.insertData( analysis, result );
    }

    public void deleteAllAnalyzes ( MutableLiveData<Integer> result ) {
        analyzes.deleteAllData( result );
    }

    public void getAllAnalyzes ( MutableLiveData<List<Analysis>> result ) {
        analyzes.getAllData( result );
    }

    public void findAnalysisById( int id, MutableLiveData<List<Analysis>> result ) {
        analyzes.findDataById( id, result );
    }

    public void insertAnalyzes ( ArrayList<Analysis> analyzesList, ProgressPresenter progressPresenter ) {
        analyzes.insertDataList( analyzesList, progressPresenter );
    }

    public void updateAnalysis( Analysis analysis, MutableLiveData<Integer> result ) {
        analyzes .updateData( analysis, result );
    }

    public void deleteAnalysis(Analysis analysis, MutableLiveData<Integer> result ) {
        analyzes.deleteData( analysis, result );
    }

//-----------------------------------------------------------------------
// AnalysisCompetitorSlot
    private AnalysisCompetitorSlotDao analysisCompetitorSlotDao = AppHandle.getHandle().getLocalDatabase().analysisCompetitorSlotDao();
    private Data<AnalysisCompetitorSlot> slots = new Data<>(analysisCompetitorSlotDao);

//-----------------------------------------------------------------------
// Article
    private ArticleDao articleDao = AppHandle.getHandle().getLocalDatabase().articleDao();
    private Data<Article> articles = new Data<>(articleDao);

//-----------------------------------------------------------------------
// CompetitorPrice
    private CompetitorPriceDao competitorPriceDao = AppHandle.getHandle().getLocalDatabase().competitorPriceDao();
    private Data<CompetitorPrice> competitorPrices = new Data<>( competitorPriceDao );

    public void askCompetitorPricesNumberOf( MutableLiveData<Integer> result ) {
        competitorPrices.getNumberOfData( result );
    }

    public void insertCompetitorPrice( CompetitorPrice competitorPrice, MutableLiveData<Long> result ) {
        competitorPrices.insertData( competitorPrice, result );
    }

    public void insertCompetitorPrices(ArrayList<CompetitorPrice> competitorPricesList, ProgressPresenter progressPresenter ) {
        competitorPrices.insertDataList( competitorPricesList, progressPresenter );
    }

    public void updateCompetitorPrice( CompetitorPrice competitorPrice, MutableLiveData<Integer> result ) {
        competitorPrices.updateData( competitorPrice, result );
    }

    public void deleteCompetitorPrice( CompetitorPrice competitorPrice, MutableLiveData<Integer> result ) {
        competitorPrices.deleteData( competitorPrice, result );
    }

    public void deleteAllCompetitorPrices( MutableLiveData<Integer> result ) {
        competitorPrices.deleteAllData( result );
    }

//-----------------------------------------------------------------------
// Family (2019-11-21 08:58 OK)
    private FamilyDao familyDao = AppHandle.getHandle().getLocalDatabase().familyDao();
    private Data<Family> families = new Data<>( familyDao );

//-----------------------------------------------------------------------
// Country
    private CountryDao countryDao = AppHandle.getHandle().getLocalDatabase().countryDao();


//-----------------------------------------------------------------------
// Company
    private CompanyDao companyDao = AppHandle.getHandle().getLocalDatabase().companyDao();
    private Data<Company> companies = new Data<>(companyDao);


//-----------------------------------------------------------------------
// Department
    private DepartmentDao departmentDao = AppHandle.getHandle().getLocalDatabase().departmentDao();
    private Data<Department> departments = new Data<>(departmentDao);

//-----------------------------------------------------------------------
// DepartmentInSector
    private DepartmentInSectorDao departmentInSectorDao = AppHandle.getHandle().getLocalDatabase().departmentInSectorDao();
    private Data<DepartmentInSector> departmentInSectors = new Data<>(departmentInSectorDao);
//-----------------------------------------------------------------------
// Market
    private MarketDao marketDao = AppHandle.getHandle().getLocalDatabase().marketDao();
    private Data<Market> markets = new Data<>( marketDao );

    public void insertFamily(Family family, MutableLiveData<Long> result ) {
        families.insertData( family, result );
    }

    public void inserFamilies( ArrayList<Family> familiesList, ProgressPresenter progressPresenter ) {
        families.insertDataList( familiesList, progressPresenter );
    }

    public void updateFamily( Family family, MutableLiveData<Integer> result ) {
        families.updateData( family, result );
    }

    public void deleteFamily( Family family, MutableLiveData<Integer> result ) {
        families.deleteData( family, result );
    }

    public void deleteAllFamilies( MutableLiveData<Integer> result ) {
        families.deleteAllData( result );
    }

    public void getAllFamilies( MutableLiveData<List<Family>> result ) {
        families.getAllData( result );
    }

    public void findFamilyById( int id, MutableLiveData<List<Family>> result ) {
        families.findDataById( id, result );
    }
    private ModuleDao moduleDao = AppHandle.getHandle().getLocalDatabase().moduleDao();
    private Data<Module> modules = new Data<>( moduleDao );
    private UOProjectDao uoProjectDao = AppHandle.getHandle().getLocalDatabase().uoProjectDao();
    private Data<UOProject> uoProjects = new Data<>( uoProjectDao );

    public void getAllCompetitorPrices( MutableLiveData<List<CompetitorPrice>> result ) {
        competitorPrices.getAllData( result );
    }

    public void findCompetitorPriceById( int id, MutableLiveData<List<CompetitorPrice>> result ) {
        competitorPrices.findDataById( id, result );
    }

    public void askMarketsNumberOf( MutableLiveData<Integer> result ) {
        markets.getNumberOfData( result );
    }

    public void insertMarket( Market market, MutableLiveData<Long> result ) {
        markets.insertData( market, result );
    }

    public void insertMarkets( ArrayList<Market> marketsList, ProgressPresenter progressPresenter ) {
        markets.insertDataList( marketsList, progressPresenter );
    }

    public void updateMarket( Market market, MutableLiveData<Integer> result ) {
        markets.updateData( market, result );
    }

//-----------------------------------------------------------------------------------
// Module

    public void deleteMarket( Market market, MutableLiveData<Integer> result ) {
        markets.deleteData( market, result );
    }

    public void deleteAllMarkets( MutableLiveData<Integer> result ) {
        markets.deleteAllData( result );
    }

    public void getAllMarkets( MutableLiveData<List<Market>> result ) {
        markets.getAllData( result );
    }

    public void findMarketById( int id, MutableLiveData<List<Market>> result ) {
        markets.findDataById( id, result );
    }

    public void askModulesNumberOf( MutableLiveData<Integer> result ) {
        modules.getNumberOfData( result );
    }

    public void insertModule( Module module, MutableLiveData<Long> result ) {
        modules.insertData( module, result );
    }

    public void insertModules( ArrayList<Module> modulesList, ProgressPresenter progressPresenter ) {
        modules.insertDataList( modulesList, progressPresenter );
    }

    public void updateModule( Module module, MutableLiveData<Integer> result ) {
        modules.updateData( module, result );
    }

    public void deleteModule( Module module, MutableLiveData<Integer> result ) {
        modules.deleteData( module, result );
    }

    public void deleteAllModules( MutableLiveData<Integer> result ) {
        modules.deleteAllData( result );
    }

//-----------------------------------------------------------------------
// UOProject
    
    public void getAllModules( MutableLiveData<List<Module>> result ) {
        modules.getAllData( result );
    }

    public void findModuleById( int id, MutableLiveData<List<Module>> result ) {
        modules.findDataById( id, result );
    }

    public void askUOProjectsNumberOf( MutableLiveData<Integer> result ) {
        uoProjects.getNumberOfData( result );
    }

    public void insertUOProject(UOProject uoProject, MutableLiveData<Long> result ) {
        uoProjects.insertData( uoProject, result );
    }

    public void insertUOProjects( ArrayList<UOProject> uoProjectsList, ProgressPresenter progressPresenter ) {
        uoProjects.insertDataList( uoProjectsList, progressPresenter );
    }

    public void updateUOProject( UOProject uoProject, MutableLiveData<Integer> result ) {
        uoProjects.updateData( uoProject, result );
    }

    public void deleteUOProject(UOProject uoProject, MutableLiveData<Integer> result ) {
        uoProjects.deleteData( uoProject, result );
    }

    public void deleteAllUOProjects( MutableLiveData<Integer> result ) {
        uoProjects.deleteAllData( result );
    }

    public void getAllUOProjects( MutableLiveData<List<UOProject>> result ) {
        uoProjects.getAllData( result );
    }

    public void findUOProjectById( int id, MutableLiveData<List<UOProject>> result ) {
        uoProjects.findDataById( id, result );
    }

//-----------------------------------------------------------------------
// OwnArticleInfo
    private OwnArticleInfoDao ownArticleInfoDao = AppHandle.getHandle().getLocalDatabase().ownArticleInfoDao();
    private Data<OwnArticleInfo> ownArticleInfos = new Data<OwnArticleInfo>(ownArticleInfoDao);
//-----------------------------------------------------------------------
// Sector
    private SectorDao sectorDao = AppHandle.getHandle().getLocalDatabase().sectorDao();
    private Data<Sector> sectors = new Data<>(sectorDao);

    public void insertAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Long> result ) {
        slots.insertData(analysisCompetitorSlot,result);
    }

    public void updateAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Integer> result  ) {
        slots.updateData(analysisCompetitorSlot, result);
    }

    public void deleteAnalysisCompetitorSlot( AnalysisCompetitorSlot analysisCompetitorSlot, MutableLiveData<Integer> result   ) {
        slots.deleteData(analysisCompetitorSlot, result);
    }

    public void insertArticles( ArrayList<Article> articlesList, ProgressPresenter progressPresenter) {
        articles.insertDataList( articlesList, progressPresenter );
    }

    public void getAllArticles( MutableLiveData<List<Article>> result ) {
        articles.getAllData( result );
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

    public void findCountryById(int id, MutableLiveData<List<Country>> result) {
        countries.findDataById( id, result);
    }

    public void findCountryByName(String countryName, MutableLiveData<List<Country>> result) {
        countries.findDataByName(countryName, result);
    }

//-----------------------------------------------------------------------
// EanCode
    private EanCodeDao eanCodeDao = AppHandle.getHandle().getLocalDatabase().eanCodeDao();
    private Data<EanCode> eanCodes = new Data<EanCode>(eanCodeDao);

    public void insertDepartment( Department department, MutableLiveData<Long> result ) {
        departments.insertData( department, result );
    }

    private Data<Country> countries = new Data<>(countryDao);

    public void insertCountry(Country country, MutableLiveData<Long> result ) {
        countries.insertData(country,result);
    }

    /*
    public LiveData<List<Country>> getCountryByIdLD(int countryId) {
        return countryDao.findByIdLD(countryId);
    }


    public LiveData<List<Country>> getCountryByNameLD(String countryName) {
        return countryDao.findByNameLD(countryName);
    }
    */

    public void updateCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.updateData(country,result);
    }

    public void deleteCountry( Country country, MutableLiveData<Integer> result  ) {
        countries.deleteData(country,result);
    }

//-----------------------------------------------------------------------
// OwnStore
    private OwnStoreDao ownStoreDao = AppHandle.getHandle().getLocalDatabase().ownStoreDao();
    private Data<OwnStore> ownStores = new Data<>(ownStoreDao);
//-----------------------------------------------------------------------
// Store
    private StoreDao storeDao = AppHandle.getHandle().getLocalDatabase().storeDao();
    private Data<Store> stores = new Data<>(storeDao);


    public void findAnalysisCompetitorSlotById(int id, MutableLiveData<List<AnalysisCompetitorSlot>> result) {
        slots.findDataById( id, result);
    }

    public void findAnalysisCompetitorSlotByName(String analysisCompetitorSlotName, MutableLiveData<List<AnalysisCompetitorSlot>> result ) {
        slots.findDataByName(analysisCompetitorSlotName, result);
    }


    public LiveData<List<Company>> getAllCompaniesLD() {
        return companyDao.getAllLiveData();
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
// Market

//-----------------------------------------------------------------------
// Module

    public void getAllDepartments( MutableLiveData<List<Department>> result ) {
        departments.getAllData( result );
    }

    public void insertDepartmentInSector( DepartmentInSector departmentInSector, MutableLiveData<Long> result ) {
        departmentInSectors.insertData( departmentInSector, result );
    }

    public void getAllDepartmentInSectors( MutableLiveData<List<DepartmentInSector>> result ) {
        departmentInSectors.getAllData( result );
    }
    
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


    /*
    public LiveData<List<Store>> getFilteredStoresLD(String filter) {
        if ((filter!=null) && (!filter.isEmpty())) {
            return storeDao.getViaQueryLiveData(new SimpleSQLiteQuery(filter));
        }
        return storeDao.getAllLiveData();
    }
    */

    public void insertEanCodes( ArrayList<EanCode> eanCodeList, ProgressPresenter progressPresenter) {
        eanCodes.insertDataList( eanCodeList, progressPresenter );
    }

    public void insertOwnArticleInfos( ArrayList<OwnArticleInfo> ownArticleInfoList, ProgressPresenter progressPresenter) {
        ownArticleInfos.insertDataList( ownArticleInfoList, progressPresenter );
    }

    public void insertSector(Sector sector, MutableLiveData<Long> result ) {
        sectors.insertData(sector,result);
    }

    public void getAllSectors( MutableLiveData<List<Sector>> result ) {
        sectors.getAllData( result );
    }
//-----------------------------------------------------------------------
// UOProject

//-----------------------------------------------------------------------------------
// clearDatabaseAsync
    public void clearDatabase(AfterDatabaseClearedCallback callback) {
        new ClearDatabaseAsyncTask(
                AppHandle.getHandle().getLocalDatabase(),
                callback
        ).execute();
    }

    public interface AfterDatabaseClearedCallback {
        void call();
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private LocalDatabase asyncTaskAnalyzesDatabase;
        private AfterDatabaseClearedCallback afterDatabaseClearedCallback;

        ClearDatabaseAsyncTask(LocalDatabase database, AfterDatabaseClearedCallback callback) {
            asyncTaskAnalyzesDatabase = database;
            afterDatabaseClearedCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskAnalyzesDatabase.clearAllTables();
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

}
