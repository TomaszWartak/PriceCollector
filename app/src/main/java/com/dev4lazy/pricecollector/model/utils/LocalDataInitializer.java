package com.dev4lazy.pricecollector.model.utils;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteUser;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.dev4lazy.pricecollector.utils.AppPreferences.BRICOMAN_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COMPANIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COMPETITORS_SLOTS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COUNTRIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LM_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_COMPETITORS_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_NOT_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OBI_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OWN_STORES_INITIALIZED;

// Klasa używana do inicjalizacji danych lokalnych aplikacji, po pierwszym logowaniu
// TODO inicjalizacja tylko danych testowych, czy wszystkich?
public class LocalDataInitializer {

    /*
    public final String CASTORAMA_NAME = "Castorama";
    public final String LEROY_MERLIN_NAME = "Leroy Merlin";
    public final String OBI_NAME = "OBI";
    public final String BRICOMAN_NAME = "BRICOMAN";
    public final String LOCAL_COMPETITOR_NAME = "Konkurent lokalny";
    */

    public final String[] COMPANIES_NAMES = {"Castorama", "Leroy Merlin", "OBI", "BRICOMAN", "Konkurent lokalny"};
    private final int CASTORAMA_INDEX = 0;
    private final int LEROY_MERLIN_INDEX = 1;
    private final int OBI_INDEX = 2;
    private final int BRICOMAN_INDEX = 3;
    private final int LOCAL_COMPETITOR_INDEX = 4;

    private static LocalDataInitializer instance = new LocalDataInitializer();

    // Użytkownicy w mocku zdalnej bazy danych
    private List<RemoteUser> remoteUsers = null;

    private List<RemoteDepartment> remoteDepartments = null;

    private List<RemoteSector> remoteSectors = null;

    private RemoteAnalysis remoteAnalysis = null;

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
    private boolean firstCallCompetitorsSlotsNr1 = true;
    private boolean firstCallCompetitorsSlotsNr2 = true;
    private boolean firstCallCompetitorsSlotsNr3 = true;
    private boolean firstCallCompetitorsSlotsNr4 = true;
    private boolean firstCallCompetitorsSlotsNr5 = true;

// ---------------------------------------------------------------------------
// Przygotowanie danych


    private LocalDataInitializer() {    }

    public static LocalDataInitializer getInstance() {
        if (instance == null) {
            synchronized (LocalDataInitializer.class) {
                if (instance == null) {
                    instance = new LocalDataInitializer();
                }
            }
        }
        return instance;
    }

//--------------------------------------------------------------------------
// Remote Database

    public void clearRemoteDatabase() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().clearDatabase();
    }

    public void InitializeRemoteDatabase() {
        /* niestety RemoteDatabaseInitializer może być wywołany tylko we Fragmencie lub Aktywności,
            więc nie mogę go tu wywołać. Jest wołany na żądanie w testowych fragmentach.
            new RemoteDatabaseInitializer(this).doConversion();
         */
        initializeRemoteUsersOnly();
        // todo chain remote
    }

