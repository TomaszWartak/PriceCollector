package com.dev4lazy.pricecollector.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.AnalysisListViewModel;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;
import static com.dev4lazy.pricecollector.view.ProgressPresenter.DATA_SIZE_UNKNOWN;

public class MainScreenFragment extends Fragment {


    private MainViewModel mViewModel;

    private AnalysisListViewModel viewModel;
    private RecyclerView recyclerView;
    private AnalysisAdapter analysisAdapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_screen_fragment, container, false);

        setOnBackPressedCalback();

        // todo? setAnalysisItem( inflater, view );

        recyclerSetup( view );
        subscribeRecycler();

        // TODO !!! i teraz, jak się wyświetlą rekordy analiz, to trzeba sprawdzić, czy są ich artykuły do anali itd..

        /**no i tu się wypierdala, bo progrsspresenter jest dla pojedynczej analizy, a na tym etapie analizy nie są wyświetlone
        updateArticlesAllData( new ProgressBarPresenter( getView().findViewById( R.id.analysis_remote_2_local_progressBar) , DATA_SIZE_UNKNOWN ) );
         */
        // TODO utworzenie rekordu analizy? zob. czy to nie jest w AnalysisCompetitorsFragment
        // TODO utworzenie AnalisysArticles
        // TODO otwarcie fragmentu ze slotami - zob openTestAnalyzesList(); <- znienić nazwę?


        ((TextView)view.findViewById(R.id.user_login)).setText(AppHandle.getHandle().getSettings().getUser().getLogin());

        // todo test
        view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_testActionsFragment);
        });

        //todo sprawdzenie, co jest w preferencjach i odtworzenie na ekranie
        // zanim to zrobisz zrób wyświetlanie listy OwnArticlesInfo JOIn z innych tabel,
        // czyli .model.joins.AnalysisArticleJoin i AnalysisArticleDao
        getPreferencesInfo(); // todo ????

        // todo jeśli analiza jest w trakcie - możliwość kontynuacji
        // todo sprawdzenie czy na serwerze zdalnym jest nowa analiza - wyświetlenie
        getNewAnalysisInfo();

        // todo incializacja menu, czy szuflady?
        return view;
    }

    private void recyclerSetup( View view ) {
        recyclerView = view.findViewById( R.id.analyzes_recycler );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) ); // todo ????
        // todo recyclerView.addItemDecoration( new DividerItemDecoration( getActivity(), VERTICAL ));
        analysisAdapter = new AnalysisAdapter( new AnalysisDiffCallback() );
        recyclerView.setAdapter( analysisAdapter );
    }

    private void subscribeRecycler() {
        viewModel = ViewModelProviders.of(this ).get( AnalysisListViewModel.class );
        viewModel.getAnalyzesLiveData().observe( this,  new Observer<PagedList<Analysis>>() {
            @Override
            public void onChanged( PagedList<Analysis> analyzesList  ) {
                if (!analyzesList.isEmpty()) {
                    analysisAdapter.submitList( analyzesList);
                }
            }
        });
    }

    private void setAnalysisItem(@NonNull LayoutInflater inflater, View view) {
        viewAnalysisiItem = inflater.inflate(R.layout.test_analysis_item,null);
        ((ViewGroup) view).addView(viewAnalysisiItem);
        view.findViewById(R.id.card_view_constraint_layout).setOnClickListener((View v) -> {
            // W pierwszej kolejności utworzenie danych artykułów, których jeszcze nie ma w lokalnej bazie dancyh,
            // a mają być analizowane.
            updateArticlesAllData( new ProgressBarPresenter( getView().findViewById(R.id.analysis_item__progressBar), DATA_SIZE_UNKNOWN ) );
            // TODO utworzenie rekordu analizy? zob. czy to nie jest w AnalysisCompetitorsFragment
            // TODO utworzenie AnalisysArticles
            // TODO otwarcie fragmentu ze slotami - zob openTestAnalyzesList(); <- znienić nazwę?
        });
    }

    public void updateArticlesAllData( ProgressPresenter progressPresenter ) {
        AnalysisDataUpdater.getInstance().insertArticles( progressPresenter );
    }

    //todo test
    private void openTestAnalyzesList() {
        Navigation.findNavController( getView() ).navigate(R.id.action_mainFragment_to_analysisCompetitorsFragment);
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
        // język
        // użytkownik -> sklep amcierzysty, dział ew sektor
        // sprawdzenie co słychać w zdalnej bazie danych -> poniżej jest getNewAnalysisInfo()
        // ew info do użytkownika
        // zob. klasę .model.logic.AnalysisDataUpdater
        // zob. OneNote Kodowanie / Po zalogowaniu / ??Aktualizacja MainScreenFragment
    }

    private void getNewAnalysisInfo() {
        /**/ //TODO TEST
        // analysisDataUpdater.downloadAnalysisBasicData();
        /**/
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
