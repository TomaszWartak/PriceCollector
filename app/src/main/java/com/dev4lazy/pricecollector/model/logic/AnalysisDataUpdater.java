package com.dev4lazy.pricecollector.model.logic;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.Remote2LocalConverter;
import com.dev4lazy.pricecollector.model.RemoteDataRepository;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteFamily;
import com.dev4lazy.pricecollector.remote_data.RemoteMarket;
import com.dev4lazy.pricecollector.remote_data.RemoteModule;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteUOProject;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.view.ProgressPresenter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.dev4lazy.pricecollector.view.ProgressPresenter.NO_PROGRESS_PRESENTER;

/**
 * AnalysisDataUpdater
 *
 * Służy do aktualizacji danych związanych z analizą konkurencji.
 * - po zalogowaniu sprawdza, czy na serwerze danych są jakieś dane do pobrania
 * - jeśli tak, sygnalizuje UI, że istnieje konieczność aktualizacji
 * - Jeśli użytkownik podejmie decyzję o aktualizacji, aktualizuje dane przez repozytorium
 */
public class AnalysisDataUpdater {

//----------------------------------------------------------------
// Obsługa singletona
    private static final String TAG = "AnalysisDataUpdater";

    private static AnalysisDataUpdater instance;

    private ArrayList<RemoteAnalysisRow> classScopeRemoteAnalysisRowsList;
    private HashMap<Integer, Article> classScopeArticleMap;
    private HashMap<String, Sector> classScopeSectorMap;
    private HashMap<String, Department> classScopeDepartmentMap;
    private Family classScopeFamily;
    private Module classScopeModule;
    private Market classScopeMarket;
    private UOProject classScopeUOProject;
    

    private AnalysisDataUpdater() {

    }

    public static AnalysisDataUpdater getInstance() {
        if (instance == null) {
            synchronized (AnalysisDataUpdater.class) {
                if (instance == null) {
                    instance = new AnalysisDataUpdater();
                }
            }
        }
        return instance;
    }
//----------------------------------------------------------------
// Obsługa danych podstawowoych analizy

    // true, jesli na serwerze jest nowa analiza do wykonania, czyli trzeba ją pobrać
    private boolean newAnalysisReadyToDownload = false;

    /*
    Sprawdza, czy na serwerze danych jest nowa analiza do wykonania.
    Efekt uboczny: Ustawia wartość newAnalysisReadyToDownload na true, jesli są.
     */
    public void checkNewAnalysisToDownload() {
        // todo
    }

    public boolean isNewAnalysisDataReadyToDownlad() {
        return newAnalysisDataReadyToDownlad;
    }

    // dane podstawowe analizy
    private Analysis analysisBasicData = null;

    /*
    Pobiera dane podstawowe analizy (daty itd)
    Efekt uboczny: Ustawia wartość analysisBasicData
     */
    public void downloadAnalysisBasicData() {
        createAnalysis();
    }

    /*
    Oddaje dane podstawowe analizy
     */
    public Analysis getAnalysisBasicData() {
        return analysisBasicData;
    }

    /*
    Zapisuje dane podstawowe analizy analysisBasicData w bazie lokalnej
     */
    public void saveAnalysisBasicData() {
        // todo
    }

    /*
    Odczytuje dane podstawowe analizy z bazy lokalnej
    Efekt uboczny: Ustawia wartość analysisBasicData
     */
    public void readAnalysisBasicData() {
        // todo
    }

    // zwraca true jeśli aktualna analiza jest już zakończona
    public boolean isAnalysisFinished() {
        return analysisBasicData.isFinished();
    }

    // zwraca true jeśli aktualna analiza nie jest zakończona
    public boolean isAnalysisNotFinished() {
        return !isAnalysisFinished();
    }

//----------------------------------------------------------------
// Obsługa danych szczegółowych analizy

    // todo - jak sprawdzić na serwerze danych, czy są dane do aktualizacji, bez pobierania tych danych?
    // Chyba tylko jeśli na serwerze będzie dana informująca o tym
    // W innym przypadku trzeba chyba pobrać dane...

    // true - jeśli na serwerze danych są dane do pobrania.
    private boolean newAnalysisDataReadyToDownlad = false;

