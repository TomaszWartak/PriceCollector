package com.dev4lazy.pricecollector.model.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.ArrayList;
import java.util.List;

import static com.dev4lazy.pricecollector.utils.AppPreferences.BRICOMAN_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COMPANIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COUNTRIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.DATA_NOT_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LM_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_COMPETITORS_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OBI_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OWN_STORES_INITIALIZED;

// Klasa używana do inicjalizacji danych aplikacji, po pierwszym logowaniu
public class DataInitializer {

    private static DataInitializer instance = new DataInitializer();
    // Kraje
    // (0) - "Kraj własny"
    private List<Country> countries = null;
    // Sieci
    // (0) - Castorama
    private List<Company> companies = null;
    // Sklepy sieci własnej
    private List<OwnStore> ownStores = null;
    // Sklepy sieci konkurencyjnych
    private List<Store> obiStores = null;
    private List<Store> lmStores = null;
    private List<Store> bricomanStores = null;
    private List<Store> localCompetitorStores = null;

    private boolean firstCallCountries = true;
    private boolean firstCallCompanies = true;
    private boolean firstCallOwnStores = true;
    private boolean firstCallObiStores = true;
    private boolean firstCallLMStores = true;
    private boolean firstCallBricomanStores = true;
    private boolean firstCallLocalCompetitorStores = true;

// ---------------------------------------------------------------------------
// Przygotowanie danych


    private DataInitializer() {    }

    public static DataInitializer getInstance() {
        if (instance == null) {
            synchronized (DataInitializer.class) {
                if (instance == null) {
                    instance = new DataInitializer();
                }
            }
        }
        return instance;
    }

    public void initializeLocalDatabase() {
        // todo !!!! to jest wołane także przy powrocie z przeglądania artykułow
        // todo usuń czyszczenie bazy
        // AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(new StartCallback());
        // todo normalnie ma być jn.
        checkAndSetIfNotInitialized();
        // todo !!!!!!!! zrób etapy inicjalizacji... do zpaisania w preferencjach
        // todo na końcu "initailisationChain wpiez do preferencji że baza zainicjowana
    }

    public void ClearLocalDatabase() {
        AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(null);
        // todo to niżej przeniósłbym do AppSettings - czyli warstwę wyżej
        //  inicjalizacja bazy lokalnej -> setLocalDatabaseNotInitialized()
        AppHandle.getHandle().getPrefs().setLocalDatabaseInitialized(false);
        AppHandle.getHandle().getPrefs().setInitialisationStage(DATA_NOT_INITIALIZED);
    }

    private void initialize() {
        prepareData();
        startInitialisationChain();
    }

    private void prepareCountries() {
        countries = new ArrayList<>();
        Country country = new Country();
        country.setName("Polska");
        country.setEnglishName("Poland");
        countries.add(country);
    }

// ---------------------------------------------------------------------------
// Przygotowanie danych

    private void prepareData() {
        prepareCountries();
        prepareCompanies();
        prepareOwnStores();
        prepareObiStores();
        prepareLeroyMerlinStores();
        prepareBricomanStores();
        prepareLocalCompetitorStores();
    }

    private void prepareCompanies() {
        companies = new ArrayList<>();
        // Nie zmieniaj kolejności, bo niżej ma to znaczenie :-)
        Company company = new Company();
        company.setName("Castorama");
        companies.add(company);
        company = new Company();
        company.setName("OBI");
        companies.add(company);
        company = new Company();
        company.setName("Leroy Merlin");
        companies.add(company);
        company = new Company();
        company.setName("BRICOMAN");
        companies.add(company);
        company = new Company();
        company.setName("Konkurent lokalny");
        companies.add(company);
           /*
        company.setName("BRICO MARCHE");
        companies.add(company);
        company.setName("Mrówka");
        companies.add(company);
        company.setName("Majster");
        companies.add(company);
        */ }

