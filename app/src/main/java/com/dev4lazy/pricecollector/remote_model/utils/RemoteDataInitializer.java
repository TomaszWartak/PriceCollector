package com.dev4lazy.pricecollector.remote_model.utils;

import android.content.res.Resources;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisArticleJoinSaver;
import com.dev4lazy.pricecollector.model.logic.RemoteDataRepository;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteFamily;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteMarket;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteModule;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUOProject;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.utils.TaskLink;
import com.dev4lazy.pricecollector.view.utils.PopupWindowWrapper;
import com.dev4lazy.pricecollector.view.utils.ProgressBarWrapper;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;
import com.dev4lazy.pricecollector.view.utils.ProgressPresentingManager;
import com.dev4lazy.pricecollector.view.utils.TextViewMessageWrapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DATA_SIZE_UNKNOWN;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DONT_HIDE_WHEN_FINISHED;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.HIDE_WHEN_FINISHED;

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

    private List<RemoteAnalysis> remoteAnalyzes = null;

    private Csv2EanCodeConverter csv2EanCodeConverter;
    private Csv2AnalysisRowConverter csv2AnalysisRowConverter;

    private final boolean firstCallRemoteAnalysis = true;

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
        // Czyści wszystkie tabele
        AppHandle.getHandle().getRepository().getRemoteDataRepository().clearDatabase();
    }

