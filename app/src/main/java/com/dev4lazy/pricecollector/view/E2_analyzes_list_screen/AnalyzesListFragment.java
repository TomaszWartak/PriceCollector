package com.dev4lazy.pricecollector.view.E2_analyzes_list_screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel2;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import static com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader.getInstance;

public class AnalyzesListFragment extends Fragment {

    private AnalyzesListViewModel analyzesListViewModel;
    private AnalyzesRecyclerView analyzesRecyclerView;
    private MutableLiveData<Boolean> newAnalyzesReady; // todo usunąć?

    public static AnalyzesListFragment newInstance() {
        return new AnalyzesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyzes_list_fragment, container, false);
        // TODO XXX startMainActivityLifecycleObserving();
        setToolbarText(getString(R.string.analyzes_toolbar_text));
        setOnBackPressedCallback();
        recyclerViewSetup( view );
        recyclerViewSubscribtion();
        newAnalyzesCheck();

        if (BuildConfig.DEBUG) {
            /*todo test
            // Wyświetlenie nazwy zalogowanego użytkownika
            ((TextView) view.findViewById(R.id.user_login)).setText(AppHandle.getHandle().getSettings().getUser().getLogin());
             */
            // Ustawienie otwarcia ekranu z czyszczeniem i inicjalizacją danych
            view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
                Navigation.findNavController(view).navigate(R.id.action_analyzesListFragment_to_testActionsFragment);
            });
        }

        // todo incializacja menu, czy szuflady?
        return view;
    }

        // TODO XXX - zastanó się, czy nie trzeba  gdzies zakończyc obserwacji (onPause(), onStop(),
        //	 onDestroyView()
        /*
        private void startMainActivityLifecycleObserving() {
            Lifecycle activityLifecycle = getActivity().getLifecycle();
            activityLifecycle.addObserver(this);
        }

         */

        private void setToolbarText( String toolbarText ) {
            int maxLength = toolbarText.length();
            if (maxLength>24) { // TODO Hardcoded - sprawdź inne wystąpienia (może opakować toolbar?)
                maxLength=24;
            }
            toolbarText = toolbarText.substring(0,maxLength);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
        }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    getLogoutQuestionDialog();
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

            private void getLogoutQuestionDialog() {
                new MaterialAlertDialogBuilder(getContext())/*, R.style.AlertDialogStyle) */
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_yes), new LogOffListener() )
                        .setNegativeButton(getActivity().getString(R.string.caption_no),null)
                        .show();
            }

                private class LogOffListener implements DialogInterface.OnClickListener {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishApp();
                    }
                }

                    private void finishApp() {
                        // TODO promotor: czy to można bardziej elegancko zrobić?
                        AppHandle.getHandle().shutdown();
                        getActivity().finishAndRemoveTask();
                        System.exit(0);
                    }


        private void recyclerViewSetup(View view ) {
            analyzesRecyclerView = view.findViewById( R.id.analyzes_recycler );
            analyzesRecyclerView.setup();
        }

        private void recyclerViewSubscribtion() {
            analyzesListViewModel = new ViewModelProvider( getActivity() ).get( AnalyzesListViewModel.class );
            analyzesListViewModel.getAnalyzesLiveData().observe( getViewLifecycleOwner(),  new Observer<PagedList<Analysis>>() {
                @Override
                public void onChanged( PagedList<Analysis> analyzesList  ) {
                    if (!analyzesList.isEmpty()) {
                        analyzesRecyclerView.submitAnalyzesList( analyzesList);
                    }
                }
            });
        }

        private void newAnalyzesCheck() {
            /*newAnalyzesReady = new MutableLiveData<>();
            AnalysisDataDownloader analysisDataDownloader = getInstance();
            newAnalyzesReady.setValue( analysisDataDownloader.isNewAnalysisReadyToDownlad() );
            newAnalyzesReady.observe( getViewLifecycleOwner(),  new Observer<Boolean>() {
                @Override
                public void onChanged( Boolean newAnalyzesReady  ) {
                    if (newAnalyzesReady) {
                        showAskUserForAnalyzesDataDownload( getView() );
                    }
                }
            }); */
            AnalysisDataDownloader analysisDataDownloader = getInstance();
            analysisDataDownloader.getNewAnalysisReadyToDownladLD().observe( getViewLifecycleOwner(),  new Observer<Boolean>() {
                @Override
                public void onChanged( Boolean newAnalyzesReady  ) {
                    if (newAnalyzesReady) {
                        showAskUserForAnalyzesDataDownload( getView() );
                    }
                }
            });
        }

    /* TODO XXX
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void afterActivityON_CREATE() {
        navigationViewMenuSetup();
    }

     */

        private void navigationViewMenuSetup() {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.anlyzes_list_screen_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.analyzes_list_screen_logout_menu_item:
                            getLogoutQuestionDialog();
                            break;
                    }
                    return false;
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
        AnalysisDataDownloader.getInstance().insertArticles( progressPresenter );
    }

    //todo test
    private void openTestAnalyzesList() {
        Navigation.findNavController( getView() ).navigate(R.id.action_mainFragment_to_analysisCompetitorsFragment);
    }
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalysisDataDownloader analysisDataDownloader = getInstance();
        if (analysisDataDownloader.isNewAnalysisReadyToDownlad()) {
            showAskUserForAnalyzesDataDownload( view );
        }
    }

        private void showAskUserForAnalyzesDataDownload( View view ) {
            AlertDialogFragmentViewModel2 alertDialogFragmentViewModel =
                    new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel2.class);
            alertDialogFragmentViewModel.setAlertDialog( getAskUserForAnalyzesDataDownloadDialog() );
            Navigation.findNavController( view ).navigate( R.id.action_analyzesListFragment_to_alertDialogFragment2 );
        }

            @NonNull
            private AlertDialog getAskUserForAnalyzesDataDownloadDialog() {
                AlertDialog alertDialog = new MaterialAlertDialogBuilder( getContext() )
                        .setTitle( getString( R.string.basic_data_ready_to_download))
                        .setMessage( getString( R.string.question_about_downloading_data) )
                        .setPositiveButton(
                                getString( R.string.caption_yes) ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AnalysisDataDownloader.getInstance().downloadAnalysisBasicData();
                                        analyzesRecyclerView.refresh();
                                    }
                                }
                        )
                        // .setNegativeButton( alertDialogFragmentViewModel.getNegativeButtonLabel(), alertDialogFragmentViewModel.getNegativeButtonOnClickListener() )
                        .setNegativeButton(
                                getString( R.string.caption_no),
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
                return alertDialog;
            }

    @Override
    public void onStart() {
        super.onStart();
        navigationViewMenuSetup();
    }

    //------------------------------------------------------------------------
// TODO XXX Obsługa Drawer menu
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
