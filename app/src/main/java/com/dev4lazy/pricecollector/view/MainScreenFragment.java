package com.dev4lazy.pricecollector.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.LocalDataRepository;
import com.dev4lazy.pricecollector.model.Remote2LocalConverter;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.EanCode;
import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteEanCode;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

public class MainScreenFragment extends Fragment {

    private MainViewModel mViewModel;

    private AnalysisDataUpdater analysisDataUpdater = getInstance();

    private View viewAnalysisiItem;


    // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
    // private RemoteDatabaseInitializer converter;

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo tutaj zainicjoawanie wykorzystywanych później obiektów tej klasy
    }

    private ArrayList<RemoteAnalysisRow> classScopeRemoteAnalysisRowsList;
    private HashMap<Integer, Article> classScopeArticleMap;
    private HashMap<String, Sector> classScopeSectorMap;
    private HashMap<String, Department> classScopeDepartmentMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        setOnBackPressedCalback();

        setAnalysisItem( inflater, view );

        ((TextView)view.findViewById(R.id.user_login)).setText(AppHandle.getHandle().getSettings().getUser().getLogin());

        // todo test
        view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_testActionsFragment);
        });

        // todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
        getPreferencesInfo(); // todo ????

        // todo jeśli analiza jest w trakcie - możliwość kontynuacji
        // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - wyświetlenie
        getNewAnalysisInfo();

        // todo incializacja menu, czy szuflady?
        return view;
    }

    private void setAnalysisItem(@NonNull LayoutInflater inflater, View view) {
        viewAnalysisiItem = inflater.inflate(R.layout.test_analysis_item,null);
        ((ViewGroup) view).addView(viewAnalysisiItem);
        view.findViewById(R.id.card_view_constraint_layout).setOnClickListener((View v) -> {
            // todo test
            testCreateAnalysisArticles();
            // todo test
            // testCreateEanCodes();
            // openTestAnalyzesList();
        });
    }

    private void testCreateAnalysisArticles() {
        MutableLiveData<List<RemoteAnalysisRow>> result = new MutableLiveData<>();
        Observer<List<RemoteAnalysisRow>> resultObserver = new Observer<List<RemoteAnalysisRow>>() {
            @Override
            public void onChanged(List<RemoteAnalysisRow> remoteAnalysisRowsList) {
                if ((remoteAnalysisRowsList != null)&&(!remoteAnalysisRowsList.isEmpty())) {
                    result.removeObserver(this); // this = observer...

                    Remote2LocalConverter converter = new Remote2LocalConverter();
                    classScopeRemoteAnalysisRowsList = (ArrayList)remoteAnalysisRowsList;
                    ArrayList<Article> articlesList = converter.getArticlesList( classScopeRemoteAnalysisRowsList );
                    // TODO nie wiem czy ProgressPresenter działa...
                    // albo dodanie artykułw idzie za szybko, albo coś nie jest tak
                    // Zró jakiś delay przy dodaawaniu
                    ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, articlesList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.insertArticles( articlesList, progressBarPresenter );
                    testGetArticles();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllAnalysisRows(result);
    }

    // todo test
    private void testGetArticles() {
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
                    testCreateEanCodes( articleList );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllArticles(result);
    }

    // todo test
    private void testCreateEanCodes( List<Article> articleList ) {
        MutableLiveData<List<RemoteEanCode>> result = new MutableLiveData<>();
        Observer<List<RemoteEanCode>> resultObserver = new Observer<List<RemoteEanCode>>() {
            @Override
            public void onChanged(List<RemoteEanCode> remoteEanCodesList) {
                if ((remoteEanCodesList != null)&&(!remoteEanCodesList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    ArrayList<EanCode> eanCodeList = convertToEanCodes( remoteEanCodesList, articleList );

                    ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
                    ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, eanCodeList.size()  );
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.insertEanCodes( eanCodeList, progressBarPresenter );
                    testGetSectors();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getRemoteDataRepository().getAllEanCodes(result);

    }

    private ArrayList<EanCode> convertToEanCodes( List<RemoteEanCode> remoteEanCodesList, List<Article> articleList ) {
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
        return converter.getEanCodesList( remoteEanCodesHashMap, articlesHashMap );
        /*
        ArrayList<EanCode> getEanCodesList(
                HashMap<Integer, RemoteEanCode> remoteEanCodesHashMap,
                HashMap<Integer, Article> articlesHashMap )

         */

        /*
        ArrayList<EanCode> eanCodeList = new ArrayList<>();
        Remote2LocalConverter converter = new Remote2LocalConverter();
        for ( RemoteEanCode remoteEanCode : remoteEanCodesList ) {
            // todo null się wywali... Powinien być Article
            // todo potrzeban list wszystkich artykułów, żeby z niej pobierać
            EanCode eanCode = converter.getEanCode(remoteEanCode, null);
            eanCodeList.add( eanCode );
        }
        return eanCodeList;

         */
    }

    private void testGetSectors() {
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
                    testGetDepartments();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors(result);
    }
    
    private void testGetDepartments() {
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
                    testCreateOwnArticlesInfo();
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
    }
    
    private void testCreateOwnArticlesInfo() {
        Remote2LocalConverter converter = new Remote2LocalConverter();
        OwnArticleInfo ownArticleInfo;
        ArrayList<OwnArticleInfo> ownArticleInfoList = new ArrayList<>();
        for (RemoteAnalysisRow remoteAnalysisRow : classScopeRemoteAnalysisRowsList) {
            ownArticleInfo = converter.getOwnArticleInfo(
                    remoteAnalysisRow,
                    classScopeArticleMap.get( remoteAnalysisRow.getArticleCode() ),
                    classScopeDepartmentMap.get( remoteAnalysisRow.getDepartment() ),
                    classScopeSectorMap.get( remoteAnalysisRow.getSector() )
            );
            ownArticleInfoList.add(ownArticleInfo);
        }
        ProgressBar progressBar = getView().findViewById(R.id.remote_2_local_progressBar);
        ProgressBarPresenter progressBarPresenter = new ProgressBarPresenter( progressBar, ownArticleInfoList.size() );
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.insertOwnArticleInfos( ownArticleInfoList, progressBarPresenter );
    }

    //todo test
    private void openTestAnalyzesList() {
        Navigation.findNavController(getView()).navigate(R.id.action_mainFragment_to_analysisCompetitorsFragment);
    }

    private void  setOnBackPressedCalback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                new MaterialAlertDialogBuilder(getContext())/*, R.style.AlertDialogStyle) */
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_ok), new LogOffListener() )
                        .setNegativeButton(getActivity().getString(R.string.caption_cancel),null)
                        .show();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void finishApp() {
        AppHandle.getHandle().shutDown();
        getActivity().finishAndRemoveTask();
        System.exit(0);
    }

    private class LogOffListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finishApp();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }


    private void getPreferencesInfo() {
        //todo
    }

    private void getNewAnalysisInfo() {
        analysisDataUpdater.checkNewAnalysisToDownload();
        if (analysisDataUpdater.isNewAnalysisDataReadyToDownlad()) {
            analysisDataUpdater.downloadAnalysisBasicData();
        }
    }

//------------------------------------------------------------------------
// Obsługa Drawer menu
    private void menuOptionAnalyzes() {
    }

    private void menuOptionNewAnalysis() {
        // tego nie implementuję, bo w tej wersji nie będzie możliwości tworzenia własnych analiz
    }

    private void menuOptionCompetitors() {

    }

    private void menuOptionNewCompetitor() {

    }

    private void menuOptionPreferences() {

    }

    private void menuOptionLogOff() {

    }

    private void menuOptionMainScreen() {

    }

}
