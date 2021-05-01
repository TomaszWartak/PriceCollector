package com.dev4lazy.pricecollector.view.E2_analyzes_list_screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.utils.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel2;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.dev4lazy.pricecollector.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater.getInstance;

public class AnalyzesListFragment extends Fragment {


    private MainViewModel mViewModel;

    private AnalyzesListViewModel viewModel;
    private RecyclerView recyclerView;
    private AnalysisAdapter analysisAdapter;

    // todo? private View viewAnalysisiItem;

    // todo to usunąć, bo służy tylko do wygenerowania mocka bazy remote
    // private RemoteDatabaseInitializer converter;

    public static AnalyzesListFragment newInstance() {
        return new AnalyzesListFragment();
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
        View view = inflater.inflate(R.layout.analyzes_list_fragment, container, false);

        setOnBackPressedCalback();

        recyclerSetup( view );
        subscribeRecycler();

        if (BuildConfig.DEBUG) {
            // todo test
            // Wyświetlenie nazwy zalogowanego użytkownika
            ((TextView) view.findViewById(R.id.user_login)).setText(AppHandle.getHandle().getSettings().getUser().getLogin());
            // Ustawienie otwarcia ekranu z czyszczeniem i inicjalizacją danych
            view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
                Navigation.findNavController(view).navigate(R.id.action_analyzesListFragment_to_testActionsFragment);
            });
        }

        // todo incializacja menu, czy szuflady?
        return view;
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

    private class LogOffListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finishApp();
        }
    }

    private void finishApp() {
        // TODO promotor: czy to można bardziej elegancko zrobić?
        AppHandle.getHandle().shutDown();
        getActivity().finishAndRemoveTask();
        System.exit(0);
    }

    private void recyclerSetup( View view ) {
        recyclerView = view.findViewById( R.id.analyzes_recycler );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) ); // todo ????
        // todo recyclerView.addItemDecoration( new DividerItemDecoration( getActivity(), VERTICAL ));
        analysisAdapter = new AnalysisAdapter( new AnalysisDiffCallback() );
        recyclerView.setAdapter( analysisAdapter );
    }

    private void subscribeRecycler() {
        viewModel = ViewModelProviders.of(this ).get( AnalyzesListViewModel.class );
        viewModel.getAnalyzesLiveData().observe( getViewLifecycleOwner(),  new Observer<PagedList<Analysis>>() {
            @Override
            public void onChanged( PagedList<Analysis> analyzesList  ) {
                if (!analyzesList.isEmpty()) {
                    analysisAdapter.submitList( analyzesList);
                }
            }
        });
    }

    /*
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
     */

    private void showAskUserForAnalyzesDataDownload( View view ) {
        AlertDialog alertDialog = new AlertDialog.Builder( getContext() )
            .setTitle( getString( R.string.data_ready_to_download ))
            .setMessage( getString( R.string.question_about_downloading_data) )
            .setPositiveButton(
                getString( R.string.yes ) ,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AnalysisDataUpdater.getInstance().downloadAnalysisBasicData();
                        // TODO !!! odśwież recycler
                        analysisAdapter.notifyDataSetChanged();
                    }
                }
            )
            // .setNegativeButton( alertDialogFragmentViewModel.getNegativeButtonLabel(), alertDialogFragmentViewModel.getNegativeButtonOnClickListener() )
            .setNegativeButton(
                getString( R.string.no ),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // todo?
                    }
                }
            )
            .setCancelable( false )
            .create();
        AlertDialogFragmentViewModel2 alertDialogFragmentViewModel = ViewModelProviders.of(getActivity()).get(AlertDialogFragmentViewModel2.class);
        alertDialogFragmentViewModel.setAlertDialog( alertDialog );
        Navigation.findNavController( view ).navigate( R.id.action_analyzesListFragment_to_alertDialogFragment2 );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalysisDataUpdater analysisDataUpdater = getInstance();
        if (analysisDataUpdater.isNewAnalysisReadyToDownlad()) {
            showAskUserForAnalyzesDataDownload( view );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
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