// ---------------------------------------------------------------------------
//  proceduta inicjalizacji danych zdalnych
    private void initializeLocalData() {
        prepareRemoteData();
        startRemoteDataInitialisationChain();
    }

    private void prepareRemoteData() {
        prepareRemoteUsers();
        prepareRemoteDepartments();
        prepareRemoteSectors();
        prepareRemoteAnalysis();
        // todo w Convererze RemoteEanCodes oddziel od RemoteAnalysisiRow po przeniesieniu permmisions do StartScreenFragment
    }

    public void initializeRemoteUsersOnly() {
        prepareRemoteUsers();
        populateRemoteUsers();
    }

    private void prepareRemoteUsers() {
        Resources resources = AppHandle.getHandle().getResources();
        remoteUsers = new ArrayList<>();
        RemoteUser remoteUser = new RemoteUser();
        remoteUser.setLogin("nowak_j");
        remoteUser.setName("Jacek Nowak");
        remoteUser.setOwnStoreNumber("8033");
        remoteUser.setDepartmentSymbol(resources.getString(R.string.r70_department_symbol));
        remoteUser.setSectorName(resources.getString(R.string.sdek_sector_name));
        remoteUsers.add( remoteUser );
        remoteUser = new RemoteUser();
        remoteUser.setLogin("rutkowski_p");
        remoteUser.setName("Piotr Rutkowski");
        remoteUser.setOwnStoreNumber("8007");
        remoteUser.setDepartmentSymbol(resources.getString(R.string.r90_department_symbol));
        remoteUser.setSectorName(resources.getString(R.string.sbud_sector_name));
        remoteUsers.add( remoteUser );
        remoteUser = new RemoteUser();
        remoteUser.setLogin("sroka_p");
        remoteUser.setName("Piotr Sroka");
        remoteUser.setOwnStoreNumber("8015");
        remoteUser.setDepartmentSymbol(resources.getString(R.string.r20_department_symbol));
        remoteUser.setSectorName(resources.getString(R.string.srem_sector_name));
        remoteUsers.add( remoteUser );
    }

    private void populateRemoteUsers() {
        for (RemoteUser remoteUser : remoteUsers ) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertUser( remoteUser, null );
        }
    }

    public void clearRemoteUsers() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllUsers( null );
    }

    private void prepareRemoteAnalysis() {
        remoteAnalysis = new RemoteAnalysis();
        try {
            remoteAnalysis.setCreationDate( new DateConverter().string2Date("2019-10-24") );
        } catch (ParseException parseException) {

        }
        try {
            remoteAnalysis.setDueDate(new DateConverter().string2Date("2019-10-31"));
        } catch (ParseException parseException) {

        }
        remoteAnalysis.setFinished( false );
    }

    private void populateRemoteAnalysis() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().insertAnalysis( remoteAnalysis, null );

    }

    private void clearRemoteAnalysis() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllAnalysis( null );

    }

    public void prepareRemoteDepartments() {
        Resources resources = AppHandle.getHandle().getResources();
        remoteDepartments = new ArrayList<>();
        RemoteDepartment remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r06_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r06_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r10_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r10_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r20_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r20_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r30_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r30_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r40_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r40_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r50_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r50_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r60_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r60_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r70_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r70_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r80_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r80_department_symbol));
        remoteDepartments.add(remoteDepartment);
        remoteDepartment = new RemoteDepartment();
        remoteDepartment.setName(resources.getString(R.string.r90_department_name));
        remoteDepartment.setSymbol(resources.getString(R.string.r90_department_symbol));
        remoteDepartments.add(remoteDepartment);
    }
    
    private void populateRemoteDepartments() {
        for (RemoteDepartment remoteDepartment : remoteDepartments) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertDepartment( remoteDepartment, null );
        }
    }
    
    private void clearRemoteDepartments() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllDepartments( null );
    }

    public void prepareRemoteSectors() {
        Resources resources = AppHandle.getHandle().getResources();
        remoteSectors = new ArrayList<>();
        RemoteSector remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.sbud_sector_name));
        remoteSectors.add(remoteSector);
        remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.srem_sector_name));
        remoteSectors.add(remoteSector);
        remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.surz_sector_name));
        remoteSectors.add(remoteSector);
        remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.sdek_sector_name));
        remoteSectors.add(remoteSector);
        remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.sogr_sector_name));
        remoteSectors.add(remoteSector);
        remoteSector = new RemoteSector();
        remoteSector.setName(resources.getString(R.string.sok_sector_name));
        remoteSectors.add(remoteSector);
    }

    private void populateRemoteSectors() {
        for (RemoteSector remoteSector : remoteSectors) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertSector( remoteSector, null );
        }
    }

    private void clearRemoteSectors() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllSectors( null );
    }

//--------------------------------------------------------------------------
// Local Database

    public void clearLocalDatabase() {
        AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(null);
        // todo to niżej przeniósłbym do AppSettings - czyli warstwę wyżej
        //  inicjalizacja bazy lokalnej -> setLocalDatabaseNotInitialized()
        AppHandle.getHandle().getPrefs().setLocalDatabaseInitialized(false);
        AppHandle.getHandle().getPrefs().setInitialisationStage(LOCAL_DATA_NOT_INITIALIZED);
    }

    public void initializeLocalDatabase() {
        // todo !!!! to jest wołane także przy powrocie z przeglądania artykułow
        // todo usuń czyszczenie bazy
        // AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(new StartCallback());
        // todo normalnie ma być jn.
        checkAndSetIfLocaDatabaselNotInitialized();
    }

