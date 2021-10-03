package com.dev4lazy.pricecollector.view.E5_article_screen;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.TaskChain;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {

    private StoreViewModel competitorStoreViewModel;
    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;
    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
    private AnalysisArticleJoinDataUpdater analysisArticleJoinDataUpdater;
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
        analysisArticleJoinDataUpdater = new AnalysisArticleJoinDataUpdater( analysisArticleJoinsListViewModel );
        viewPagerSetup( view );
        // TODO XXX setOnBackPressedCallback();
        setToolbarText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getArticleName() );
        viewPagerSubscribtion( analysisArticleJoinViewModel );
        return view;
    }

        private void viewModelsSetup() {
            competitorStoreViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( StoreViewModel.class );
            analysisArticleJoinsListViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinsListViewModel.class );
            analysisArticleJoinViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinViewModel.class );
        }

        private void viewPagerSetup( View view ) {
            analysisArticlesViewPager = view.findViewById(R.id.analysis_articles_pager);
            analysisArticleJoinPagerAdapter =
                    new AnalysisArticleJoinPagerAdapter(
                            new AnalysisArticleJoinDiffCallback(),
                            analysisArticleJoinViewModel
                    );
            analysisArticlesViewPager.setAdapter(analysisArticleJoinPagerAdapter);
            analysisArticlesViewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    if (analysisArticleJoinViewModel.isNeedToSave() ) {
                        analysisArticleJoinViewModel.clearNeedToSave();
                        // TODO XXX AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
                        PagedList<AnalysisArticleJoin> pagedList = analysisArticleJoinPagerAdapter.getCurrentList();
                        int position2 = analysisArticleJoinViewModel.getPositionOnList();
                        AnalysisArticleJoin analysisArticleJoin = pagedList.get( position2 );
                        /* TODO XXX
                        analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(0);
                        analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(1);
                        analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(2);
                        analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(3);
                        analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
                        */
                        if (analysisArticleJoin.isCompetitorStoreIdNotSet()) {
                            analysisArticleJoin.setCompetitorStoreId( competitorStoreViewModel.getStore().getId() );
                        }
                        startSavingDataChain( analysisArticleJoin );
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // AnalysisArticleJoin analysisArticleJoin = analysisArticlesViewPager.getCurrentItem();
                    AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
                    setToolbarText( analysisArticleJoin.getArticleName() );
                    analysisArticleJoinViewModel.setAnalysisArticleJoin( analysisArticleJoin );
                    analysisArticleJoinViewModel.setPositionOnList( position );
                }
            });
        }
            private void startSavingDataChain(AnalysisArticleJoin analysisArticleJoin ) {
                AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder = analysisArticleJoinViewModel.getValuesStateHolder();
                TaskChain taskChain = analysisArticleJoinDataUpdater.getTaskChain();
                /*
                A. Jeśli dane artykułu referencyjnego (nazwa, opis) (Article) zostały zmienione, trzeba:
                    1. zapisać go lub uaktualnić.
                    2. Sprawdzić, czy przy tej okazji został zmieniony jego kod EAN (EanCode)
                        Jeśli tak i :
                        a) nie ma ustawionej wartości, to trzeba go usunąć z BD, jeśli jest w DB
                        b) ma ustawiną wartość - należy ean zapisać i zarejestrowac tą zmianę w cenie (CompetitorPrice).
                    3. Jeśli Ean nie był zmienioeny, to trzeba sprawdzić, czy cena była zmieniana i
                        a) jesli była zmieniana, i jeśli tak, to zapisać ją
                        b) jeśli nie była zmieniana, to w sytuacji, gdy artykuł referencyjny jeszcze nie istnieje w BD,
                        to trzeba jego id zapisać w cenie - czyli trzeba ją tak, czy siak, zapisać
                B. Jeśli dane artykułu referencyjnego (nazwa, opis) (Article) nie zostały zmienione, trzeba:
                    1. Sprawdzić, czy został zmieniony kod EAN (EanCode). Jeśli został zmieniony, to
                        a) gdy nie ma ustawionej wartości, to trzeba go usunąć z BD (jeśli jest w DB)
                        Gdy ma ustawioną wartość, to:
                        b) jeśli artykuł referencyjny jeszcze nie istnieje w BD, to trzeba go zapisać
                        c) zapisać ean
                        d) i zaktualizować id art ref i ean w cenie (CompetitorPrice).
                    2. Jeśli Ean nie był zmieniony, to
                        a) trzeba sprawdzićż czy cena była zmieniona i zapisać ją
                C. Jeśli został zmieniony Artykuł Strategiczny (AnalysisArticle) to trzeba go zapisać.
                */
        /*A*/   if (valuesStateHolder.isReferenceArticleChangedFlagSet() ) {
                    savingChangesOfReferenceArticle( analysisArticleJoin, valuesStateHolder, taskChain);
        /*B*/   } else {
            /*B1*/  if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) { /**/
                        savingChangesOfRefArtEanIfRefArtIsNotChanged(analysisArticleJoin, valuesStateHolder, taskChain);
            /*B2*/  } else {
                /*B2a*/ if (valuesStateHolder.isPriceChangedFlagSet()) {
                            taskChain.addTaskLink(
                                    analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                            taskChain,
                                            analysisArticleJoin,
                                            valuesStateHolder
                                    )
                            );
                        }
                    }
                }
          /*C*/ if (valuesStateHolder.isCommentsChangedFlagSet()) {
                    taskChain.addTaskLink(
                            analysisArticleJoinDataUpdater.new AnalysisArticleUpdater(
                                    taskChain,
                                    analysisArticleJoin,
                                    valuesStateHolder
                            )

                    );
                }
                analysisArticleJoinDataUpdater.getTaskChain().startIt();
            }

    private void savingChangesOfReferenceArticle(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain) {
 /*A1*/ taskChain.addTaskLink(
            analysisArticleJoinDataUpdater.new ReferenceArticleUpdater(
                taskChain,
                analysisArticleJoin,
                valuesStateHolder
            )
        );
        if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) {
     /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged( analysisArticleJoin, valuesStateHolder, taskChain );
        } else {
     /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged( analysisArticleJoin, valuesStateHolder, taskChain );
        }
    }

    private void /*A2*/ savingChangesOfRefArtEanIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain) {
/*A2a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
/*A2b*/} else {
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
        }
    }

    private void /*A3*/ savingChangesOfCompetitorPriceIfRefArtIsChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain) {
/*A3a*/ if (valuesStateHolder.isPriceChangedFlagSet()) {
            taskChain.addTaskLink(
                    analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                            taskChain,
                            analysisArticleJoin,
                            valuesStateHolder
                    )
            );
        } else {
    /*A3b*/  if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
        }
    }

    private void savingChangesOfRefArtEanIfRefArtIsNotChanged(
            AnalysisArticleJoin analysisArticleJoin,
            AnalysisArticleJoinViewModel.AnalysisArticleJoinValuesStateHolder valuesStateHolder,
            TaskChain taskChain) {
/*B1a*/ if (isReferenceArticleEanCleared(analysisArticleJoin)) {
            if (isReferenceArticleEanInDB(analysisArticleJoin)) {
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new ReferenceArticleEanDeleter(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
                taskChain.addTaskLink(
                        analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                                taskChain,
                                analysisArticleJoin,
                                valuesStateHolder
                        )
                );
            }
         } else {
     /*B1b*/ if (isReferenceArticleNotInDB(analysisArticleJoin)) {
                 taskChain.addTaskLink(
                         analysisArticleJoinDataUpdater.new ReferenceArticleUpdater(
                                 taskChain,
                                 analysisArticleJoin,
                                 valuesStateHolder
                         )
                 );
             }
     /*B1c*/ taskChain.addTaskLink(
                     analysisArticleJoinDataUpdater.new ReferenceArticleEanUpdater(
                             taskChain,
                             analysisArticleJoin,
                             valuesStateHolder
                     )
             );
     /*B1d*/ taskChain.addTaskLink(
                     analysisArticleJoinDataUpdater.new CompetitorPriceUpdater(
                             taskChain,
                             analysisArticleJoin,
                             valuesStateHolder
                     )
             );
         }
/*B1b*/
    }

                private boolean isReferenceArticleEanCleared(AnalysisArticleJoin analysisArticleJoin) {
                    return analysisArticleJoin.isReferenceArticleEanNotSet();
                }


                boolean isReferenceArticleEanInDB(AnalysisArticleJoin analysisArticleJoin) {
                    if (analysisArticleJoin.getReferenceArticleEanCodeId() == null) {
                        return false;
                    }
                    return analysisArticleJoin.getReferenceArticleEanCodeId() > 0;
                }

                boolean isReferenceArticleNotInDB(AnalysisArticleJoin analysisArticleJoin) {
                        if (analysisArticleJoin.getReferenceArticleId() == null) {
                            return true;
                        }
                        return analysisArticleJoin.getReferenceArticleId() < 1;
                }

        private void setToolbarText( String toolbarText ) {
            int maxLength = toolbarText.length();
            if (maxLength>24) {
                maxLength=24;
            }
            toolbarText = toolbarText.substring(0,maxLength);
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
                                analysisArticleJoinViewModel.getPositionOnList(),
                                false
                        );
                    }
                }
            });
        }

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
                            analysisArticleJoinViewModel.setPositionOnList(0);
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
            ((AnalysisArticleJoinPagerAdapter)analysisArticlesViewPager.getAdapter()).cleanArticleAddedData();
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
