package com.dev4lazy.pricecollector.view.E5_article_screen;


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
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.MainActivity;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {


    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;
    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
    // Niestety nie da się dziedziczyc po ViewPager2, bo jest final.
    // Dlatego implementacja Adaptera została tutaj.
    private ViewPager2 analysisArticlesViewPager;
    private AnalysisArticleJoinPagerAdapter analysisArticleJoinPagerAdapter;

    public static AnalysisArticlesPagerFragment newInstance() {
        return new AnalysisArticlesPagerFragment();
    }

    // TODO XXX ?
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO XXX startMainActivityLifecycleObserving();
    }

        /* TODO XXX - zastanów się, czy nie trzeba  gdzies zakończyc obserwacji (onPause(), onStop(),
        //	 onDestroyView()
        private void startMainActivityLifecycleObserving() {
            Lifecycle activityLifecycle = getActivity().getLifecycle();
            activityLifecycle.addObserver(this);
        }

         */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_pager_fragment, container, false);
        viewModelsSetup();
        viewPagerSetup( view );
        // TODO XXX setOnBackPressedCallback();
        setToolbarText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getArticleName() );
        viewPagerSubscribtion( analysisArticleJoinViewModel );
        return view;
    }


        private void viewModelsSetup() {
            analysisArticleJoinsListViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinsListViewModel.class );
            analysisArticleJoinViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinViewModel.class );
        }

        private void viewPagerSetup( View view ) {
            analysisArticlesViewPager = view.findViewById(R.id.analysis_articles_pager);
            analysisArticleJoinPagerAdapter = new AnalysisArticleJoinPagerAdapter( new AnalysisArticleJoinDiffCallback() );
            analysisArticlesViewPager.setAdapter(analysisArticleJoinPagerAdapter);
            analysisArticlesViewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // AnalysisArticleJoin analysisArticleJoin = analysisArticlesViewPager.getCurrentItem();
                    AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
                    setToolbarText( analysisArticleJoin.getArticleName() );
                    analysisArticleJoinViewModel.setAnalysisArticleJoin( analysisArticleJoin );
                    analysisArticleJoinViewModel.setRecyclerViewPosition( position );
                }
            });
        }

        private void setToolbarText( String toolbarText ) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
        }

        /* TODO XXX
        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
                @Override
                public void handleOnBackPressed() {
                    analysisArticleJoinPagerAdapter.getCurrentList().get( analysisArticleJoinViewModel.getRecyclerViewPosition() )
                    analysisArticleJoinViewModel.setAnalysisArticleJoin( getItem( getAdapterPosition() ) );
                    analysisArticleJoinViewModel.setRecyclerViewPosition( getAdapterPosition() );
                    NavHostFragment.findNavController(ge).navigateUp();
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }
        */

        private void viewPagerSubscribtion( AnalysisArticleJoinViewModel analysisArticleJoinViewModel ) {
            analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
                @Override
                public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                    if (!analysisArticlesJoins.isEmpty()) {
                        analysisArticleJoinPagerAdapter.submitList(analysisArticlesJoins);
                        // TODO ??? jeśli pojawi się zmiana na liście, to będzie się ustawiac w tym miejscu?
                        analysisArticlesViewPager.setCurrentItem(
                                analysisArticleJoinViewModel.getRecyclerViewPosition(),
                                false
                        );
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
            navigationView.inflateMenu(R.menu.article_screen_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // return true to display the item as the selected item
                    DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_activity_with_drawer_layout);
                    drawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.article_screen_clear_competitor_article_data_item:
                            clearCompetitorArticleData();
                            break;
                        case R.id.article_screen_gotoanalyzes_menu_item:
                            analysisArticleJoinsListViewModel.getSearchArticlesCriteria().clearAll();
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesPagerFragment_to_analyzesListFragment );
                            break;
                        case R.id.article_screen_logout_menu_item:
                            getLogoutQuestionDialog();
                            break;
                    }
                    return false;
                }
            });
        }

        private void clearCompetitorArticleData() {
            // TODO analysisArticlesViewPager.getAdapter().getItemId()
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

    @Override
    public void onStart() {
        super.onStart();
        navigationViewMenuSetup();
    }

}