// ---------------------------------------------------------------------------
// Metoda sprawdzająca, czy baza danych jest zainicjowana. Jeśli nie jest - inicjalizuje bazę.
    private void checkAndSetIfLocaDatabaselNotInitialized() {
        int localDatabaseInitalisationStage = AppHandle.getHandle().getPrefs().getLocalDatabaseInitialisationStage();
        switch (localDatabaseInitalisationStage) {
            case LOCAL_DATA_NOT_INITIALIZED:
                initializeLocalData();
                break;
            case COUNTRIES_INITIALIZED:
                prepareLocalData();
                populateCompanies();
                break;
            case COMPANIES_INITIALIZED:
                prepareLocalData();
                populateOwnStores();
                break;
            case OWN_STORES_INITIALIZED:
                prepareLocalData();
                populateObiStores();
                break;
            case OBI_STORES_INITIALIZED:
                prepareLocalData();
                populateLMStores();
                break;
            case LM_STORES_INITIALIZED:
                prepareLocalData();
                populateBricomanStores();
                break;
            case BRICOMAN_STORES_INITIALIZED:
                prepareLocalData();
                populateLocalCompetitorStores();
                break;
            case LOCAL_COMPETITORS_STORES_INITIALIZED:
                prepareLocalData();
                populateCompetitorSlotNr1();
                break;
            case COMPETITORS_SLOTS_INITIALIZED:
                // inicjalizacja była kompletna
                break;
        }
    }

// ---------------------------------------------------------------------------
//  proceduta inicjalizacji danych lokalnych
    private void initializeLocalData() {
        prepareLocalData();
        startLocalDataInitialisationChain();
    }

