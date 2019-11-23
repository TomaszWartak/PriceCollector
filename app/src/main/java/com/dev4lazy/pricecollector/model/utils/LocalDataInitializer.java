package com.dev4lazy.pricecollector.model.utils;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dev4lazy.pricecollector.utils.AppPreferences.BRICOMAN_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COMPANIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COMPETITORS_SLOTS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.COUNTRIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LM_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_COMPETITORS_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.LOCAL_DATA_NOT_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OBI_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.OWN_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppPreferences.SECTORS_DEPARTMENTS_INITIALIZED;

/**
 * LocalDataInitializer
 *
 * Klasa używana do inicjalizacji danych lokalnych aplikacji, po pierwszym logowaniu
 */
public class LocalDataInitializer {

    // +++
    private static LocalDataInitializer instance;
    private AppDataFeeder appDataFeeder;

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


    private LocalDataInitializer() {
        appDataFeeder = AppDataFeeder.getInstance();
    }

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

// ---------------------------------------------------------------------------
// Przygotowanie danych



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
            case SECTORS_DEPARTMENTS_INITIALIZED:
                prepareLocalData();
                populateCountries();
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
    }

// ---------------------------------------------------------------------------
// Przygotowanie danych
    private void prepareLocalData() {
        copySectorsAndDepartmentsFromRemoteDatabase( );
    }

    private void copySectorsAndDepartmentsFromRemoteDatabase( ) {
        MutableLiveData<Long> finalResult = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                finalResult.removeObserver(this);
                copyDummyFamiliesEtcFromRemoteDatabase( finalResult );
            }
        };
        finalResult.observeForever(resultObserver);
        AnalysisDataUpdater.getInstance().copySectorsAndDepartmentsFromRemoteDatabase( finalResult );
    }

    private void copyDummyFamiliesEtcFromRemoteDatabase( MutableLiveData<Long> finalResult ) {
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                finalResult.removeObserver(this);
                prepareCountries();
                prepareCompanies();
                prepareOwnStores();
                prepareObiStores();
                prepareLeroyMerlinStores();
                prepareBricomanStores();
                prepareLocalCompetitorStores();
                prepareCompetitorSlots();
                startLocalDataPopulationChain();
            }
        };
        finalResult.observeForever(resultObserver);
        AnalysisDataUpdater.getInstance().copyDummyFamiliesEtcFromRemoteDatabase( finalResult );
    }

    private void prepareCountries() {
        countries = appDataFeeder.getCountriesInitialList();
    }

    private void prepareCompanies() {
        companies = appDataFeeder.getCompaniesInitialList();
    }

   private void prepareOwnStores() {
        ownStores = appDataFeeder.getOwnStoresInitialList();
    }

    private void prepareObiStores() {
        obiStores = appDataFeeder.getObiStoresInitialList();
    }

    private void prepareLeroyMerlinStores() {
        lmStores = appDataFeeder.getLmStoresInitialList();
    }

    private void prepareBricomanStores() {
        bricomanStores = appDataFeeder.getBricomanStoresInitialList();
    }

    private void prepareLocalCompetitorStores() {
        localCompetitorStores = appDataFeeder.getLocalCompetitorStoresInitialList();
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
// Zapis danych


    private void startLocalDataPopulationChain() {
        prepareSectors();
    }

    private void prepareSectors() {
        // todo no a teraz jak jesteś taki mądry, to połącz...
        // odczyt sektorów
        MutableLiveData<List<Sector>> getAllSectorsResult = new MutableLiveData<>();
        Observer<List<Sector>> getAllSectorsResultObserver = new Observer<List<Sector>>() {
            @Override
            public void onChanged( List<Sector> sectorList ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllSectorsResult.removeObserver(this); // this = observer...
                if (!sectorList.isEmpty()) {
                    Stream< Sector > sectorStream = sectorList.stream();
                    Map< String, Integer > sectorsMap = sectorStream.collect(
                            Collectors.toMap( Sector::getName, Sector::getId ) );
                    prepareDepartments( sectorsMap );
                }
            }
        };
        getAllSectorsResult.observeForever( getAllSectorsResultObserver );
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors( getAllSectorsResult );
    }

    private void prepareDepartments( Map<String, Integer> sectorsMap ) {
        MutableLiveData<List<Department>> getAllDepartmentsResult = new MutableLiveData<>();
        Observer<List<Department>> getAllDepartmentsResultObserver = new Observer<List<Department>>() {
            @Override
            public void onChanged( List<Department> departmentList ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllDepartmentsResult.removeObserver(this); // this = observer...
                if (!departmentList.isEmpty()) {
                    Stream<Department> departmentStream = departmentList.stream();
                    Map<String, Integer> departmentsMap = departmentStream.collect(
                            Collectors.toMap( Department::getSymbol, Department::getId ) );
                    populateDepartmentsInSector( sectorsMap, departmentsMap );
                }
            }
        };
        getAllDepartmentsResult.observeForever( getAllDepartmentsResultObserver );
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments( getAllDepartmentsResult );
    }

    private void populateDepartmentsInSector(
            Map<String, Integer> sectorsMap,
            Map<String, Integer> departmentsMap ) {
        DepartmentInSector departmentInSector = new DepartmentInSector();
        Resources resources = AppHandle.getHandle().getResources();
        Integer sectorId;
        Integer departmentId;

        // Budujesz
        sectorId = sectorsMap.get( resources.getString(R.string.sbud_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r10_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r30_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r90_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Urządzasz
        sectorId = sectorsMap.get( resources.getString(R.string.surz_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r30_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r40_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r50_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Dekorujesz
        sectorId = sectorsMap.get( resources.getString(R.string.sdek_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r10_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r40_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r70_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r80_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Remontujesz
        sectorId = sectorsMap.get( resources.getString(R.string.srem_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r10_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r20_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r50_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r80_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );
        departmentId = departmentsMap.get( resources.getString(R.string.r90_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Ogród
        sectorId = sectorsMap.get( resources.getString(R.string.sogr_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r60_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Obsługi Klienta
        sectorId = sectorsMap.get( resources.getString(R.string.sok_sector_name) );
        departmentInSector.setSectorId( sectorId );
        departmentId = departmentsMap.get( resources.getString(R.string.r06_department_symbol) );
        departmentInSector.setDepartmentId( departmentId );
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertDepartmentInSector( departmentInSector, null );

        // Sprawdzenie
        MutableLiveData<List<DepartmentInSector>> getAllDepartmentsInSectorsResult = new MutableLiveData<>();
        Observer<List<DepartmentInSector>> getAllDepartmentsInSectorsResultObserver = new Observer<List<DepartmentInSector>>() {
            @Override
            public void onChanged( List<DepartmentInSector> departmensInSectorsList ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllDepartmentsInSectorsResult.removeObserver(this); // this = observer...
                if (!departmensInSectorsList.isEmpty()) {
                }
                AppHandle.getHandle().getPrefs().setInitialisationStage(SECTORS_DEPARTMENTS_INITIALIZED);
                populateCountries();
            }
        };
        getAllDepartmentsInSectorsResult.observeForever( getAllDepartmentsInSectorsResultObserver );
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartmentInSectors( getAllDepartmentsInSectorsResult );

    }

    private void populateCountries() {
        // todo odczyt kraju z preferencji
        // todo tu zamotka jest... Z preferencji, czy z DataInitializera?
        /*
        Country ownCountry = new Country();
        ownCountry.setName(AppHandle.getHandle().getPrefs().getCountryName());
        ownCountry.setEnglishName(AppHandle.getHandle().getPrefs().getEnglishCountryName());
         */
        Country ownCountry = countries.get(0);

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
                        for (Company company : companies ) {
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
                        for (OwnStore store : ownStores ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertOwnStore( store, null );
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage(OWN_STORES_INITIALIZED);
                        populateLMStores();                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company ownCompany = companies.get(ProductionDataFeeder.getInstance().CASTORAMA_INDEX);
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
                        for (Store store : lmStores ) {
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
        Company lmCompany = companies.get(ProductionDataFeeder.getInstance().LEROY_MERLIN_INDEX);
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
                        for (Store store : obiStores ) {
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
        Company obiCompany = companies.get(ProductionDataFeeder.getInstance().OBI_INDEX);
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
                        for (Store store : bricomanStores ) {
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
        Company bricomanCompany = companies.get(ProductionDataFeeder.getInstance().BRICOMAN_INDEX);
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
                        for (Store store : localCompetitorStores ) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore(store, null);
                        }
                        AppHandle.getHandle().getPrefs().setInitialisationStage( LOCAL_COMPETITORS_STORES_INITIALIZED);
                        populateCompetitorSlotNr1();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = companies.get( ProductionDataFeeder.getInstance().LOCAL_COMPETITOR_INDEX);
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
                        analysisCompetitorSlot.setOtherStoreId( AnalysisCompetitorSlot.NONE );
                        analysisCompetitorSlot.setCompanyId( companiesList.get(0).getId() );
                        analysisCompetitorSlot.setCompanyName( companiesList.get(0).getName() );
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        populateCompetitorSlotNr2();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = companies.get( ProductionDataFeeder.getInstance().LEROY_MERLIN_INDEX );
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 2 = OBI
    private void populateCompetitorSlotNr2() {
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
                        populateCompetitorSlotNr3();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany =  companies.get( ProductionDataFeeder.getInstance().OBI_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 3 = BRICOMAN
    private void populateCompetitorSlotNr3() {
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
                        populateCompetitorSlotNr4();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = companies.get( ProductionDataFeeder.getInstance().BRICOMAN_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 4 = Konkurent lokalny 1
    private void populateCompetitorSlotNr4() {
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
                        populateCompetitorSlotNr5();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company localCompetitorCompany = companies.get( ProductionDataFeeder.getInstance().LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    // Slot nr 5 = Konkurent lokalny 2
    private void populateCompetitorSlotNr5() {
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
        Company localCompetitorCompany = companies.get( ProductionDataFeeder.getInstance().LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(localCompetitorCompany.getName(),result);
    }

    private class StartCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
        @Override
        public void call() {
            initializeLocalData();
        }
    }
}
