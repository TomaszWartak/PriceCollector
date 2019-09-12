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
    private boolean firstCallCountries = true; //todo zmień nazwę
    private boolean isFirstCallOwnStores = true;
    private boolean isFirstCallObiStores = true;
    private boolean isFirstCallLMStores = true;

// ---------------------------------------------------------------------------
// Przygotowanie danych
    private boolean isFirstCallBricomanStores = true;
    private boolean isFirstCallLocalCompetitorStores = true;

    private DataInitializer() {

    }

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
        AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(new StartCallback());
    }

    private void prepareData() {
        prepareCountries();
        prepareCompanies();
        prepareOwnStores();
        prepareObiStores();
        prepareLeroyMerlinStores();
        prepareBricomanStores();
        prepareLocalCompetitorStores();
    }

    private void prepareCountries() {
        countries = new ArrayList<>();
        Country country = new Country();
        country.setName("Poland");
        country.setEnglishName("Poland");
        countries.add(country);
    }

    private void prepareCompanies() {
        companies = new ArrayList<>();
        // Nie zmieniaj kolsjności, bo niżej ma to znaczenie :-)
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

// ---------------------------------------------------------------------------
// gettery danych (lokalne :-P) todo? czy prywatne gettery mają sens?

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

    private List<Country> getCountries() {
        return countries;
    }

    private List<Company> getCompanies() {
        return companies;
    }

// ---------------------------------------------------------------------------
// Zapis danych

    private List<OwnStore> getOwnStores() {
        return ownStores;
    }

    // todo usuń private boolean firstCallCountries = true;

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

    private void startInitialisationChain() {
        initalizeCountries();
    }

    private void initalizeCountries() {
        // todo odczyt kraju z preferencji
        // todo tu zamotka jest... Z preferencji, czy z DataInitializera?
        /*
        Country ownCountry = new Country();
        ownCountry.setName(AppHandle.getHandle().getPreferences().getCountryName());
        ownCountry.setEnglishName(AppHandle.getHandle().getPreferences().getCountryEnglishName());
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
                        initializeCompanies(ownCountryId);
                    }
                }
            }
        };
        ownCountryInsertResult.observeForever(insertingResultObserver);

        // Dodanie kraju własnego
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertCountry(ownCountry,ownCountryInsertResult);
    }

    private void initializeCompanies(Long ownCountryId) {
        for (Company company : getCompanies() ) {
            company.setCountryId(ownCountryId.intValue());
            AppHandle.getHandle().getRepository().getLocalDataRepository().insertCompany( company, null );
        }
        initializeOwnStores();
    }

    private void initializeOwnStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (isFirstCallOwnStores) {
                    isFirstCallOwnStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (OwnStore store : getOwnStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertOwnStore( store, null );
                        }
                        initializeObiStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company ownCompany = getCompanies().get(0);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getCompanyByName(ownCompany.getName(),result);
    }

    private void initializeObiStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (isFirstCallObiStores) {
                    isFirstCallObiStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getObiStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        initializeLMStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company obiCompany = getCompanies().get(1);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getCompanyByName(obiCompany.getName(),result);
    }

    private void initializeLMStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (isFirstCallLMStores) {
                    isFirstCallLMStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getLMStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        initializeBricomanStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company lmCompany = getCompanies().get(2);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getCompanyByName(lmCompany.getName(),result);
    }

    private void initializeBricomanStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (isFirstCallBricomanStores) {
                    isFirstCallBricomanStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : getBricomanStores() ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        initializeLocalCompetitorStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company bricomanCompany = getCompanies().get(3);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getCompanyByName(bricomanCompany.getName(),result);
    }

    private void initializeLocalCompetitorStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (isFirstCallLocalCompetitorStores) {
                    isFirstCallLocalCompetitorStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        for (Store store : getLocalCompetitorStores()) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore(store, null);
                        }
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(4);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getCompanyByName(localCompetitorCompany.getName(),result);
    }

    private class StartCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
        @Override
        public void call() {
            prepareData();
            startInitialisationChain();
        }
    }
}