    /*
    Sprawdza, czy na serwerze danych są dane do pobrania.
    Efekt uboczny: Ustawia wartość newAnalisisDataReadyToDownlad na true, jesli są.
     */
    public void checkNewAnalysisDataToDownload() {
        //todo !!
        // z preferncji pobranie daty ostatniej aktualizacji
        // sprawdzenie jaka jest data najnowszych danych na serwerze danych
        // Jeśli są nowsze dane - ustawienie wartości newAnalisisDataReadyToDownlad
        // Jeśli jest nowe badanie,to trzeba zawiesić info na ekranie głównym
        // Podobnie jeśli zostanie pobrana informacjae badanie jest zakończone
        // ustawienie newAnalysisDataReadyToDownlad
    }

    public boolean areNewAnalysisDataReadyToDownlad() {
        return newAnalysisDataReadyToDownlad;
    }

    /*
    Pobiera dane nowsze niż data ostatniej aktualizacji
    todo trzeba przepytać każdą tabelę i pobrać dane nowsze niż ostatnia aktualizacja
    todo Jeśli użytkownik modyfikował lokalne dane po aktualizacji, to trzeba rozwiązać konflikty
    todo Kolekcja wszystkich DAO/RDAO; Każde DAO/RDAO relizuje interfejs z metodą oddającą datę kiedy dana była aktualizowana
    todo Może lpiej polegać na zapyatniu SQL, które zwróci dane zmodyfikowane po ostatniej aktualizacji...
    todo Metoda musi być zabezpieczona przed sytaucją, że pobieramy aktualne dane, zapisujemy je do lokalnej bazy
    todo i zrywa połączenie. Dobrze byłoby jakoś kontrolować, co zostało zaktualizowane i odtworzyć mniej więcej od miejsca przerwania
    todo !!! Zbyt odbiegasz od realiów. W istniejącej bazie nie ma mechnizmu pamięatnia daty aktualizacji każdej danej
     */
    public void downloadAnalysisNewData() {

    }

    /*
    Pobiera wszsykie dane (refresh - reload), bez względu na to czy były już pobrane, czy nie
     */
    public void downloadAnalysisAllData() {

    }

    /*
    Przepisuje dane lokalne na serwer zdalny
    todo E... To chyba on-line powinno być robione?
    todo Może nie on-line, bo trzeba się zastanowić, jak ma być nowa cena
    todo ! powinien podpowiadać nową cenę i sygnalizować odchylenie od ceny konurenta - lista wadliwych cen...
     */
    public void uploadAnalysisAllData() {

    }

    public void copySectorsAndDepartmentsFromRemoteDatabase( MutableLiveData<Long> finalResult ) {
        MutableLiveData<List<RemoteSector>> getAllRemoteSectorsResult = new MutableLiveData<>();
        Observer<List<RemoteSector>> insertingResultObserver = new Observer<List<RemoteSector>>() {
            @Override
            public void onChanged( List<RemoteSector> remoteSectors ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllRemoteSectorsResult.removeObserver(this); // this = observer...
                if (!remoteSectors.isEmpty()) {
                    Remote2LocalConverter remote2LocalConverter = new Remote2LocalConverter();
                    Sector sector;
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    for (RemoteSector remoteSector : remoteSectors) {
                        sector = remote2LocalConverter.createSector( remoteSector );
                        localDataRepository.insertSector( sector, null );
                    }
                    copyDepartmentsFromRemoteDatabase( finalResult );
                }
            }
        };
        getAllRemoteSectorsResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllSectors(getAllRemoteSectorsResult);
    }

    private void copyDepartmentsFromRemoteDatabase( MutableLiveData<Long> finalResult ) {
        MutableLiveData<List<RemoteDepartment>> getAllRemoteDepartmentsResult = new MutableLiveData<>();
        Observer<List<RemoteDepartment>> insertingResultObserver = new Observer<List<RemoteDepartment>>() {
            @Override
            public void onChanged( List<RemoteDepartment> remoteDepartments ) {
                // todo zobacz post o dwukrotnym uruchamianiu onChanged() (przy utworzeniu i zmianie obserwowwanej wartości)
                // todo oraz https://stackoverflow.com/questions/57540207/room-db-insert-callback
                getAllRemoteDepartmentsResult.removeObserver(this); // this = observer...
                if (!remoteDepartments.isEmpty()) {
                    Remote2LocalConverter remote2LocalConverter = new Remote2LocalConverter();
                    Department department;
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    RemoteDepartment lastRemoteDepartment = remoteDepartments.get( remoteDepartments.size()-1 );
                    for (RemoteDepartment remoteDepartment : remoteDepartments) {
                        department = remote2LocalConverter.createDepartment( remoteDepartment );
                        if (remoteDepartment==lastRemoteDepartment ) {
                            localDataRepository.insertDepartment( department, finalResult );
                        } else {
                            localDataRepository.insertDepartment( department, null );
                        }
                    }
                }
            }
        };
        getAllRemoteDepartmentsResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllDepartments( getAllRemoteDepartmentsResult );
    }