   private void prepareOwnStores() {
        ownStores = new ArrayList<>();
        OwnStore ownStore = new OwnStore();
        ownStore.setName("Castorama Rybnik");
        ownStore.setCity("Rybnik");
        ownStore.setStreet("Obwiednia Północna 21");
        ownStore.setZipCode("44-200");
        ownStore.setOwnNumber("8033");
        ownStore.setSapOwnNumber("1563");
        ownStore.setStructureType(StoreStructureType.BY_SECTORS);
        ownStores.add(ownStore);
        ownStore = new OwnStore();
        ownStore.setName("Castorama Katowice");
        ownStore.setCity("Katowice");
        ownStore.setStreet("aleja Roździeńskiego 198");
        ownStore.setZipCode("40-315");
        ownStore.setOwnNumber("8007");
        ownStore.setSapOwnNumber("????");
        ownStore.setStructureType(StoreStructureType.BY_DEPARTMENTS);
        ownStores.add(ownStore);
        ownStore = new OwnStore();
        ownStore.setName("Castorama Sosnowiec");
        ownStore.setCity("Sosnowiec");
        ownStore.setStreet("Jana Długosza 82");
        ownStore.setZipCode("41-219");
        ownStore.setOwnNumber("8015");
        ownStore.setSapOwnNumber("????");
        ownStore.setStructureType(StoreStructureType.BY_DEPARTMENTS);
        ownStores.add(ownStore);
    }

// ---------------------------------------------------------------------------
// Metoda sprawdzająca, czy baza danych jest zainicjowana. Jeśli nie jest - inicjalizuje bazę.
    private void checkAndSetIfNotInitialized() {
        int initalisationStage = AppHandle.getHandle().getPrefs().getInitialisationStage();
        switch (initalisationStage) {
            case DATA_NOT_INITIALIZED:
                initialize();
                break;
            case COUNTRIES_INITIALIZED:
                prepareData();
                initializeCompanies();
                break;
            case COMPANIES_INITIALIZED:
                prepareData();
                initializeOwnStores();
                break;
            case OWN_STORES_INITIALIZED:
                prepareData();
                initializeObiStores();
                break;
            case OBI_STORES_INITIALIZED:
                prepareData();
                initializeLMStores();
                break;
            case LM_STORES_INITIALIZED:
                prepareData();
                initializeBricomanStores();
                break;
            case BRICOMAN_STORES_INITIALIZED:
                prepareData();
                initializeLocalCompetitorStores();
                break;
            case LOCAL_COMPETITORS_STORES_INITIALIZED:
                // inicjalizacja była kompletna
                break;
        }
    }

    private void prepareObiStores() {
        obiStores = new ArrayList<>();
        Store otherStore = new Store();
        otherStore.setName("OBI Rybnik");
        otherStore.setCity("Rybnik");
        otherStore.setStreet("Żorska 55");
        otherStore.setZipCode("44-203");
        obiStores.add(otherStore);
        // obi Dąbrowa Górnicza
        // obi Czeladź
        // obi Katowice
    }

    private void prepareLeroyMerlinStores() {
        lmStores = new ArrayList<>();
        Store otherStore = new Store();
        // LM Sosnowiec
        // LM katowice 1
    }

    private void prepareBricomanStores() {
        bricomanStores = new ArrayList<>();
        Store otherStore = new Store();
        // Bricoman Jaworzno
    }

    private void prepareLocalCompetitorStores() {
        localCompetitorStores = new ArrayList<>();
        Store otherStore = new Store();
        otherStore.setName("PSB Mrówka Jastrzębie=Zdrój");
        otherStore.setCity("Jastrzębie-Zdrój");
        otherStore.setStreet("Rybnicka 11");
        otherStore.setZipCode("44-335");
        localCompetitorStores.add(otherStore);
        otherStore = new Store();
        otherStore.setName("Majster Radlin");
        otherStore.setCity("Radlin");
        otherStore.setStreet("Rybnicka 125");
        otherStore.setZipCode("44-310");
        localCompetitorStores.add(otherStore);
    }

// ---------------------------------------------------------------------------
// gettery danych (lokalne :-P) todo? czy prywatne gettery mają sens?

    private List<Country> getCountries() {
        return countries;
    }

    private List<Company> getCompanies() {
        return companies;
    }

