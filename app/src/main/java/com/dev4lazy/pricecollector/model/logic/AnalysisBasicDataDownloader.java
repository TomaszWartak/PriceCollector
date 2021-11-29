package com.dev4lazy.pricecollector.model.logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.Family;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.Module;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.entities.UOProject;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteEanCode;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppSettings;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DONT_HIDE_WHEN_FINISHED;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.NO_PROGRESS_PRESENTER;

/**
 * AnalysisBasicDataDownloader
 *
 * Służy do aktualizacji danych związanych z analizą konkurencji.
 * - po zalogowaniu sprawdza, czy na serwerze zdalnym są jakieś dane podstawowe ("nagłówek") do pobrania
 * - jeśli tak, sygnalizuje UI, że istnieje konieczność aktualizacji
 * - Jeśli użytkownik podejmie decyzję o aktualizacji, aktualizuje dane przez repozytorium
 */
public class AnalysisBasicDataDownloader {

//----------------------------------------------------------------
// Obsługa singletona
    private static final String TAG = "AnalysisBasicDataDownloader";

    private static AnalysisBasicDataDownloader instance;

    private ArrayList<RemoteAnalysisRow> classScopeRemoteAnalysisRowsList;
    private HashMap<Integer, Article> classScopeArticleMap; // Article.getRemote_id()
    private HashMap<String, Sector> classScopeSectorMap; // Sector.getName()
    private HashMap<String, Department> classScopeDepartmentMap; // Department.getSymbol()
    private Family classScopeDummyFamily;
    private Module classScopeDummyModule;
    private Market classScopeDummyMarket;
    private UOProject classScopeDummyUOProject;
    private HashMap<Integer, OwnArticleInfo> classScopeOwnArticleInfoMap; // OwnArticleInfo.getArticleId()


    private AnalysisBasicDataDownloader() {
        newAnalysisReadyToDownload.setValue(false);
    }

    public static AnalysisBasicDataDownloader getInstance() {
        if (instance == null) {
            synchronized (AnalysisBasicDataDownloader.class) {
                if (instance == null) {
                    instance = new AnalysisBasicDataDownloader();
                }
            }
        }
        return instance;
    }
//----------------------------------------------------------------
// Obsługa danych podstawowoych analizy

    // true, jeśli zapytano serwer o to, czy są nowe analizy i była odpowiedź
    private boolean serverRepliedThereAreNewAnalyzes = false;

    public boolean isServerRepliedThereAreNewAnalyzes() {
        return serverRepliedThereAreNewAnalyzes;
    }

    // true, jesli na serwerze jest nowa analiza do wykonania, czyli trzeba ją pobrać
    private MutableLiveData<Boolean> newAnalysisReadyToDownload = new MutableLiveData<>();