    public void copyDummyFamiliesEtcFromRemoteDatabase( MutableLiveData<Long> finalResult ) {
        getRemoteFamily( finalResult );
    }

    private void getRemoteFamily( MutableLiveData<Long> finalResult ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteFamily>> result = new MutableLiveData<>();
        Observer<List<RemoteFamily>> resultObserver = new Observer<List<RemoteFamily>>() {
            @Override
            public void onChanged(List<RemoteFamily> familiesList ) {
                if ((familiesList != null)&&(!familiesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    Family family = converter.createFamily( familiesList.get(0) );
                    insertDummyFamily( family, finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteFamilies( result );
    }

    private void insertDummyFamily( Family family, MutableLiveData<Long> finalResult ) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long familyId ) {
                if ((familyId != null)&&(familyId>0)) {
                    result.removeObserver(this); // this = observer...
                    getRemoteMarket( finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertFamily( family, result );
    }

    private void getRemoteMarket( MutableLiveData<Long> finalResult ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteMarket>> result = new MutableLiveData<>();
        Observer<List<RemoteMarket>> resultObserver = new Observer<List<RemoteMarket>>() {
            @Override
            public void onChanged(List<RemoteMarket> marketsList) {
                if ((marketsList!= null)&&(!marketsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    Market market = converter.createMarket( marketsList.get(0) );
                    insertDummyMarket( market, finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteMarkets(result);
    }

    private void insertDummyMarket( Market market, MutableLiveData<Long> finalResult ) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long marketId ) {
                if ((marketId != null)&&(marketId>0)) {
                    result.removeObserver(this); // this = observer...
                    getRemoteModule( finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertMarket( market, result );
    }

    private void getRemoteModule( MutableLiveData<Long> finalResult ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteModule>> result = new MutableLiveData<>();
        Observer<List<RemoteModule>> resultObserver = new Observer<List<RemoteModule>>() {
            @Override
            public void onChanged(List<RemoteModule> modulesList) {
                if ((modulesList != null)&&(!modulesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    Module module = converter.createModule( modulesList.get(0) );
                    insertDummyModule( module, finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteModules(result);
    }

    private void insertDummyModule( Module module, MutableLiveData<Long> finalResult ) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged( Long marketId ) {
                if ((marketId != null)&&(marketId>0)) {
                    result.removeObserver(this); // this = observer...
                    getRemoteUOProject( finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertModule( module, result );
    }
    private void getRemoteUOProject( MutableLiveData<Long> finalResult ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        MutableLiveData<List<RemoteUOProject>> result = new MutableLiveData<>();
        Observer<List<RemoteUOProject>> resultObserver = new Observer<List<RemoteUOProject>>() {
            @Override
            public void onChanged(List<RemoteUOProject> uoProjectList) {
                if ((uoProjectList != null)&&(!uoProjectList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    UOProject uoProject = converter.createUOProject( uoProjectList.get(0) );
                    insertDummyUOProject( uoProject, finalResult );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteUOProjects(result);
    }

    private void insertDummyUOProject( UOProject uoProject, MutableLiveData<Long> finalResult ) {
        Observer<Long> resultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long marketId) {
                if ((marketId != null) && (marketId > 0)) {
                    finalResult.removeObserver(this); // this = observer...
                }
            }
        };
        finalResult.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().insertUOProject( uoProject, finalResult);
    }

    /**
     * Dopisuje artykuły do lokalnej bazy danych.
     * Tworzy listę wierszy analizy z bazy zdalnej classScopeRemoteAnalysisRowsList, widoczną na pozimie klasy.
     * 1. Pobranie wierszy analizy z bazy zdalnej.
     * 2. Utworzenie listy wierszy analizy z bazy zdalnej.
     * 3. Utworzenie listy artykułów, na podstawie listy wierszy analizy.
     * 4. Dopisanie artykułów do bazy lokalnej.
     * 5. Wywołąnie createArticlesMap().
     * @param progressPresenter
     */
    public void insertArticles(ProgressPresenter progressPresenter ) {
        MutableLiveData<List<RemoteAnalysisRow>> result = new MutableLiveData<>();
        Observer<List<RemoteAnalysisRow>> resultObserver = new Observer<List<RemoteAnalysisRow>>() {
            @Override
            public void onChanged(List<RemoteAnalysisRow> remoteAnalysisRowsList) {
                if ((remoteAnalysisRowsList != null)&&(!remoteAnalysisRowsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...

                    Remote2LocalConverter converter = new Remote2LocalConverter();
                    classScopeRemoteAnalysisRowsList = (ArrayList)remoteAnalysisRowsList;
                    ArrayList<Article> articlesList = converter.createArticlesList( classScopeRemoteAnalysisRowsList );
                    // TODO nie wiem czy ProgressPresenter działa...
                    // albo dodanie artykułw idzie za szybko, albo coś nie jest tak
                    // Zró jakiś delay przy dodaawaniu
                    // todo?? ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    // todo?? ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, articlesList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.insertArticles( articlesList, progressPresenter );
                    createArticlesMap( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllAnalysisRows(result);
    }

    /**
     * Tworzy mapę artykułów Article classScopeArticleMap (klucz: remote_id, wartość: Article) widocznej na poziomie klasy.
     * 1. Pobranie wszystkich artykułów z bazy lokalnej.
     * 2. Utworzenie mapy artykułów.
     * 3. Wywołanie insertEanCodes() w celu dopisania kodów kreskowych.
     * @param progressPresenter
     */
    private void createArticlesMap( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Article>> result = new MutableLiveData<>();
        Observer<List<Article>> resultObserver = new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                if ((articleList != null)&&(!articleList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeArticleMap = new HashMap<>();
                    for (Article article : articleList ) {
                        classScopeArticleMap.put( article.getRemote_id(), article );
                    }
                    insertEanCodes( articleList, progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
    }

    /**
     * Dopisuje do bazy lokalnej listę kodów kreskowych dla listy artykułów articleList.
     * 1. Pobtanie wszysykich kodów kreskowych z bazy zdalnej.
     * 2. Utworzenie listy kodów kreskowych.
     * 3. Dodanie kodów kreskowych do bazy lokalnej.
     * 4. Wywołanie createSectorsMap() w celu utworzenia mapy sektorów
     * @param articleList
     * @param progressPresenter
     */
    private void insertEanCodes(List<Article> articleList, ProgressPresenter progressPresenter ) {
        MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
        Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
            @Override
            public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    ArrayList<EanCode> eanCodeList = convertToEanCodes( remoteEanCodesList, articleList, progressPresenter );
                    // todo? ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    // todo? ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, eanCodeList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    if (progressPresenter!=null) {
                        progressPresenter.reset(eanCodeList.size());
                    }
                    localDataRepository.insertEanCodes( eanCodeList, progressPresenter );
                    createSectorsMap( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllEanCodes(result);

    }

    private ArrayList<EanCode> convertToEanCodes(
            List<RemoteEanCode> remoteEanCodesList,
            List<Article> articleList,
            ProgressPresenter progressPresenter
    ) {
        //HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap =
        // remoteEanCodesList.stream().collect( Collectors.toMap(RemoteEanCode::getArticleId, Function.identity() ));
        //remoteEanCodesList.stream().collect( Collectors.toMap( RemoteEanCode::getArticleId, b->b ));

        HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap = new HashMap<>();
        for (RemoteEanCode remoteEanCode : remoteEanCodesList) {
            remoteEanCodesHashMap.put( remoteEanCode.getArticleId(), remoteEanCode );
        }
        HashMap<Integer, Article> articlesHashMap = new HashMap<>();
        for (Article article : articleList ) {
            articlesHashMap.put( article.getRemote_id(), article );
        }
        Remote2LocalConverter converter = new Remote2LocalConverter();
        return converter.createEanCodesList( remoteEanCodesHashMap, articlesHashMap );
    }

    private void createSectorsMap( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Sector>> result = new MutableLiveData<>();
        Observer<List<Sector>> resultObserver = new Observer<List<Sector>>() {
            @Override
            public void onChanged(List<Sector> sectorList) {
                if ((sectorList != null)&&(!sectorList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeSectorMap = new HashMap<>();
                    for (Sector sector : sectorList ) {
                        classScopeSectorMap.put( sector.getName(), sector );
                    }
                    createDepartmentsMap( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors(result);
    }

    private void createDepartmentsMap( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Department>> result = new MutableLiveData<>();
        Observer<List<Department>> resultObserver = new Observer<List<Department>>() {
            @Override
            public void onChanged(List<Department> departmentList) {
                if ((departmentList != null)&&(!departmentList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeDepartmentMap = new HashMap<>();
                    for (Department department : departmentList ) {
                        classScopeDepartmentMap.put( department.getSymbol(), department );
                    }
                    createDummyFamily( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
    }

    private void createDummyFamily( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Family>> result = new MutableLiveData<>();
        Observer<List<Family>> resultObserver = new Observer<List<Family>>() {
            @Override
            public void onChanged( List<Family> familiesList) {
                if ((familiesList != null)&&(!familiesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeFamily = familiesList.get(0);
                    createDummyMarket( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllFamilies( result );
    }

    private void createDummyMarket( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Market>> result = new MutableLiveData<>();
        Observer<List<Market>> resultObserver = new Observer<List<Market>>() {
            @Override
            public void onChanged( List<Market> marketsList) {
                if ((marketsList != null)&&(!marketsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeMarket = marketsList.get(0);
                    createDummyModule( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllMarkets( result );
    }

    private void createDummyModule( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<Module>> result = new MutableLiveData<>();
        Observer<List<Module>> resultObserver = new Observer<List<Module>>() {
            @Override
            public void onChanged( List<Module> modulesList ) {
                if ((modulesList != null)&&(!modulesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeModule = modulesList.get(0);
                    createDummyUOProject( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllModules( result );
    }

    private void createDummyUOProject( ProgressPresenter progressPresenter ) {
        MutableLiveData<List<UOProject>> result = new MutableLiveData<>();
        Observer<List<UOProject>> resultObserver = new Observer<List<UOProject>>() {
            @Override
            public void onChanged(List<UOProject> uoProjectsList) {
                if ((uoProjectsList != null)&&(!uoProjectsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeUOProject = uoProjectsList.get(0);
                    insertOwnArticlesInfos( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllUOProjects( result );
    }

    /**
     * Dodaje do bazy lokalnej opisy artykułów własnych
     * @param progressPresenter
     */
    private void insertOwnArticlesInfos(ProgressPresenter progressPresenter ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        OwnArticleInfo ownArticleInfo;
        ArrayList<OwnArticleInfo> ownArticleInfoList = new ArrayList<>();
        for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
            ownArticleInfo = converter.createOwnArticleInfo(
                    remoteAnalysisRow,
                    classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() ),
                    classScopeDepartmentMap.get( remoteAnalysisRow.getDepartment() ),
                    classScopeSectorMap.get( remoteAnalysisRow.getSector() ),
                    classScopeFamily,
                    classScopeModule,
                    classScopeMarket,
                    classScopeUOProject
            );
            ownArticleInfoList.add(ownArticleInfo);
        }
        // ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
        // ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, ownArticleInfoList.size() );
        if (progressPresenter!=null) {
            progressPresenter.reset(ownArticleInfoList.size());
        }
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertOwnArticleInfos( ownArticleInfoList, progressPresenter );
    }

    public void createAnalysis() {
        // TODO !!! Date lastCheckAnalysisDate = ???
        MutableLiveData<List<RemoteAnalysis>> result = new MutableLiveData<>();
        Observer<List<RemoteAnalysis>> resultObserver = new Observer<List<RemoteAnalysis>>() {
            @Override
            public void onChanged(List<RemoteAnalysis> remoteAnalysisList) {
                if ((remoteAnalysisList != null)&&(!remoteAnalysisList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    Remote2LocalConverter converter = new Remote2LocalConverter();
                    ArrayList<Analysis> newAnalyzes = new ArrayList<>();
                    for (RemoteAnalysis remoteAnalysis : remoteAnalysisList ) {
                        // TODO jeśli jest jakaś analiza nowa, ale zakończona to jej nie ruszamy
                        Analysis analysis = converter.createAnalysis( remoteAnalysis );
                        if (remoteAnalysis.isNotFinished()) {
                            newAnalyzes.add(analysis);
                        }
                    }
                    insertNewAnalyzes( newAnalyzes );
                }
            }
        };
        result.observeForever( resultObserver );
        RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
        Date lastCheckAnalysisDate = null;
        try {
            lastCheckAnalysisDate = new DateConverter().string2Date( "2019-01-01" ); // TODO !!!! data
        } catch ( ParseException ex ) {
            ex.printStackTrace();
            return;
        }
        remoteDataRepository.findAnalysisNewerThen( lastCheckAnalysisDate, result );
    }

    private void insertNewAnalyzes(ArrayList<Analysis> newAnalyzes ) {
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertAnalyzes( newAnalyzes, NO_PROGRESS_PRESENTER );
    }

        /*
        // TODO !!!!
        i dla każdej AnalysisiArticles...

    i tworzenie wiersza analizy wreszcie...

         */


}