// ---------------------------------------------------------------------------
// Przygotowanie danych
    private void prepareLocalData() {
        prepareCountries();
        prepareCompanies();
        prepareOwnStores();
        prepareObiStores();
        prepareLeroyMerlinStores();
        prepareBricomanStores();
        prepareLocalCompetitorStores();
        prepareCompetitorSlots();
    }

    private void prepareCountries() {
        countries = new ArrayList<>();
        Country country = new Country();
        country.setName("Polska");
        country.setEnglishName("Poland");
        countries.add(country);
    }

    private void prepareCompanies() {
        companies = new ArrayList<>();
        // Nie zmieniaj kolejności, bo niżej ma to znaczenie :-)
        Company company = new Company();
        company.setName(COMPANIES_NAMES[CASTORAMA_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[LEROY_MERLIN_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[OBI_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[BRICOMAN_INDEX]);
        companies.add(company);
        company = new Company();
        company.setName(COMPANIES_NAMES[LOCAL_COMPETITOR_INDEX]);
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

    private void prepareCompetitorSlots() {
        /* todo?
        AnalysisCompetitorSlot slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(1);
        slot.setCompanyId(1);
        slot.setOtherStoreId(1);
        AnalysisCompetitorSlotFullInfo slotFullInfo = new AnalysisCompetitorSlotFullInfo(slot);
        slotFullInfo.setCompetitorCompanyName("OBI");
        slotFullInfo.setCompetitorStoreName("OBI Rybnik");
        analysisCompetitorSlotFullInfos.add(slotFullInfo);
        slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(2);
        slot.setCompanyId(2);
        slot.setOtherStoreId(-1);
        slotFullInfo = new AnalysisCompetitorSlotFullInfo(slot);
        slotFullInfo.setCompetitorCompanyName("LM");
        slotFullInfo.setCompetitorStoreName("");
        analysisCompetitorSlotFullInfos.add(slotFullInfo);
        slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(3);
        slot.setCompanyId(3);
        slot.setOtherStoreId(-1);
        slotFullInfo = new AnalysisCompetitorSlotFullInfo(slot);
        slotFullInfo.setCompetitorCompanyName("BRICOMAN");
        slotFullInfo.setCompetitorStoreName("");
        analysisCompetitorSlotFullInfos.add(slotFullInfo);
        slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(4);
        slot.setCompanyId(4);
        slot.setOtherStoreId(4);
        slotFullInfo = new AnalysisCompetitorSlotFullInfo(slot);
        slotFullInfo.setCompetitorCompanyName("Konkurent lokalny 1");
        slotFullInfo.setCompetitorStoreName("DyWyTa, Rybnik");
        analysisCompetitorSlotFullInfos.add(slotFullInfo);
        slot = new AnalysisCompetitorSlot();
        slot.setSlotNr(5);
        slot.setCompanyId(-1);
        slot.setOtherStoreId(-1);
        slotFullInfo = new AnalysisCompetitorSlotFullInfo(slot);
        slotFullInfo.setCompetitorCompanyName("");
        slotFullInfo.setCompetitorStoreName("");
        analysisCompetitorSlotFullInfos.add(slotFullInfo);

         */
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

// ---------------------------------------------------------------------------
// Zapis danych


    private void startLocalDataInitialisationChain() {
        populateCountries();
    }

    private void populateCountries() {
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
                        populateCompanies();
                    }
                }
            }
        };
        ownCountryInsertResult.observeForever(insertingResultObserver);

        // Dodanie kraju własnego
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertCountry(ownCountry,ownCountryInsertResult);
    }

    private void populateCompanies() {
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
                        populateOwnStores();}
                }
            }
        };
        result.observeForever(resultObserver);
        String ownCountryName = AppHandle.getHandle().getPrefs().getCountryName();
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCountryByName(ownCountryName,result);
    }

    private void populateOwnStores() {
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
                        populateLMStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company ownCompany = getCompanies().get(CASTORAMA_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(ownCompany.getName(),result);
    }

    private void populateLMStores() {
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
                        populateObiStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company lmCompany = getCompanies().get(LEROY_MERLIN_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(lmCompany.getName(),result);
    }

    private void populateObiStores() {
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
                        populateBricomanStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company obiCompany = getCompanies().get(OBI_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(obiCompany.getName(),result);
    }

    private void populateBricomanStores() {
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
                        populateLocalCompetitorStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company bricomanCompany = getCompanies().get(BRICOMAN_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(bricomanCompany.getName(),result);
    }

    private void populateLocalCompetitorStores() {
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
                        populateCompetitorSlotNr1();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 1 = LM
    private void populateCompetitorSlotNr1() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCompetitorsSlotsNr1) {
                    firstCallCompetitorsSlotsNr1 = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        AnalysisCompetitorSlot analysisCompetitorSlot = new AnalysisCompetitorSlot();
                        analysisCompetitorSlot.setSlotNr(1);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        initializeCompetitorSlotNr2();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(LEROY_MERLIN_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 2 = OBI
    private void initializeCompetitorSlotNr2() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCompetitorsSlotsNr2) {
                    firstCallCompetitorsSlotsNr2 = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        AnalysisCompetitorSlot analysisCompetitorSlot = new AnalysisCompetitorSlot();
                        analysisCompetitorSlot.setSlotNr(2);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        initializeCompetitorSlotNr3();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(OBI_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 3 = BRICOMAN
    private void initializeCompetitorSlotNr3() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCompetitorsSlotsNr3) {
                    firstCallCompetitorsSlotsNr3 = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        AnalysisCompetitorSlot analysisCompetitorSlot = new AnalysisCompetitorSlot();
                        analysisCompetitorSlot.setSlotNr(3);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        initializeCompetitorSlotNr4();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(BRICOMAN_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 4 = Konkurent lokalny 1
    private void initializeCompetitorSlotNr4() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCompetitorsSlotsNr4) {
                    firstCallCompetitorsSlotsNr4 = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        AnalysisCompetitorSlot analysisCompetitorSlot = new AnalysisCompetitorSlot();
                        analysisCompetitorSlot.setSlotNr(4);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        initializeCompetitorSlotNr5();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 5 = Konkurent lokalny 2
    private void initializeCompetitorSlotNr5() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCompetitorsSlotsNr5) {
                    firstCallCompetitorsSlotsNr5 = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList != null) {
                        AnalysisCompetitorSlot analysisCompetitorSlot = new AnalysisCompetitorSlot();
                        analysisCompetitorSlot.setSlotNr(5);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        AppHandle.getHandle().getSettings().setLocalDatabaseInitialized();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = getCompanies().get(LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    private class StartCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
        @Override
        public void call() {
            initializeLocalData();
        }
    }
}
