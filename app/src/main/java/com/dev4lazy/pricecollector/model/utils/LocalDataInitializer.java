package com.dev4lazy.pricecollector.model.utils;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.model.logic.Remote2LocalConverter;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteFamily;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteMarket;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteModule;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUOProject;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppSettings;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dev4lazy.pricecollector.utils.AppSettings.CASTORAMA_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.COMPANIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.COMPETITORS_SLOTS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.COUNTRIES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.DEPARTMENTS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.DUMMY_FAMILY_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.DUMMY_MARKET_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.DUMMY_MODULE_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.LM_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.LOCAL_COMPETITORS_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.LOCAL_DATA_NOT_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.OBI_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.OWN_STORES_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.SECTORS_DEPARTMENTS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.SECTORS_INITIALIZED;
import static com.dev4lazy.pricecollector.utils.AppSettings.UOPROJECT_INITIALIZED;

/**
 * LocalDataInitializer
 *
 * Klasa używana do inicjalizacji danych lokalnych aplikacji, po pierwszym logowaniu
 */
public class LocalDataInitializer {

    // +++
    private static LocalDataInitializer instance;
    private final AppDataFeeder appDataFeeder;

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
    private List<Store> castoramaStores = null;
    private List<Store> localCompetitorStores = null;
    private List<Sector> sectors = null;
    private List<Department> departments = null;
    private Map<String, Integer> sectorsMap = null;
    private Map<String, Integer> departmentsMap = null;
    private Family dummyFamily = null;
    private Market dummyMarket = null;
    private Module dummyModule = null;
    private UOProject dummyUOProject = null;

    private boolean firstCallCountries = true;
    private boolean firstCallCompanies = true;
    private boolean firstCallOwnStores = true;
    private boolean firstCallObiStores = true;
    private boolean firstCallLMStores = true;
    private boolean firstCallCastoramaStores = true;
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

//--------------------------------------------------------------------------
// Local Database

