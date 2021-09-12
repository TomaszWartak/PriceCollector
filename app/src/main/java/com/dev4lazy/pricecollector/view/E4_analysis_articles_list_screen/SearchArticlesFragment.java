package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.SearchArticlesCriteria;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

public class SearchArticlesFragment extends Fragment {

    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;

    public static SearchArticlesFragment newInstance() {
        return new SearchArticlesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_fragment, container, false);
        setOnBackPressedCallback();
        analysisArticleJoinsListViewModel =
                new ViewModelProvider( getActivity() ).get( AnalysisArticleJoinsListViewModel.class );
        // TODO XXX setToolbarText();
        setupTabLayout( view );
        return view;
    }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    analysisArticleJoinsListViewModel.getSearchArticlesCriteria().clearAll();
                    Navigation.findNavController(getView()).navigate(R.id.action_searchArticlesFragment_to_analysisArticlesListFragment);
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

        private void setupTabLayout(View view) {
            ViewPager2 viewPager = view.findViewById(R.id.search_articles_viewpager);
            SearchArticlesPagerAdapter searchArticlesPagerAdapter = new SearchArticlesPagerAdapter(this);
            viewPager.setAdapter(searchArticlesPagerAdapter);

            TabLayout tabLayout = view.findViewById(R.id.search_articles_tabs);
            /**/
            new TabLayoutMediator(
                    tabLayout,
                    viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    SearchArticlesCriteria searchArticlesCriteria =
                            analysisArticleJoinsListViewModel.getSearchArticlesCriteria();
                    String filterSetSign = "";
                    switch (position) {
                        case SearchArticlesPagerAdapter.SEARCH_BY_DATA:
                            if (searchArticlesCriteria.isDataFilterSet()) {
                                filterSetSign = " *";
                            }
                            tab.setText(getResources().getString(R.string.articles_search_tab_data_name)+filterSetSign);
                            // tab.setCustomView( view.findViewById(R.id.search_articles_by_data_tab ) );
                            break;
                        case SearchArticlesPagerAdapter.SEARCH_BY_STRUCTURE:
                            if (searchArticlesCriteria.isStructureFilterSet()) {
                                filterSetSign = " *";
                            }
                            tab.setText(getResources().getString(R.string.articles_search_tab_structure_name)+filterSetSign);
                            // tab.setCustomView( view.findViewById(R.id.search_articles_by_structure_tab ) );
                            break;
                    }
                }
            }).attach();
        }

    @Override
    public void onStart() {
        super.onStart();
        navigationViewMenuSetup();
    }

        private void navigationViewMenuSetup() {
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
            Menu navigationViewMenu = navigationView.getMenu();
            navigationViewMenu.clear();
            navigationView.inflateMenu(R.menu.search_articles_fragment_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.search_articles_gotoanalyzes_menu_item:
                            analysisArticleJoinsListViewModel.getSearchArticlesCriteria().clearAll();
                            Navigation.findNavController( getView() ).navigate( R.id.action_searchArticlesFragment_to_analyzesListFragment );
                            break;
                        case R.id.search_articles_logout_menu_item:
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
                        // TODO promotor?: czy to można bardziej elegancko zrobić?
                        AppHandle.getHandle().shutdown();
                        getActivity().finishAndRemoveTask();
                        System.exit(0);
                    }
}
