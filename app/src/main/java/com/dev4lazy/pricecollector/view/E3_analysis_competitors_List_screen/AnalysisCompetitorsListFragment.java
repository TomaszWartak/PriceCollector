package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;


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

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUploader;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AlertDialogFragmentViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.dev4lazy.pricecollector.viewmodel.CompetitorsSlotsViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AnalysisCompetitorsListFragment extends Fragment { //OK

    private CompetitorsSlotsListView competitorsSlotsListView;
    private CompetitorsSlotsViewModel competitorsSlotsViewModel;

    public static AnalysisCompetitorsListFragment newInstance() {
        return new AnalysisCompetitorsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_competitors_fragment, container, false);
        setOnBackPressedCallback();
        listViewSetup(view);
        listViewSubscribtion();
        return view;
    }

        private void setToolbarText( String toolbarText ) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
        }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    Navigation.findNavController(getView()).navigate(R.id.action_analysisCompetitorsFragment_to_analyzesListFragment);
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        private void listViewSetup(View view) {
            competitorsSlotsListView = view.findViewById(R.id.analysis_competitors_listview);
            competitorsSlotsListView.setup(
                    new ViewModelProvider( getActivity() ).get( StoreViewModel.class),
                    new ViewModelProvider( getActivity() ).get( CompetitorsSlotsViewModel.class)
            );
        }

        private void listViewSubscribtion() {
            competitorsSlotsViewModel = new ViewModelProvider( getActivity() ).get( CompetitorsSlotsViewModel.class);
            competitorsSlotsViewModel.getCompetitorsSlotsFullDataLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<CompetitorSlotFullData>>() {
                @Override
                public void onChanged(ArrayList<CompetitorSlotFullData> competitorSlotFullDataList) {
                    if (!competitorSlotFullDataList.isEmpty()) {
                        competitorsSlotsListView.submitCompetitorsSlotsList(competitorSlotFullDataList);
                    }
                }
            });
        }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarText(getString(R.string.competitors_toolbar_text));
        navigationViewMenuSetup();
    }

        private void navigationViewMenuSetup() {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.analysis_competitors_list_screen_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.analysis_competitors_list_screen_logout_menu_item:
                            new LogoutQuestionDialog( getContext(), getActivity() ).get();
                            break;
                        case R.id.analysis_competitors_list_screen_upload_data_menu_item:
                            showAskUserForAnalyzesDataUploadDialog();
                            break;
                        case R.id.analysis_competitors_list_screen_gotoanalyzes_menu_item:
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisCompetitorsFragment_to_analyzesListFragment );
                            break;
                    }
                    return false;
                }
            });
        }

            private void showAskUserForAnalyzesDataUploadDialog() {
                AlertDialogFragmentViewModel alertDialogFragmentViewModel =
                        new ViewModelProvider(getActivity()).get(AlertDialogFragmentViewModel.class);
                alertDialogFragmentViewModel.setAlertDialog(
                        getAskUserForAnalyzesDataUploadDialog()
                );
                Navigation.findNavController( getView() ).navigate( R.id.action_analysisCompetitorsFragment_to_alertDialogFragment );
            }

            private AlertDialog getAskUserForAnalyzesDataUploadDialog() {
                AlertDialog alertDialog =
                        new MaterialAlertDialogBuilder(
                                getContext(),
                                R.style.PC_AlertDialogStyle_Overlay
                        )
                        // .setTitle( "")
                        .setMessage( getString( R.string.question_about_uploading_data) )
                        .setPositiveButton(
                                getString( R.string.caption_yes) ,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        uploadAnalysisData();
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

            private void uploadAnalysisData() {
                if (isNetworkAvailable()) {
                    AnalyzesListViewModel analyzesListViewModel =
                            new ViewModelProvider( AppUtils.getActivity( getContext() ) ).get( AnalyzesListViewModel.class );
                    new AnalysisDataUploader( analyzesListViewModel.getChosenAnalysis() ).uploadData();
                } else {
                    Toast.makeText(
                            getContext(),
                            AppHandle.getHandle().getString( R.string.network_not_available ),
                            Toast.LENGTH_SHORT
                    ).show();                }
            };

                private boolean isNetworkAvailable() {
                    return AppHandle.getHandle().getNetworkAvailabilityMonitor().isNetworkAvailable();
                }

}