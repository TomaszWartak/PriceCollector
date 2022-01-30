package com.dev4lazy.pricecollector.view.E2_analyzes_list_screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisBasicDataDownloader;
import com.dev4lazy.pricecollector.model.utils.LocalDataInitializer;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import static com.dev4lazy.pricecollector.model.logic.AnalysisBasicDataDownloader.getInstance;

public class AnalyzesListFragment extends Fragment { //OK

    private AnalyzesListViewModel analyzesListViewModel;
    private AnalyzesRecyclerView analyzesRecyclerView;
    public static AnalyzesListFragment newInstance() {
        return new AnalyzesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analyzes_list_fragment, container, false);
        setOnBackPressedCallback();
        recyclerViewSetup( view );
        recyclerViewSubscribtion();
        newAnalyzesCheck();
        if (BuildConfig.DEBUG) {
            view.findViewById(R.id.main_screen_fragment_layout).setOnClickListener((View v) ->{
                Navigation.findNavController(view).navigate(R.id.action_analyzesListFragment_to_testActionsFragment);
            });
        }
        return view;
    }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    new LogoutQuestionDialog( getContext(), getActivity() ).get();
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        private void recyclerViewSetup(View view ) {
            analyzesRecyclerView = view.findViewById( R.id.analyzes_recycler );
            analyzesRecyclerView.setup();
        }

        private void recyclerViewSubscribtion() {
            analyzesListViewModel = new ViewModelProvider( getActivity() ).get( AnalyzesListViewModel.class );
            analyzesListViewModel.getAnalyzesLiveData().observe( getViewLifecycleOwner(),  new Observer<PagedList<Analysis>>() {
                @Override
                public void onChanged( PagedList<Analysis> analyzesList ) {
                    if (!analyzesList.isEmpty()) {
                        analyzesRecyclerView.submitAnalyzesList( analyzesList);
                    }
                }
            });
        }

        private void newAnalyzesCheck() {
            AnalysisBasicDataDownloader analysisBasicDataDownloader = getInstance();
            analysisBasicDataDownloader.getNewAnalysisReadyToDownladLD().observe( getViewLifecycleOwner(),  new Observer<Boolean>() {
                @Override
                public void onChanged( Boolean newAnalyzesReady  ) {
                    if (newAnalyzesReady) {
                        showAskUserForAnalyzesDataDownload( getView() );
                    }
                }
            });
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalysisBasicDataDownloader analysisBasicDataDownloader = getInstance();
        if (analysisBasicDataDownloader.isNewAnalysisReadyToDownlad()) {
            showAskUserForAnalyzesDataDownload( view );
        }
    }

        private void showAskUserForAnalyzesDataDownload( View view ) {
            AlertDialogFragmentViewModel alertDialogFragmentViewModel =
                    new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel.class);
            alertDialogFragmentViewModel.setAlertDialog( getAskUserForAnalyzesDataDownloadDialog() );
            Navigation.findNavController( view ).navigate( R.id.action_analyzesListFragment_to_alertDialogFragment);
        }

            @NonNull
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
                                        if (isNetworkAvailable()) {
                                            AnalysisBasicDataDownloader.getInstance().downloadAnalysisBasicData();
                                            analyzesRecyclerView.refresh();
                                        } else {
                                            Toast.makeText(
                                                    getContext(),
                                                    AppHandle.getHandle().getString( R.string.network_not_available ),
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }                                    }
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

                private boolean isNetworkAvailable() {
                    return AppHandle.getHandle().getNetworkAvailabilityMonitor().isNetworkAvailable();
                }

    @Override
    public void onStart() {
        super.onStart();
        toolbarSetup();
        navigationViewMenuSetup();
    }

    public void toolbarSetup() {
        setToolbarText(getString(R.string.analyzes_toolbar_text));
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

        private void setToolbarText( String toolbarText ) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
        }


        private void navigationViewMenuSetup() {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.analyzes_list_screen_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.analyzes_list_screen_clear_local_db_menu_item:
                            showAskUserForClearLocalDatabaseDialog();
                            break;
                        case R.id.analyzes_list_screen_logout_menu_item:
                            new LogoutQuestionDialog( getContext(), getActivity() ).get();
                            break;
                    }
                    return false;
                }
            });
        }

            private void showAskUserForClearLocalDatabaseDialog() {
                AlertDialogFragmentViewModel alertDialogFragmentViewModel =
                        new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel.class);
                alertDialogFragmentViewModel.setAlertDialog(
                        getAskUserForClearLocalDatabaseDialog()
                );
                Navigation.findNavController( getView() ).navigate( R.id.action_analyzesListFragment_to_alertDialogFragment2 );
            }

                private AlertDialog getAskUserForClearLocalDatabaseDialog() {
                    AlertDialog alertDialog =
                            new MaterialAlertDialogBuilder(
                                    getContext(),
                                    R.style.PC_AlertDialogStyle_Overlay
                            )
                            // TODO XXX .setTitle( getString( R.string.basic_data_ready_to_download))
                            .setMessage( getString( R.string.question_about_clearing_local_data) )
                            .setPositiveButton(
                                    getString( R.string.caption_yes) ,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            LocalDataInitializer.getInstance().clearLocalDatabase();
                                            analyzesRecyclerView.refreshAfterClearDatabase();
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

}
