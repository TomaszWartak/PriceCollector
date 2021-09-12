package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;


import android.content.DialogInterface;
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
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesListFragment extends Fragment {

    private AnalysisArticleJoinsRecyclerView analysisArticleJoinsRecyclerView;
    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;

    public static AnalysisArticlesListFragment newInstance() {
        return new AnalysisArticlesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_list_fragment, container, false);
        setOnBackPressedCallback();
        analysisArticleJoinsListViewModel =
                new ViewModelProvider( getActivity() ).get( AnalysisArticleJoinsListViewModel.class );
        setToolbarText();
        recyclerViewSetup( view );
        recyclerViewSubscribtion();
        return view;
    }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinsListViewModel.class )
                            .getSearchArticlesCriteria()
                            .clearAll();
                    Navigation.findNavController(getView()).navigate(R.id.action_analysisArticlesListFragment_to_analysisCompetitorsFragment);
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        private void setToolbarText() {
            if (analysisArticleJoinsListViewModel.getSearchArticlesCriteria().isFilterSet()) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PriceCollector *");
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("PriceCollector");
            }
        }

        private void recyclerViewSetup( View view ) {
            analysisArticleJoinsRecyclerView = view.findViewById(R.id.analysis_articles_recycler);
            analysisArticleJoinsRecyclerView.setup();
        }

        private void recyclerViewSubscribtion() {
            AnalyzesListViewModel analyzesListViewModel
                    = new ViewModelProvider( getActivity() ).get( AnalyzesListViewModel.class );
            analysisArticleJoinsListViewModel.buildAnalysisiArticleJoinsPagedList( analyzesListViewModel.getChosenAnalysisId() );
            analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().observe(
                    getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
                @Override
                public void onChanged( PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                    if (!analysisArticlesJoins.isEmpty()) {
                        analysisArticleJoinsRecyclerView.submitArticlesList( analysisArticlesJoins );
                    }
                }
            });
        }

    @Override
    public void onStart() {
        super.onStart();
        navigationViewMenuSetup();
    }

        private void navigationViewMenuSetup( ) {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.anlysis_articles_list_screen_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.anlysis_articles_list_screen_gotoanalyzes_menu_item:
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesListFragment_to_analyzesListFragment );
                            break;
                        case R.id.anlysis_articles_list_screen_search_menu_item:
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesListFragment_to_searchArticlesFragment );
                            break;
                        case R.id.anlysis_articles_list_screen_logout_menu_item:
                            getLogoutQuestionDialog();
                            break;
                    }
                    return false;
                }
            });
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

}
