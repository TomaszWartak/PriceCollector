package com.dev4lazy.pricecollector.remote_data;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

// Klasa używana do inicjalizacji danych lokalnych aplikacji, po pierwszym logowaniu
// TODO inicjalizacja tylko danych testowych, czy wszystkich?
public class RemoteDataInitializer {

    private static RemoteDataInitializer instance = new RemoteDataInitializer();

    // Użytkownicy w mocku zdalnej bazy danych
    private List<RemoteUser> remoteUsers = null;

    private List<RemoteDepartment> remoteDepartments = null;

    private List<RemoteSector> remoteSectors = null;

    private List<RemoteMarket> remoteMarkets = null;

    private List<RemoteModule> remoteModules = null;

    private List<RemoteFamily> remoteFamilies = null;

    private List<RemoteUOProject> remoteUOProjects = null;

    private RemoteAnalysis remoteAnalysis = null;

    private Csv2EanCodeConverter csv2EanCodeConverter;
    private Csv2AnalysisRowConverter csv2AnalysisRowConverter;

    private boolean firstCallRemoteAnalysis = true;

// ---------------------------------------------------------------------------
// Przygotowanie danych


    private RemoteDataInitializer() {    }

    public static RemoteDataInitializer getInstance() {
        if (instance == null) {
            synchronized (RemoteDataInitializer.class) {
                if (instance == null) {
                    instance = new RemoteDataInitializer();
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

    public void initializeRemoteDatabase() {
        /* todo wywal komentarz
            niestety RemoteDatabaseInitializer może być wywołany tylko we Fragmencie lub Aktywności,
            więc nie mogę go tu wywołać. Jest wołany na żądanie w testowych fragmentach.
            new RemoteDatabaseInitializer(this).doConversion();
         */
        initializeRemoteData();
    }

// ---------------------------------------------------------------------------
//  proceduta inicjalizacji danych zdalnych
    private void initializeRemoteData() {
        prepareRemoteData();
        startRemoteDataInitialisationChain();
    }

    private void prepareRemoteData() {
        prepareRemoteUsers();
        prepareRemoteDepartments();
        prepareRemoteSectors();
        prepareRemoteAnalysis();
        prepareRemoteFamilies();
        prepareRemoteMarkets();
        prepareRemoteModules();
        prepareRemoteUOProjects();
        prepareConverters();
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

    private void clearRemoteAnalysis() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllAnalyzes( null );

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
    

    public void clearRemoteDepartments() {
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

    public void clearRemoteSectors() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllSectors( null );
    }

    private void prepareRemoteModules(){
        Resources resources = AppHandle.getHandle().getResources();
        remoteModules = new ArrayList<>();
        RemoteModule remoteModule = new RemoteModule();
        remoteModule.setName( resources.getString(R.string.dummy_string) );
        remoteModules.add(remoteModule);
    }

    public void clearRemoteModules() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteModules( null );
    }

    private void prepareRemoteFamilies(){
        Resources resources = AppHandle.getHandle().getResources();
        remoteFamilies = new ArrayList<>();
        RemoteFamily remoteFamily = new RemoteFamily();
        remoteFamily.setName( resources.getString(R.string.dummy_string) );
        remoteFamilies.add(remoteFamily);
    }

    public void clearRemoteFamilies() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteFamilies( null );
    }

    private void prepareRemoteMarkets(){
        Resources resources = AppHandle.getHandle().getResources();
        remoteMarkets = new ArrayList<>();
        RemoteMarket remoteMarket = new RemoteMarket();
        remoteMarket.setName( resources.getString(R.string.dummy_string) );
        remoteMarkets.add(remoteMarket);
    }

    public void clearRemoteMarkets() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteMarkets( null );
    }

    private void prepareRemoteUOProjects(){
        Resources resources = AppHandle.getHandle().getResources();
        remoteUOProjects = new ArrayList<>();
        RemoteUOProject remoteUOProject = new RemoteUOProject();
        remoteUOProject.setName( resources.getString(R.string.dummy_string) );
        remoteUOProjects.add(remoteUOProject);
    }

    public void clearRemoteUOProjects() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteUOProjects( null );
    }


    /*
    public void initializeAnaylysisRowsAndEanCodesOnly() {
        clearRemoteDatabase();
        prepareConverters();
        // todo tą metodę trzeba usunąć
        populateAnaylysisRowsAndEanCodes(-1);
    }
     */

// ---------------------------------------------------------------------------
// Metoda sprawdzająca, czy baza danych jest zainicjowana. Jeśli nie jest - inicjalizuje bazę.
/* zrezygnowałem z kontroli etapu inicjalizacji mocka zdalnej bazy, żeby nie generować nadmiaru kodu
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
*/

// ---------------------------------------------------------------------------
// Zapis danych


    private void startRemoteDataInitialisationChain() {
        populateRemoteUsers();
        // Nie zrobiłem łąncucha. W przypadku Remote nie ma tworzenia zależności on line.
        // Wystarczy zainicjować dane w odpowiedniej kolejności.
        // A przynajmniej tak mi się wydaje... :-)
        populateRemoteDepartments();
        populateRemoteSectors();
        populateRemoteFamilies();
        populateRemoteMarkets();
        populateRemoteModules();
        populateRemoteUOProjects();
        // todo ? I dupa, bo RemoteAnalysisRow.analysisId zalezy od Analysis.id
        // Czyli populateAnaylysisRowsAndEanCodes jest wołane z populateRemoteAnalysis
        populateRemoteAnalysis();
    }

    public void populateRemoteUsers() {
        for (RemoteUser remoteUser : remoteUsers ) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertUser( remoteUser, null );
        }
    }

