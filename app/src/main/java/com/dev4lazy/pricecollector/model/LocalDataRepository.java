package com.dev4lazy.pricecollector.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db.CompanyDao;
import com.dev4lazy.pricecollector.model.db.CountryDao2;
import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;

public class LocalDataRepository {

    private static LocalDataRepository instance = new LocalDataRepository();

    private LocalDataRepository() {
        // todo dodaj sprawdzenie, czy baza jest zainicjowana
        // todo jesli nie incializajca bazy
        // todo bo przy powrocie z przegladania remote database powtórnie jest baza inicjowana
        initializeLocalDatabase();
    }

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

//-----------------------------------------------------------------------------------
// clearDatabase
    public void clearDatabase() {
        new ClearDatabaseAsyncTask(AppHandle.getHandle().getLocalDatabase()).execute();
    }

    private static class ClearDatabaseAsyncTask extends AsyncTask<Void,Void,Void> {
        private LocalDatabase assyncTaskAnalyzesDatabase;

        ClearDatabaseAsyncTask(LocalDatabase database) {
            assyncTaskAnalyzesDatabase = database;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assyncTaskAnalyzesDatabase.clearAllTables();
            return null;
        }
    }

//-----------------------------------------------------------------------------------
// inicjalizacja bazy lokalnej
    public void initializeLocalDatabase() {
        // todo usuń czyszczenie bazy
        clearDatabase();
        // todo inicjalizacj bazy zrob w oddzielnym obiekcie
        startInitialisationChain();
    }

    private void startInitialisationChain() {
        initalizeCountries();
    }

    // todo usuń private boolean firstCall = true;