    /**
     * Sprawdza, czy na serwerze danych jest nowa analiza do wykonania.
     * Efekty uboczne:
     *      Ustawia wartość serverRepliedThereAreNewAnalyzes na true, jeśli jest odpowiedź z serwera
     *      Ustawia wartość newAnalysisReadyToDownload na true, jesli są.
     */
    public void checkNewAnalysisToDownload( MutableLiveData<Boolean> result ) {
        serverRepliedThereAreNewAnalyzes = false;
        newAnalysisReadyToDownload.setValue(false);
        MutableLiveData<Integer> countAnalyzesNewerThenResult = new MutableLiveData<>();
        Observer<Integer> resultObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer remoteAnalyzesCount) {
                serverRepliedThereAreNewAnalyzes = true;
                result.postValue( true );
                newAnalysisReadyToDownload.setValue((remoteAnalyzesCount != null) && (remoteAnalyzesCount.intValue()>0));
                countAnalyzesNewerThenResult.removeObserver(this); // this = observer...
            }
        };
        countAnalyzesNewerThenResult.observeForever( resultObserver );
        RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
        Date lastCheckAnalysisDate = AppSettings.getInstance().getLastAnalysisCreationDate();
        remoteDataRepository.countRemoteAnalyzesNewerThen( lastCheckAnalysisDate, countAnalyzesNewerThenResult );
    }

    public boolean isNewAnalysisReadyToDownlad() {
        return newAnalysisReadyToDownload.getValue();
    }

    public LiveData<Boolean> getNewAnalysisReadyToDownladLD() {
        return newAnalysisReadyToDownload;
    }

    // Pobiera dane podstawowe analizy (daty itd)
    // Efekt uboczny: Ustawia wartość analysisBasicData
    public void downloadAnalysisBasicData() {
        getNewAnalysis();
    }

    /* TODO XXX dane podstawowe analizy
    private final Analysis analysisBasicData = null;

    // Oddaje dane podstawowe analizy
    public Analysis getAnalysisBasicData() {
        return analysisBasicData;
    }

    // todo ??? Zapisuje dane podstawowe analizy analysisBasicData w bazie lokalnej
    public void saveAnalysisBasicData() {
        // todo
    }

    // todo ??? Odczytuje dane podstawowe analizy z bazy lokalnej
    // Efekt uboczny: Ustawia wartość analysisBasicData
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
     */

    public void getNewAnalysis() {
        MutableLiveData<List<RemoteAnalysis>> result = new MutableLiveData<>();
        Observer<List<RemoteAnalysis>> resultObserver = new Observer<List<RemoteAnalysis>>() {
            @Override
            public void onChanged(List<RemoteAnalysis> remoteAnalysisList) {
                if ((remoteAnalysisList != null)&&(!remoteAnalysisList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    Remote2LocalConverter converter = new Remote2LocalConverter();
                    ArrayList<Analysis> newAnalyzes = new ArrayList<>();
                    Date dateTheLastRemoteAnalysisWasCreated = new Date( 0 );
                    for (RemoteAnalysis remoteAnalysis : remoteAnalysisList ) {
                        // TODO jeśli jest jakaś analiza nowa, ale zakończona to jej nie ruszamy
                        Analysis analysis = converter.createAnalysis( remoteAnalysis );
                        if (remoteAnalysis.isNotFinished()) {
                            newAnalyzes.add(analysis);
                        }
                        if (analysis.getCreationDate().compareTo(dateTheLastRemoteAnalysisWasCreated)>0) {
                            dateTheLastRemoteAnalysisWasCreated = analysis.getCreationDate();
                        }
                    }
                    AppSettings.getInstance().setLastAnalysisCreationDate( dateTheLastRemoteAnalysisWasCreated );
                    newAnalysisReadyToDownload.setValue(false);
                    insertNewAnalyzes( newAnalyzes );
                }
            }
        };
        result.observeForever( resultObserver );
        RemoteDataRepository remoteDataRepository = AppHandle.getHandle().getRepository().getRemoteDataRepository();
        Date dateTheLastAnalysisWasCreated = AppSettings.getInstance().getLastAnalysisCreationDate();
        remoteDataRepository.findRemoteAnalyzesNewerThen( dateTheLastAnalysisWasCreated, result );
    }

    private void insertNewAnalyzes( ArrayList<Analysis> newAnalyzes ) {
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertAnalyzes( newAnalyzes, NO_PROGRESS_PRESENTER );
    }

   // TODO XXX TO wszystko poniżej zostało zastapione przez TaskChain w AnalysisFullDataDownloader
    /**
     * Dopisuje artykuły do lokalnej bazy danych.
     * Tworzy listę wierszy analizy z bazy zdalnej classScopeRemoteAnalysisRowsList, widoczną na pozimie klasy.
     * 1. Pobranie wierszy analizy z bazy zdalnej.
     * 2. Utworzenie listy wierszy analizy z bazy zdalnej.
     * 3. Utworzenie listy artykułów, na podstawie listy wierszy analizy.
     * 4. Dopisanie artykułów do bazy lokalnej.
     * 5. Wywołąnie createArticlesMap().
     * @param analysis
     * @param progressPresenter
     */
    public void insertArticles( Analysis analysis, MutableLiveData<Boolean> finalResult, ProgressPresenter progressPresenter) {
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
                    if (progressPresenter !=null) {
                        progressPresenter.reset( articlesList.size(), DONT_HIDE_WHEN_FINISHED );
                    }
                    localDataRepository.insertArticles( articlesList, progressPresenter);
                    createArticlesMap( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        String remoteAnalysisRowQueryString = getQuery( analysis.getRemote_id() );
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getRemoteAnalysisRowViaQuery( remoteAnalysisRowQueryString, result );
    }

    private String getQuery( int remoteAnalysisId ) {
        SelectQuery analysisArticlesJoinWithPricesQuery = new SelectQuery()
                .addAllColumns( )
                .addCustomFromTable("analysis_rows")
                .addCondition( BinaryCondition.equalTo( new CustomSql( "analysis_rows.analysisId" ), remoteAnalysisId ) );
        analysisArticlesJoinWithPricesQuery = analysisArticlesJoinWithPricesQuery.validate();
        return analysisArticlesJoinWithPricesQuery.toString();
    }

    /**
     * Tworzy mapę artykułów Article classScopeArticleMap (klucz: remote_id, wartość: Article) widocznej na poziomie klasy.
     * 1. Pobranie wszystkich artykułów z bazy lokalnej.
     * 2. Utworzenie mapy artykułów.
     * 3. Wywołanie insertEanCodes() w celu dopisania kodów kreskowych.
     * @param progressPresenter
     */
    private void createArticlesMap( Analysis analysis, MutableLiveData<Boolean> finalResult, ProgressPresenter progressPresenter) {
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
                    insertEanCodes( analysis, finalResult, articleList, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
    }

    /**
     * Dopisuje do bazy lokalnej listę kodów kreskowych dla listy artykułów articleList.
     * 1. Pobranie wszysykich kodów kreskowych z bazy zdalnej.
     * 2. Utworzenie listy kodów kreskowych.
     * 3. Dodanie kodów kreskowych do bazy lokalnej.
     * 4. Wywołanie createSectorsMap() w celu utworzenia mapy sektorów
     * @param articleList
     * @param progressPresenter
     */
    private void insertEanCodes(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            List<Article> articleList,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
        Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
            @Override
            public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    ArrayList<EanCode> eanCodeList = convertToEanCodes( remoteEanCodesList, articleList );
                    // todo? ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    // todo? ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, eanCodeList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    if (progressPresenter !=null) {
                        progressPresenter.reset( eanCodeList.size(), DONT_HIDE_WHEN_FINISHED );
                    }
                    localDataRepository.insertEanCodes( eanCodeList, progressPresenter);
                    createSectorsMap( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllRemoteEanCodes(result);

    }

    private ArrayList<EanCode> convertToEanCodes(
            List<RemoteEanCode> remoteEanCodesList,
            List<Article> articleList ) {
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

    private void createSectorsMap(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
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
                    createDepartmentsMap( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors(result);
    }

    private void createDepartmentsMap(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
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
                    createDummyFamily( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
    }

    private void createDummyFamily(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<Family>> result = new MutableLiveData<>();
        Observer<List<Family>> resultObserver = new Observer<List<Family>>() {
            @Override
            public void onChanged( List<Family> familiesList) {
                if ((familiesList != null)&&(!familiesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeDummyFamily = familiesList.get(0);
                    createDummyMarket( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllFamilies( result );
    }

    private void createDummyMarket(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<Market>> result = new MutableLiveData<>();
        Observer<List<Market>> resultObserver = new Observer<List<Market>>() {
            @Override
            public void onChanged( List<Market> marketsList) {
                if ((marketsList != null)&&(!marketsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeDummyMarket = marketsList.get(0);
                    createDummyModule( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllMarkets( result );
    }

    private void createDummyModule(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<Module>> result = new MutableLiveData<>();
        Observer<List<Module>> resultObserver = new Observer<List<Module>>() {
            @Override
            public void onChanged( List<Module> modulesList ) {
                if ((modulesList != null)&&(!modulesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeDummyModule = modulesList.get(0);
                    createDummyUOProject( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllModules( result );
    }

    private void createDummyUOProject(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<UOProject>> result = new MutableLiveData<>();
        Observer<List<UOProject>> resultObserver = new Observer<List<UOProject>>() {
            @Override
            public void onChanged(List<UOProject> uoProjectsList) {
                if ((uoProjectsList != null)&&(!uoProjectsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeDummyUOProject = uoProjectsList.get(0);
                    insertOwnArticlesInfos( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllUOProjects( result );
    }

    /**
     * Dodaje do bazy lokalnej opisy artykułów własnych
     * @param analysis
     * @param progressPresenter
     */
    private void insertOwnArticlesInfos(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        OwnArticleInfo ownArticleInfo;
        ArrayList<OwnArticleInfo> ownArticleInfoList = new ArrayList<>();
        for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
            ownArticleInfo = converter.createOwnArticleInfo(
                    remoteAnalysisRow,
                    classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() ),
                    classScopeDepartmentMap.get( remoteAnalysisRow.getDepartment() ),
                    classScopeSectorMap.get( remoteAnalysisRow.getSector() ),
                    classScopeDummyFamily,
                    classScopeDummyModule,
                    classScopeDummyMarket,
                    classScopeDummyUOProject
            );
            ownArticleInfoList.add( ownArticleInfo );
        }

        MutableLiveData<Long> insertResult = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long lastAddedOwnArticleInfoId) {
                    insertResult.removeObserver(this); // this = observer...
                    if (lastAddedOwnArticleInfoId!=null) {
                        createOwnArticleInfosMap( analysis, finalResult, progressPresenter);
                    }
            }
        };
        if (progressPresenter !=null) {
            progressPresenter.reset( ownArticleInfoList.size(), DONT_HIDE_WHEN_FINISHED );
        }
        insertResult.observeForever(insertingResultObserver);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertOwnArticleInfos( ownArticleInfoList, progressPresenter, insertResult );
    }

    private void createOwnArticleInfosMap(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        MutableLiveData<List<OwnArticleInfo>> result = new MutableLiveData<>();
        Observer<List<OwnArticleInfo>> resultObserver = new Observer<List<OwnArticleInfo>>() {
            @Override
            public void onChanged(List<OwnArticleInfo> ownArticleInfosList) {
                if ((ownArticleInfosList != null)&&(!ownArticleInfosList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    classScopeOwnArticleInfoMap = new HashMap<>();
                    for (OwnArticleInfo ownArticleInfo : ownArticleInfosList ) {
                        classScopeOwnArticleInfoMap.put( ownArticleInfo.getArticleId(), ownArticleInfo );
                    }
                    insertAnalysisArticles( analysis, finalResult, progressPresenter);
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllOwnArticleInfos(result);
    }

    private void insertAnalysisArticles(
            Analysis analysis,
            MutableLiveData<Boolean> finalResult,
            ProgressPresenter progressPresenter) {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        AnalysisArticle analysisArticle;
        ArrayList<AnalysisArticle> analysisArticlesList = new ArrayList<>();
        Article article;
        OwnArticleInfo ownArticleInfo;
        for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
            article = classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() );
            ownArticleInfo = classScopeOwnArticleInfoMap.get( article.getId() );
            analysisArticle = converter.createAnalysisArticle(
                    remoteAnalysisRow,
                    analysis,
                    ownArticleInfo
            );
            analysisArticlesList.add( analysisArticle );
        }

        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        MutableLiveData<Long> insertResult = new MutableLiveData<>();
        Observer<Long> insertingResultObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long lastInsertedId) {
                insertResult.removeObserver( this ); // this = observer...
                analysis.setDataDownloaded( true );
                localDataRepository.updateAnalysis( analysis, null );
                finalResult.postValue( true );
            }
        };
        if (progressPresenter !=null) {
            progressPresenter.reset( analysisArticlesList.size(), DONT_HIDE_WHEN_FINISHED );
        }
        insertResult.observeForever( insertingResultObserver );
        localDataRepository.insertAnalysisArticles( analysisArticlesList, progressPresenter, insertResult );
    }

}