    public void populateRemoteDepartments() {
        for (RemoteDepartment remoteDepartment : remoteDepartments) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertDepartment( remoteDepartment, null );
        }
    }

    public void populateRemoteSectors() {
        for (RemoteSector remoteSector : remoteSectors) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertSector( remoteSector, null );
        }
    }

    public void populateRemoteUOProjects() {
        for (RemoteUOProject remoteUOProject : remoteUOProjects) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteUOProject( remoteUOProject, null );
        }
    }

    public void populateRemoteFamilies() {
        for (RemoteFamily remoteFamily : remoteFamilies) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteFamily( remoteFamily, null );
        }
    }

    public void populateRemoteMarkets() {
        for (RemoteMarket remoteMarket : remoteMarkets) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteMarket( remoteMarket, null );
        }
    }

    public void populateRemoteModules() {
        for (RemoteModule remoteModule : remoteModules) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteModule( remoteModule, null );
        }
    }
    
    private void populateRemoteAnalysis() {
       // TODO!! przy MainScreenFragment nie znajduje żadnej remote Analizy...
       // TODO!! sprawdx czy tutaj się coś dopisuje...
        MutableLiveData<Long> analysisInsertResult = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long analysisId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                if (firstCallRemoteAnalysis) {
                    firstCallRemoteAnalysis = false;
                    analysisInsertResult.removeObserver(this); // this = observer...
                    if (analysisId!=-1) {
                        populateAnaylysisRowsAndEanCodes( analysisId.intValue() );
                    }
                }
            }
        };
        analysisInsertResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().insertAnalysis( remoteAnalysis, analysisInsertResult );
    }

    private void populateAnaylysisRowsAndEanCodes( int analysisId ) {
        prepareConverters();
        csv2AnalysisRowConverter.makeAnalisisRowList( analysisId );
        csv2AnalysisRowConverter.insertAllAnalysisRows();
        csv2EanCodeConverter.makeEanCodeList();
        csv2EanCodeConverter.insertAllEanCodes();
    }

    private void prepareConverters() {
        csv2AnalysisRowConverter = new Csv2AnalysisRowConverter();
        csv2EanCodeConverter = new Csv2EanCodeConverter();
    }

}