    boolean firstCall = true; //todo zmień nazwę
    private void initalizeCountries() {
        // todo odczyt kraju z preferencji
        Country ownCountry = new Country();
        ownCountry.setName(AppHandle.getHandle().getPreferences().getCountryName());
        ownCountry.setEnglishName(AppHandle.getHandle().getPreferences().getCountryEnglishName());

        // Ustanowienie obserwatora dla rezulatu dopisania kraju własnego
        // todo test

        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long ownCountryId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (firstCall) {
                    firstCall = false;
                    ownCountryInsertResult.removeObserver(this); // this = observer...
                    if (ownCountryId!=-1) {
                        insertOwnComapny(ownCountryId);
                    }
                } /*else {
                    notFirstCall = true;
                } */
                // todo removeObserver()
            }
        };
        ownCountryInsertResult.observeForever(insertingResultObserver);

        // Dodanie kraju własnego
        // todo robisz to przez Thread, może wróć do AsyncTask?
        insertCountryAsync(ownCountry);
    }


    /*
    Nie kasuj... Tu masz przykład jak odczytać dane z bazy

    private MutableLiveData<List<Country>> getCountryByNameResult = new MutableLiveData<>();

    private addOwnComapny() {

         String countryName = AppHandle.getHandle().getPreferences().getCountryName();

        // Ustanowienie obserwatora dla pobrania dancyh kraju własnego z bazy. Jest potrzebne jego id do wpisania do danych sieci
        Observer<List<Country>> observer = new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countriesList) {
                // todo? czy to sprawdzenie jest potrzebne?
                if (AppHandle.getHandle().getLocalDatabase().isDatabaseCreated().getValue() != null) {
                    Country country = countriesList.get(0);
                    Company company = new Company();
                    company.setCountryId(country.getId());
                    // todo odczyt danych z "inicjalizatora" (tablice z danymi do inicjalizacji danych) - > sieć własna
                    company.setName("Castorama");
                    // todo dodanie Castorama
                    // todo dodanie pozostałych sieci
                    // todo wywołanie initializeStores()
                }
                // to chyba automatycznie jest przy MediatorLiveData mObservableCountries.removeObserver(observer);
                getCountryByNameResult.removeObserver(this); // this = observer...
            }
        };
        getCountryByNameResult.observeForever(observer);

        // Wywołanie zapytania o dane kraju własnego
        // todo robisz to przez Thread, może wróć do AsyncTask?
        getCountryByNameAsync(countryName);
   }
   */

    boolean firstCall2 = true; //todo zmień nazwę

    private void insertOwnComapny(Long ownCountryId) {
        Company ownCompany = new Company();
        ownCompany.setCountryId(ownCountryId.intValue());
        // todo odczyt danych z "inicjalizatora" (tablice z danymi do inicjalizacji danych) - > sieć własna
        ownCompany.setName("Castorama");
        // todo dodanie sieci własnej
        // todo na potrzeby testu - z observerem; bezt testu po prostu dodanie
        MutableLiveData<Long> companyInsertResult1 = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long ownCompanyId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (firstCall2) {
                    firstCall2 = false;
                    if (ownCompanyId!=null) {
                        if (ownCompanyId!=-1) {
                        }
                    }
                }
                companyInsertResult1.removeObserver(this); // this = observer...
            }
        };
        companyInsertResult1.observeForever(insertingResultObserver);

        // Dodanie kraju własnego
        // todo robisz to przez Thread, może wróć do AsyncTask?
        insertAsyncCompany( ownCompany, companyInsertResult1 );

        // todo dodanie pozostałych sieci
        insertOtherComapnies(ownCountryId);

        // todo wywołanie initializeStores()
    }

    private void insertOtherComapnies(Long ownCountryId) {
        // Pobranie "kraju własnego", w celu ustawienia wartości "w sieci własnej"
        // todo odczyt danych z "inicjalizatora" (tablice z danymi do inicjalizacji danych)
        // todo być może część danych z inicjalizatora powinna trafić do preferencji przy pierwszym uruchomieniu
        // todo cd. a tutuaj już pobranie z preferencji
        String countryName = AppHandle.getHandle().getPreferences().getCountryName();
    }


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

    public void insertCompany( Company company ) {
        companies.insertData(company);
    }

    public void insertAsyncCompany( Company company, MutableLiveData<Long> result ) {
        companies.insertData(company,result);
    }
    public void updateCompany( Company company ) {
        companies.updateData(company);
    }

    public void deleteCompany( Company company ) {
        companies.deleteData(company);
    }

    public LiveData<List<Company>> getCompanyByIdLD(int companyId) {
        return companyDao.findByIdLD(companyId);
    }

    public LiveData<List<Company>> getCompanyByNameLD(String companyName) {
        return companyDao.findByNameLD(companyName);
    }

    public List<Company> getCompanyByName(String companyName) {
        return companyDao.findByName(companyName);
    }

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

    public void updateCountry( Country country ) {
        countries.updateData(country);
    }

    public void deleteCountry( Country country ) {
        countries.deleteData(country);
    }

    public LiveData<List<Country>> getCountryByIdLD(int countryId) {
        return countryDao2.findByIdLD(countryId);
    }

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

    //**********************************************************************

    private MutableLiveData<Long> ownCountryInsertResult = new MutableLiveData<>();

    private MutableLiveData<List<Country>> getCountryByNameResult = new MutableLiveData<>();

    private void insertCountry(Country country) {
        insertCountryAsync(country);
    }

    private  void insertCountryAsync(Country country) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ownCountryInsertResult.postValue(countryDao2.insert(country));
                    // todo usuń? ownCountryInsertResult.postValue(1);
                } catch (Exception e) {
                    ownCountryInsertResult.postValue(-1L);
                }
            }
        }).start();
    }

    private void getCountryByNameAsync(String countryName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getCountryByNameResult.postValue(getCountryByName(countryName));
                } catch (Exception e) {
                    getCountryByNameResult.postValue(null);
                }
            }
        }).start();
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

//-----------------------------------------------------------------------
// OwnStore

//-----------------------------------------------------------------------
// Sector

//-----------------------------------------------------------------------
// Store


//-----------------------------------------------------------------------
// UOProject

}