    private List<OwnStore> getOwnStores() {
        return ownStores;
    }

    private List<Store> getObiStores() {
        return obiStores;
    }

    private List<Store> getLMStores() {
        return lmStores;
    }

    private List<Store> getBricomanStores() {
        return bricomanStores;
    }
    
    private List<Store> getLocalCompetitorStores() {
        return localCompetitorStores;
    }

    private void initalizeCountries() {
        // todo odczyt kraju z preferencji
        // todo tu zamotka jest... Z preferencji, czy z DataInitializera?
        /*
        Country ownCountry = new Country();
        ownCountry.setName(AppHandle.getHandle().getPrefs().getCountryName());
        ownCountry.setEnglishName(AppHandle.getHandle().getPrefs().getEnglishCountryName());
         */
        Country ownCountry = getCountries().get(0);

        // Ustanowienie obserwatora dla rezulatu dopisania kraju własnego
        MutableLiveData<Long> ownCountryInsertResult = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long ownCountryId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (firstCallCountries) {
                    firstCallCountries = false;
                    ownCountryInsertResult.removeObserver(this); // this = observer...
                    if (ownCountryId!=-1) {
                        //insertOwnCompany(ownCountryId);
                        AppHandle.getHandle().getPrefs().setInitialisationStage(COUNTRIES_INITIALIZED);
                        initializeCompanies();
                    }
                }
            }
        };
        ownCountryInsertResult.observeForever(insertingResultObserver);

        // Dodanie kraju własnego
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertCountry(ownCountry,ownCountryInsertResult);
    }

// ---------------------------------------------------------------------------
// Zapis danych

    private void startInitialisationChain() {
        initalizeCountries();
    }

    private void initializeCompanies() {
        MutableLiveData<List<Country>> result = new MutableLiveData<>();
        Observer<List<Country>> resultObserver = new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countriesList) {
                if (firstCallCompanies) {
                    firstCallCompanies = false;
                    result.removeObserver(this); // this = observer...
                    if ((countriesList!=null)&&(!countriesList.isEmpty())){
                        Country country = countriesList.get(0);
                        for (Company company : getCompanies() ) {
                            company.setCountryId(country.getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertCompany( company, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(COMPANIES_INITIALIZED);
                        initializeOwnStores();}
                }
            }
        };
        result.observeForever(resultObserver);
        String ownCountryName = AppHandle.getHandle().getPrefs().getCountryName();
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCountryByName(ownCountryName,result);
    }

    private void initializeOwnStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (firstCallOwnStores) {
                    firstCallOwnStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (OwnStore store : getOwnStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertOwnStore( store, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(OWN_STORES_INITIALIZED);
                        initializeObiStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company ownCompany = getCompanies().get(0);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(ownCompany.getName(),result);
    }

    private void initializeObiStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallObiStores) {
                    firstCallObiStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getObiStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(OBI_STORES_INITIALIZED);
                        initializeLMStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company obiCompany = getCompanies().get(1);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(obiCompany.getName(),result);
    }

    private void initializeLMStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallLMStores) {
                    firstCallLMStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getLMStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(LM_STORES_INITIALIZED);
                        initializeBricomanStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company lmCompany = getCompanies().get(2);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(lmCompany.getName(),result);
    }

    private void initializeBricomanStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallBricomanStores) {
                    firstCallBricomanStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getBricomanStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(BRICOMAN_STORES_INITIALIZED);
                        initializeLocalCompetitorStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company bricomanCompany = getCompanies().get(3);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(bricomanCompany.getName(),result);
    }

    private void initializeLocalCompetitorStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallLocalCompetitorStores) {
                    firstCallLocalCompetitorStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        for (Store store : getLocalCompetitorStores()) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore(store, null);
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(LOCAL_COMPETITORS_STORES_INITIALIZED);
                        AppHandle.getHandle().getPrefs().setLocalDatabaseInitialized(true);
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(4);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    private class StartCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
        @Override
        public void call() {
            initialize();
        }
    }
}