// ---------------------------------------------------------------------------
//  proceduta inicjalizacji danych zdalnych
    public void initializeRemoteData( ProgressPresentingManager progressPresentingManager ) {
        prepareRemoteData();
        // TODO XXX startRemoteDataInitialisationChain();
        startRemoteDataInitialisationChain_2( progressPresentingManager );
    }

    private void prepareRemoteData() {
        prepareRemoteUsers();
        prepareRemoteDepartments();
        prepareRemoteSectors();
        prepareRemoteAnalyzes();
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
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteRemoteAllDepartments( null );
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
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteSectors( null );
    }

    public void prepareRemoteAnalyzes() {
        remoteAnalyzes = new ArrayList<>();
        prepareRemoteAnalysis1( remoteAnalyzes );
        prepareRemoteAnalysis2( remoteAnalyzes );
        prepareRemoteAnalysis3( remoteAnalyzes );
    }

    private void prepareRemoteAnalysis1( List<RemoteAnalysis> remoteAnalyzes ) {
        RemoteAnalysis remoteAnalysis = new RemoteAnalysis();
        try {
            remoteAnalysis.setCreationDate( new DateConverter().string2Date("2021-09-15") );
        } catch (ParseException parseException) {

        }
        try {
            remoteAnalysis.setDueDate(new DateConverter().string2Date("2021-09-30"));
        } catch (ParseException parseException) {

        }
        remoteAnalysis.setFinished( false );
        remoteAnalyzes.add( remoteAnalysis );
    }

    private void prepareRemoteAnalysis2( List<RemoteAnalysis> remoteAnalyzes ) {
        RemoteAnalysis remoteAnalysis = new RemoteAnalysis();
        try {
            remoteAnalysis.setCreationDate( new DateConverter().string2Date("2021-10-07") );
        } catch (ParseException parseException) {

        }
        try {
            remoteAnalysis.setDueDate(new DateConverter().string2Date("2021-10-21"));
        } catch (ParseException parseException) {

        }
        remoteAnalysis.setFinished( false );
        remoteAnalyzes.add( remoteAnalysis );
    }

    private void prepareRemoteAnalysis3( List<RemoteAnalysis> remoteAnalyzes ) {
        RemoteAnalysis remoteAnalysis = new RemoteAnalysis();
        try {
            remoteAnalysis.setCreationDate( new DateConverter().string2Date("2021-11-05") );
        } catch (ParseException parseException) {

        }
        try {
            remoteAnalysis.setDueDate(new DateConverter().string2Date("2021-11-30"));
        } catch (ParseException parseException) {

        }
        remoteAnalysis.setFinished( false );
        remoteAnalyzes.add( remoteAnalysis );
    }

    private void clearRemoteAnalysis() {
        AppHandle.getHandle().getRepository().getRemoteDataRepository().deleteAllRemoteAnalyzes( null );

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

    private void prepareConverters() {
        if (csv2AnalysisRowConverter==null) {
            csv2AnalysisRowConverter = new Csv2AnalysisRowConverter();
        } else {
            csv2AnalysisRowConverter.reset();
        }
        if (csv2EanCodeConverter==null) {
            csv2EanCodeConverter = new Csv2EanCodeConverter();
        } else {
            csv2EanCodeConverter.clearRemoteEanCodeList();
        }
    }


    /* TODO XXX
    public void initializeAnaylysisRowsAndEanCodesOnly() {
        clearRemoteDatabase();
        prepareConverters();
        // todo tą metodę trzeba usunąć
        populateRemoteAnalysisRows(-1);
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

    // TODO lift up
    private ProgressPresentingManager progressPresentingManager;

    private void startRemoteDataInitialisationChain_2( ProgressPresentingManager progressPresentingManager ) {
        this.progressPresentingManager = progressPresentingManager;
        new TaskChain()
                .addTaskLink( new RemoteUsersSaver() )
                .addTaskLink( new RemoteDepartmentsSaver() )
                .addTaskLink( new RemoteSectorsSaver() )
                .addTaskLink( new RemoteUOProjectsSaver() )
                .addTaskLink( new RemoteFamiliesSaver() )
                .addTaskLink( new RemoteMarketsSaver() )
                .addTaskLink( new RemoteModulesSaver() )
                .addTaskLink( new RemoteAnalysisSaver( 0 ) )
                .addTaskLink( new RemoteAnalysisRowsSaver() )
                .addTaskLink( new RemoteEanCodesSaver() )
                .startIt( );
    }

    // TODO XXX
    public void populateRemoteUsers() {
        for (RemoteUser remoteUser : remoteUsers ) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteUser( remoteUser, null );
        }
    }

    private class RemoteUsersSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteUsers.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteUsers.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista użytkowników" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteUsers( remoteUsers, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteDepartments() {
        for (RemoteDepartment remoteDepartment : remoteDepartments) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertDepartment( remoteDepartment, null );
        }
    }


    private class RemoteDepartmentsSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteDepartments.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteDepartments.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista działów" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteDepartments( remoteDepartments, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteSectors() {
        for (RemoteSector remoteSector : remoteSectors) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteSector( remoteSector, null );
        }
    }


    private class RemoteSectorsSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteSectors.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteSectors.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista sektorów" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteSectors( remoteSectors, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteUOProjects() {
        for (RemoteUOProject remoteUOProject : remoteUOProjects) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteUOProject( remoteUOProject, null );
        }
    }


    private class RemoteUOProjectsSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteUOProjects.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteUOProjects.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista projektów UO" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteUOProjects( remoteUOProjects, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteFamilies() {
        for (RemoteFamily remoteFamily : remoteFamilies) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteFamily( remoteFamily, null );
        }
    }

    private class RemoteFamiliesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteFamilies.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteFamilies.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista rodzin" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteFamilies( remoteFamilies, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteMarkets() {
        for (RemoteMarket remoteMarket : remoteMarkets) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteMarket( remoteMarket, null );
        }
    }

    private class RemoteMarketsSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteMarkets.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteMarkets.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista rynków" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteMarkets( remoteMarkets, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    // TODO XXX
    public void populateRemoteModules() {
        for (RemoteModule remoteModule : remoteModules) {
            AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteModule( remoteModule, null );
        }
    }

    private class RemoteModulesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            if (remoteModules.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long lastAddedId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (lastAddedId != null) {
                            runNextTaskLink();
                        }
                    }
                };
                progressPresentingManager.resetProgressPresenter( remoteModules.size(), DONT_HIDE_WHEN_FINISHED );
                progressPresentingManager.showMessagePresenter( "Lista modułów" ); // TODO hardcoded
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteModules( remoteModules, insertResult, progressPresentingManager.getProgressPresenterWrapper() );
            } else {
                runNextTaskLink();
            }
        }
    }

    public void populateRemoteAnalysis( int analysisNr ) {
        MutableLiveData<Long> analysisInsertResult = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long analysisId) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                analysisInsertResult.removeObserver(this); // this = observer...
                if (analysisId!=-1) {
                    prepareConverters();
                    // todo: jesli populateRemoteAnalysis() jest wołane z startRemoteDataInitialisationChain,
                    //  to prepareCOnverters było już wcześniej wołane. Czyli konwertery są dwa razy tworozne
                    populateRemoteAnalysisRows( analysisNr, analysisId.intValue() );
                    populateRemoteEanCodes( );
                }
            }
        };
        analysisInsertResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().insertRemoteAnalysis(
                remoteAnalyzes.get( analysisNr ),
                analysisInsertResult
        );
    }

    private class RemoteAnalysisSaver extends TaskLink {

        Integer analysisNr;

        public RemoteAnalysisSaver( int analysisNr ) {
            this.analysisNr = analysisNr;
        }

        @Override
        protected void doIt(Object... data) {
            if (remoteAnalyzes.size()>0) {
                MutableLiveData<Long> insertResult = new MutableLiveData<>();
                Observer<Long> insertingResultObserver = new Observer<Long>() {
                    @Override
                    public void onChanged(Long analysisId) {
                        insertResult.removeObserver(this); // this = observer...
                        if (analysisId!=-1) {
                            prepareConverters();
                            // todo: jesli populateRemoteAnalysis() jest wołane z startRemoteDataInitialisationChain,
                            //  to prepareCOnverters było już wcześniej wołane. Czyli konwertery są dwa razy tworozne
                            if (analysisId != null) {
                                runNextTaskLink( analysisNr, analysisId );
                            }
                        }
                    }
                };
                insertResult.observeForever(insertingResultObserver);
                RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
                remoteDataRepository.insertRemoteAnalysis( remoteAnalyzes.get( analysisNr ), insertResult );
            } else {
                // TODO Może Rollback transakcji?
                // runNextTaskLink( null ); // <-- CRASH!
            }
        }
    }

    private void populateRemoteAnalysisRows( int analysisNr, int analysisId ) {
        csv2AnalysisRowConverter.makeAnalysisRowList( analysisNr, analysisId );
        csv2AnalysisRowConverter.insertAllAnalysisRows();
    }

    private class RemoteAnalysisRowsSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            Integer analysisNr = (Integer) data[0];
            Long analysisId = (Long) data[1];
            MutableLiveData<Long> insertResult = new MutableLiveData<>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long lastAddedId) {
                    insertResult.removeObserver(this); // this = observer...
                    if (lastAddedId != null) {
                        runNextTaskLink();
                    }
                }
            };
            List<RemoteAnalysisRow> remoteAnalysisRowsList = csv2AnalysisRowConverter.makeAnalysisRowList(
                    analysisNr,
                    analysisId.intValue()
            );
            progressPresentingManager.resetProgressPresenter( remoteAnalysisRowsList.size(), DONT_HIDE_WHEN_FINISHED );
            progressPresentingManager.showMessagePresenter( "Lista artykułów strategicznych" ); // TODO hardcoded
            insertResult.observeForever( insertingResultObserver );
            csv2AnalysisRowConverter.insertAllAnalysisRows( insertResult, progressPresentingManager.getProgressPresenterWrapper() );
        }
    }

    private void populateRemoteEanCodes( ) {
        csv2EanCodeConverter.makeEanCodeList();
        csv2EanCodeConverter.insertAllEanCodes();
    }

    private class RemoteEanCodesSaver extends TaskLink {
        @Override
        protected void doIt(Object... data) {
            MutableLiveData<Long> insertResult = new MutableLiveData<>();
            Observer<Long> insertingResultObserver = new Observer<Long>() {
                @Override
                public void onChanged(Long lastAddedId) {
                    insertResult.removeObserver(this); // this = observer...
                    if (lastAddedId != null) {
                        runNextTaskLink();
                    }
                }
            };
            List<RemoteEanCode> remoteEanCodeList = csv2EanCodeConverter.makeEanCodeList();
            progressPresentingManager.resetProgressPresenter( remoteEanCodeList.size(), HIDE_WHEN_FINISHED );
            progressPresentingManager.showMessagePresenter( "Lista kodów kreskowych" ); // TODO hardcoded
            insertResult.observeForever( insertingResultObserver );
            csv2EanCodeConverter.insertAllEanCodes( insertResult, progressPresentingManager.getProgressPresenterWrapper() );
        }
    }

}
