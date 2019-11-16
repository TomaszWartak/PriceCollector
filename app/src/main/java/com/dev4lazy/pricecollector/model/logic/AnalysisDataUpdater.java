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
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.view.ProgressPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        // todo
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

    // todo private?
    public void copySectorsAndDepartmentsFromRemoteDatabase() {
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
                    copyDepartmentsFromRemoteDatabase();
                }
            }
        };
        getAllRemoteSectorsResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllSectors(getAllRemoteSectorsResult);
    }

    private void copyDepartmentsFromRemoteDatabase() {
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
                    for (RemoteDepartment remoteDepartment : remoteDepartments) {
                        department = remote2LocalConverter.createDepartment( remoteDepartment );
                        localDataRepository.insertDepartment( department, null );
                    }
                }
            }
        };
        getAllRemoteDepartmentsResult.observeForever( insertingResultObserver );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllDepartments( getAllRemoteDepartmentsResult );
    }

    public void createArticles( ProgressPresenter progressPresenter ) {
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

    // todo test
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
                    createEanCodes( articleList, progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
    }

    // todo test
    private void createEanCodes(List<Article> articleList, ProgressPresenter progressPresenter ) {
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
        /*
        ArrayList<EanCode> createEanCodesList(
                HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap,
                HashMap<Integer, Article> articlesHashMap )

         */

        /*
        ArrayList<EanCode> eanCodeList = new ArrayList<>();
        Remote2LocalConverter converter = new Remote2LocalConverter();
        for ( RemoteEanCode remoteEanCode : remoteEanCodesList ) {
            // todo null się wywali... Powinien być Article
            // todo potrzeban list wszystkich artykułów, żeby z niej pobierać
            EanCode eanCode = converter.createEanCode(remoteEanCode, null);
            eanCodeList.add( eanCode );
        }
        return eanCodeList;

         */
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
                    createOwnArticlesInfos( progressPresenter );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
    }

    private void createOwnArticlesInfos( ProgressPresenter progressPresenter ) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        OwnArticleInfo ownArticleInfo;
        ArrayList<OwnArticleInfo> ownArticleInfoList = new ArrayList<>();
        for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
            ownArticleInfo = converter.createOwnArticleInfo(
                    remoteAnalysisRow,
                    classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() ),
                    classScopeDepartmentMap.get( remoteAnalysisRow.getDepartment() ),
                    classScopeSectorMap.get( remoteAnalysisRow.getSector() )
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


    private void createAnalysis() {
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
                    addNewAnalyzes( newAnalyzes );
                }
            }
        };
        result.observeForever( resultObserver );
        RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
        // TODO !!! remoteDataRepository.findAnalysisNewerThen( lastCheckAnalysisDate, result );
    }

    private void addNewAnalyzes( ArrayList<Analysis> newAnalyzes ) {
        // TODO !!!!
        /*
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertAnalyzes( newAnalyzes, progressBarPresenter );
        i dla każdej AnalysisiArticles...
         */
    }

        /*
        // TODO !!!!

    i tworzenie wiersza analizy wreszcie...

         */


}
