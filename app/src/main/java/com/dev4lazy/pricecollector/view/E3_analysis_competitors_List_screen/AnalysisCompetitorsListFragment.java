package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUploader;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.dev4lazy.pricecollector.viewmodel.CompetitorsSlotsViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AnalysisCompetitorsListFragment extends Fragment {

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
            // todo askForSlots();
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
                            // TODO XXX getLogoutQuestionDialog();
                            break;
                        case R.id.analysis_competitors_list_screen_uploaddata_menu_item:
                            uploadAnalysisData();
                            break;
                        case R.id.analysis_competitors_list_screen_gotoanalyzes_menu_item:
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisCompetitorsFragment_to_analyzesListFragment );
                            break;
                    }
                    return false;
                }
            });
        }

        /* TODO XXX
            private void getLogoutQuestionDialog() {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("")
                        .setMessage(R.string.question_close_app)
                        .setPositiveButton(getActivity().getString(R.string.caption_yes), new LogoutDialogListener( getActivity() ) )
                        .setNegativeButton(getActivity().getString(R.string.caption_no),null)
                        .show();
            }

         */

            /* TODO XXX
                private class LogoutDialogListener implements DialogInterface.OnClickListener {

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

             */

            private void uploadAnalysisData() {
                AnalyzesListViewModel analyzesListViewModel =
                        new ViewModelProvider( AppUtils.getActivity( getContext() ) ).get( AnalyzesListViewModel.class );
                new AnalysisDataUploader( analyzesListViewModel.getChosenAnalysis() ).uploadData();
            };
}