    public void clearLocalDatabase() {
        // Wyczyszczenie wszystkich tabel
        AppHandle.getHandle().getRepository().getLocalDataRepository().clearDatabase(null);
        // todo to niżej przeniósłbym do AppSettings - czyli warstwę wyżej
        //  inicjalizacja bazy lokalnej -> setLocalDatabaseNotInitialized()
        AppHandle.getHandle().getSettings().saveLocalDatabaseInitialized(false);
        AppHandle.getHandle().getSettings().saveInitialisationStage(LOCAL_DATA_NOT_INITIALIZED);
        AppSettings.getInstance().setLastAnalysisCreationDate( new Date( 0 ) );
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
        int localDatabaseInitalisationStage = AppHandle.getHandle().getSettings().getLocalDatabaseInitialisationStage();
        switch (localDatabaseInitalisationStage) {
            case LOCAL_DATA_NOT_INITIALIZED:
                initializeLocalData();
                break;
            case SECTORS_INITIALIZED:
                prepareLocalData();
                populateDepartments();
                break;
            case DEPARTMENTS_INITIALIZED:
                prepareLocalData();
                populateDepartmentsInSector();
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
                populateCastoramaStores();
                break;
            case CASTORAMA_STORES_INITIALIZED:
                prepareLocalData();
                populateLocalCompetitorStores();
                break;
            case LOCAL_COMPETITORS_STORES_INITIALIZED:
                prepareLocalData();
                populateCompetitorSlotNr1();
                break;
            case COMPETITORS_SLOTS_INITIALIZED:
                prepareLocalData();
                populateDummyFamily();
                break;
            case DUMMY_FAMILY_INITIALIZED:
                prepareLocalData();
                populateDummyMarket();
                break;
            case DUMMY_MARKET_INITIALIZED:
                prepareLocalData();
                populateDummyModule();
                break;
            case DUMMY_MODULE_INITIALIZED:
                prepareLocalData();
                populateDummyUOProject();
                break;
            case UOPROJECT_INITIALIZED:
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
        prepareCountries();
        prepareCompanies();
        prepareOwnStores();
        prepareObiStores();
        prepareLeroyMerlinStores();
        prepareCastoramaStores();
        prepareLocalCompetitorStores();
        prepareCompetitorSlots();
        startPreparingOtherLocalDataChain();
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

    private void prepareCastoramaStores() {
        castoramaStores = appDataFeeder.getCastoramaStoresInitialList();
    }

    private void prepareSectors() {
        getSectorsFromRemoteDatabase( );
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

    private void startPreparingOtherLocalDataChain() {
        prepareSectors(); // todo 2??
    }

    private void getSectorsFromRemoteDatabase( ) {
        MutableLiveData<List<RemoteSector>> getAllRemoteSectorsResult = new MutableLiveData<>();
        Observer<List<RemoteSector>> resultObserver = new Observer<List<RemoteSector>>() {
            @Override
            public void onChanged( List<RemoteSector> remoteSectors ) {
                getAllRemoteSectorsResult.removeObserver(this); // this = observer...
                if (!remoteSectors.isEmpty()) {
                    Remote2LocalConverter remote2LocalConverter = new Remote2LocalConverter();
                    sectors = remote2LocalConverter.createSectors( remoteSectors );
                    getDepartmentsFromRemoteDatabase( );
                }
            }
        };
        getAllRemoteSectorsResult.observeForever( resultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllSectors(getAllRemoteSectorsResult);
    }

    private void getDepartmentsFromRemoteDatabase( ) {
        MutableLiveData<List<RemoteDepartment>> getAllRemoteDepartmentsResult = new MutableLiveData<>();
        Observer<List<RemoteDepartment>> insertingResultObserver = new Observer<List<RemoteDepartment>>() {
            @Override
            public void onChanged( List<RemoteDepartment> remoteDepartments ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllRemoteDepartmentsResult.removeObserver(this); // this = observer...
                if (!remoteDepartments.isEmpty()) {
                    Remote2LocalConverter remote2LocalConverter = new Remote2LocalConverter();
                    departments = remote2LocalConverter.createDepartments( remoteDepartments );
                    getFamilyFromRemoteDatabase(  );
                }
            }
        };
        getAllRemoteDepartmentsResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllDepartments( getAllRemoteDepartmentsResult );
    }

    private void getFamilyFromRemoteDatabase( ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteFamily>> result = new MutableLiveData<>();
        Observer<List<RemoteFamily>> resultObserver = new Observer<List<RemoteFamily>>() {
            @Override
            public void onChanged(List<RemoteFamily> familiesList ) {
                if ((familiesList != null)&&(!familiesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    dummyFamily = converter.createFamily( familiesList.get(0) );
                    getMarketFromRemoteDatabase();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteFamilies( result );
    }

    private void getMarketFromRemoteDatabase( ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteMarket>> result = new MutableLiveData<>();
        Observer<List<RemoteMarket>> resultObserver = new Observer<List<RemoteMarket>>() {
            @Override
            public void onChanged(List<RemoteMarket> marketsList) {
                if ((marketsList!= null)&&(!marketsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    dummyMarket = converter.createMarket( marketsList.get(0) );
                    getModuleFromRemoteDatabase();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteMarkets(result);
    }

      private void getModuleFromRemoteDatabase( ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteModule>> result = new MutableLiveData<>();
        Observer<List<RemoteModule>> resultObserver = new Observer<List<RemoteModule>>() {
            @Override
            public void onChanged(List<RemoteModule> modulesList) {
                if ((modulesList != null)&&(!modulesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    dummyModule = converter.createModule( modulesList.get(0) );
                    getUOProjectFromRemoteDatabase();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteModules(result);
    }


    private void getUOProjectFromRemoteDatabase( ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteUOProject>> result = new MutableLiveData<>();
        Observer<List<RemoteUOProject>> resultObserver = new Observer<List<RemoteUOProject>>() {
            @Override
            public void onChanged(List<RemoteUOProject> uoProjectList) {
                if ((uoProjectList != null)&&(!uoProjectList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    dummyUOProject = converter.createUOProject( uoProjectList.get(0) );
                    startLocalDataPopulationChain();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteUOProjects(result);
    }



// ---------------------------------------------------------------------------
// Zapis danych


    private void startLocalDataPopulationChain() {
        populateSectors();
    }


    private void populateSectors() {
        MutableLiveData<Long> populateSectorsResult = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long lastSectorId) {
                populateSectorsResult.removeObserver(this); // this = observer...
                if (lastSectorId!=null) {
                    AppHandle.getHandle().getSettings().saveInitialisationStage( SECTORS_INITIALIZED );
                    populateDepartments();
                }
            }
        };
        populateSectorsResult.observeForever( resultObserver );
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertSectors( sectors, populateSectorsResult );
    }


    private void populateDepartments() {
        MutableLiveData<Long> populateDepartmentsResult = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long lastDepartmentId) {
                populateDepartmentsResult.removeObserver(this); // this = observer...
                if (lastDepartmentId!=null) {
                    AppHandle.getHandle().getSettings().saveInitialisationStage( DEPARTMENTS_INITIALIZED );
                    createSectors();
                }
            }
        };
        populateDepartmentsResult.observeForever( resultObserver );
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertDepartments( departments, populateDepartmentsResult );
    }

    private void createSectors() {
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
                    sectorsMap = sectorStream.collect(
                            Collectors.toMap( Sector::getName, Sector::getId ) );
                    createDepartments( );
                }
            }
        };
        getAllSectorsResult.observeForever( getAllSectorsResultObserver );
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors( getAllSectorsResult );
    }

    private void createDepartments( ) {
        MutableLiveData<List<Department>> getAllDepartmentsResult = new MutableLiveData<>();
        Observer<List<Department>> getAllDepartmentsResultObserver = new Observer<List<Department>>() {
            @Override
            public void onChanged( List<Department> departmentList ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllDepartmentsResult.removeObserver(this); // this = observer...
                if (!departmentList.isEmpty()) {
                    Stream<Department> departmentStream = departmentList.stream();
                    departmentsMap = departmentStream.collect(
                            Collectors.toMap( Department::getSymbol, Department::getId ) );
                    populateDepartmentsInSector( );
                }
            }
        };
        getAllDepartmentsResult.observeForever( getAllDepartmentsResultObserver );
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments( getAllDepartmentsResult );
    }

    private void populateDepartmentsInSector(
             ) {
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
                AppHandle.getHandle().getSettings().saveInitialisationStage(SECTORS_DEPARTMENTS_INITIALIZED);
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
        ownCountry.setName(AppHandle.getHandle().getSettings().getCountryName());
        ownCountry.setEnglishName(AppHandle.getHandle().getSettings().getEnglishCountryName());
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage(COUNTRIES_INITIALIZED);
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage(COMPANIES_INITIALIZED);
                        populateOwnStores();}
                }
            }
        };
        result.observeForever(resultObserver);
        String ownCountryName = AppHandle.getHandle().getSettings().getCountryName();
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage(OWN_STORES_INITIALIZED);
                        populateLMStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company ownCompany = companies.get(ProductionDataFeeder.getInstance().BRIKO_INDEX);
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage(LM_STORES_INITIALIZED);
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage(OBI_STORES_INITIALIZED);
                        populateCastoramaStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company obiCompany = companies.get(ProductionDataFeeder.getInstance().OBI_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(obiCompany.getName(),result);
    }

    private void populateCastoramaStores() {
        MutableLiveData<List<Company>> result = new MutableLiveData<>();
        Observer<List<Company>> resultObserver = new Observer<List<Company>>() {
            @Override
            public void onChanged(List<Company> companiesList) {
                if (firstCallCastoramaStores) {
                    firstCallCastoramaStores = false;
                    result.removeObserver(this); // this = observer...
                    if (companiesList!=null) {
                        for (Store store : castoramaStores) {
                            store.setCompanyId(companiesList.get(0).getId());
                            AppHandle.getHandle().getRepository().getLocalDataRepository().insertStore( store, null );
                        }
                        AppHandle.getHandle().getSettings().saveInitialisationStage(CASTORAMA_STORES_INITIALIZED);
                        populateLocalCompetitorStores();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company castoramaCompany = companies.get(ProductionDataFeeder.getInstance().CASTORAMA_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(castoramaCompany.getName(),result);
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
                        AppHandle.getHandle().getSettings().saveInitialisationStage( LOCAL_COMPETITORS_STORES_INITIALIZED);
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
                        analysisCompetitorSlot.initialize( 1, companiesList.get(0).getId(), companiesList.get(0).getName() );
                        /* TODO XXX
                        analysisCompetitorSlot.setSlotNr(1);
                        analysisCompetitorSlot.setOtherStoreId( AnalysisCompetitorSlot.NONE );
                        analysisCompetitorSlot.setCompanyId( companiesList.get(0).getId() );
                        analysisCompetitorSlot.setCompanyName( companiesList.get(0).getName() );
                         */
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        populateCompetitorSlotNr2();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company competitorCompany = companies.get( ProductionDataFeeder.getInstance().LEROY_MERLIN_INDEX );
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(competitorCompany.getName(),result);
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
                        analysisCompetitorSlot.initialize( 2, companiesList.get(0).getId(), companiesList.get(0).getName() );
                        /* TODO XXX
                        analysisCompetitorSlot.setSlotNr(2);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                         */
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        populateCompetitorSlotNr3();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company competitorCompany =  companies.get( ProductionDataFeeder.getInstance().OBI_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(competitorCompany.getName(),result);
    }

    // Slot nr 3 = CASTORAMA
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
                        analysisCompetitorSlot.initialize( 3, companiesList.get(0).getId(), companiesList.get(0).getName() );
                        /* TODO XXX
                        analysisCompetitorSlot.setSlotNr(3);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                        */
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        populateCompetitorSlotNr4();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company competitorCompany = companies.get( ProductionDataFeeder.getInstance().CASTORAMA_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(competitorCompany.getName(),result);
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
                        analysisCompetitorSlot.initialize( 4, companiesList.get(0).getId(), companiesList.get(0).getName() );
                        /* TODO XXX
                        analysisCompetitorSlot.setSlotNr(4);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                         */
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        populateCompetitorSlotNr5();
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company competitorCompany = companies.get( ProductionDataFeeder.getInstance().LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(competitorCompany.getName(),result);
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
                        analysisCompetitorSlot.initialize( 5, companiesList.get(0).getId(), companiesList.get(0).getName() );
                        /* TODO XXX
                        analysisCompetitorSlot.setSlotNr(5);
                        analysisCompetitorSlot.setOtherStoreId(AnalysisCompetitorSlot.NONE);
                        analysisCompetitorSlot.setCompanyId(companiesList.get(0).getId());
                        analysisCompetitorSlot.setCompanyName(companiesList.get(0).getName());
                         */
                        AppHandle.getHandle().getRepository().getLocalDataRepository().insertAnalysisCompetitorSlot(analysisCompetitorSlot,null);
                        AppHandle.getHandle().getSettings().saveInitialisationStage( COMPETITORS_SLOTS_INITIALIZED );
                        populateDummyFamily( );
                    }
                }
            }
        };
        result.observeForever(resultObserver);
        Company competitorCompany = companies.get( ProductionDataFeeder.getInstance().LOCAL_COMPETITOR_INDEX);
        AppHandle.getHandle().getRepository().getLocalDataRepository().findCompanyByName(competitorCompany.getName(),result);
    }

    private void populateDummyFamily( ) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long familyId ) {
                if ((familyId != null)&&(familyId>0)) {
                    result.removeObserver(this); // this = observer...
                    AppHandle.getHandle().getSettings().saveInitialisationStage( DUMMY_FAMILY_INITIALIZED );
                    populateDummyMarket();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertFamily( dummyFamily, result );
    }

    private void populateDummyMarket() {
        MutableLiveData<Long> finalResult = new MutableLiveData<>();
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long marketId ) {
                if ((marketId != null)&&(marketId>0)) {
                    result.removeObserver(this); // this = observer...
                    AppHandle.getHandle().getSettings().saveInitialisationStage( DUMMY_MARKET_INITIALIZED );
                    populateDummyModule( );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertMarket( dummyMarket, result );
    }

    private void populateDummyModule( ) {
        MutableLiveData<Long> finalResult = new MutableLiveData<>();
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long marketId ) {
                if ((marketId != null)&&(marketId>0)) {
                    result.removeObserver(this); // this = observer...
                    AppHandle.getHandle().getSettings().saveInitialisationStage( DUMMY_MODULE_INITIALIZED );
                    populateDummyUOProject( );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertModule( dummyModule, result );
    }

    private void populateDummyUOProject( ) {
        MutableLiveData<Long> finalResult = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long marketId) {
                if ((marketId != null) && (marketId > 0)) {
                    finalResult.removeObserver(this); // this = observer...
                    AppHandle.getHandle().getSettings().setLocalDatabaseInitialized();
                }
            }
        };
        finalResult.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertUOProject( dummyUOProject, finalResult);
    }

    private class StartCallback implements LocalDataRepository.AfterDatabaseClearedCallback {
        @Override
        public void call() {
            initializeLocalData();
        }
    }

}
