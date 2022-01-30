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

    /* TODO XXX
    private AlertDialog getAskUserForAnalyzesDataDownloadDialog() {
        AlertDialog alertDialog =
                new MaterialAlertDialogBuilder(
                        getContext(),
                        R.style.PC_AlertDialogStyle_Overlay
                )
                        .setTitle( getString( R.string.basic_data_ready_to_download))
                        .setMessage( getString( R.string.question_about_downloading_data) )
                        .setPositiveButton(
                                getString( R.string.caption_yes) ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AnalysisBasicDataDownloader.getInstance().downloadAnalysisBasicData();
                                        analyzesRecyclerView.refresh();
                                    }
                                }
                        )
                        .setNegativeButton(
                                getString( R.string.caption_no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setCancelable( false )
                        .create();
        return alertDialog;
    }
    */

}
