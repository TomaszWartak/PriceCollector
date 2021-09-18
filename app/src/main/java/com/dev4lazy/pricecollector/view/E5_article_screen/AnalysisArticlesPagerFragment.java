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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {

    private StoreViewModel competitorStoreViewModel;
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
                    // TODO i tutaj jest kicha bo na ekranie wpisałeś cenę, ale ona nie została wpisana do AnalysisArticleJoin...
                    //  w Pager Adapter trzeba ustawić listenery
                    if (analysisArticleJoinViewModel.isNeedToSave() ) {
                        analysisArticleJoinViewModel.setNeedToSave(false);
                        AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
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
                    analysisArticleJoinViewModel.setAnalysisArticleJoinsRecyclerViewPosition( position );
                }
            });
        }
            private void startSavingDataChain(AnalysisArticleJoin analysisArticleJoin ) {
                // TODO !!! najpierw zapisz referenceArticle, bo id jest potrzebne w Competitor price
                // TODO XXX saveReferenceArticle( analysisArticleJoin );
                saveCompetitorPrice( analysisArticleJoin );
                // TODO XXX saveAnalysisArticle( analysisArticleJoin );
            }

                private void saveCompetitorPrice( AnalysisArticleJoin analysisArticleJoin ) {
                    if ( isCompetitorStorePriceInDB(analysisArticleJoin) ) {
                        if ( analysisArticleJoin.isCompetitorStorePriceSet() ) {
                            // Pobierz, uaktualnij i zapisz cenę <- nie trzeba pobierać - dane CompetitorPrice
                            // można skompletoweć z analysisArticleJoin.
                            // startCompetitorPriceUpdatingChain( analysisArticleJoin );
                            CompetitorPrice competitorPrice = prepareCompetitorPriceData( analysisArticleJoin );
                            updateCompetitorPrice( competitorPrice );
                        }
                    } else {
                        if ( analysisArticleJoin.isCompetitorStorePriceSet() ) {
                            // Przygotuj i zapisz cenę
                            CompetitorPrice competitorPrice = prepareCompetitorPriceData( analysisArticleJoin );
                            insertCompetitorPrice( competitorPrice, analysisArticleJoin );
                            // TODO musisz dodać adekwatne dummy CompetitorPrice's dla każdego sklepu konkurenta
                            //  Ale dupa... A co jesli w międyczasie zostanie dodany nowy sklep?
                            //  Jak dla niego dodasz dummy
                        }
                    }

                }

                    private boolean isCompetitorStorePriceInDB(AnalysisArticleJoin analysisArticleJoin ) {
                        if ( analysisArticleJoin.getCompetitorStorePriceId()==null ) {
                            return false;
                        }
                        return analysisArticleJoin.getCompetitorStorePriceId()>-1;
                    }



                    // TODO XXX
                    private void startCompetitorPriceUpdatingChain( AnalysisArticleJoin analysisArticleJoin) {
                        readCompetitorPrice( analysisArticleJoin );
                    };

                        private void readCompetitorPrice( AnalysisArticleJoin analysisArticleJoin ) {
                            MutableLiveData<List<CompetitorPrice>> readResult = new MutableLiveData<>();
                            Observer<List<CompetitorPrice>> readingResultObserver = new Observer<List<CompetitorPrice>>() {
                                @Override
                                public void onChanged(List<CompetitorPrice> competitorPricesList) {
                                    if ((competitorPricesList != null)&&(!competitorPricesList.isEmpty())) {
                                        readResult.removeObserver(this); // this = observer...
                                        CompetitorPrice competitorPrice = competitorPricesList.get(0);
                                        // TODO uaktualnij cenę
                                        updateCompetitorPriceData( competitorPrice, analysisArticleJoin );
                                        analysisArticleJoin.setCompetitorStorePriceId( competitorPrice.getId() );
                                        // TODO Zapisz cenę
                                        updateCompetitorPrice( competitorPrice );
                                    }
                                }
                            };
                            readResult.observeForever(readingResultObserver);
                            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                            localDataRepository.findCompetitorPriceById( analysisArticleJoin.getCompetitorStorePriceId(), readResult );
                        }

                            private CompetitorPrice updateCompetitorPriceData(
                                    CompetitorPrice competitorPrice,
                                    AnalysisArticleJoin analysisArticleJoin ) {
                                competitorPrice.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice() );
                                competitorPrice.setReferenceArticleId( analysisArticleJoin.getReferenceArticleId() );
                                return competitorPrice;
                            }

                        private void updateCompetitorPrice( CompetitorPrice competitorPrice ) {
                            MutableLiveData<Integer> updateResult = new MutableLiveData<>();
                            Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer updatedCount ) {
                                    updateResult.removeObserver(this); // this = observer...
                                    // TODO Kolej na AnalysisArticle
                                }
                            };
                            updateResult.observeForever(updatingResultObserver);
                            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                            localDataRepository.updateCompetitorPrice( competitorPrice, updateResult );

                        }

                        private CompetitorPrice prepareCompetitorPriceData(AnalysisArticleJoin analysisArticleJoin ) {
                            CompetitorPrice competitorPrice = new CompetitorPrice();
                            competitorPrice.setId( analysisArticleJoin.getCompetitorStorePriceId() );
                            competitorPrice.setAnalysisId( analysisArticleJoin.getAnalysisId() );
                            competitorPrice.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
                            competitorPrice.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
                            competitorPrice.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreIdInt() );
                            competitorPrice.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice() );
                            competitorPrice.setReferenceArticleId( analysisArticleJoin.getReferenceArticleIdInt() );
                            return competitorPrice;
                        }

                        private void insertCompetitorPrice(
                                CompetitorPrice competitorPrice,
                                AnalysisArticleJoin analysisArticleJoin) {
                            MutableLiveData<Long> insertResult = new MutableLiveData<>();
                            Observer<Long> insertingResultObserver = new Observer<Long>() {
                                @Override
                                public void onChanged( Long insertedCompetitorPriceId ) {
                                    insertResult.removeObserver(this); // this = observer...
                                    // TODO Uaktualnij analysisArticleJoin
                                    analysisArticleJoin.setCompetitorStorePriceId( insertedCompetitorPriceId.intValue() );
                                    analysisArticlesViewPager.getAdapter().notifyDataSetChanged();
                                    // TODO Kolej na AnalysisArticle i zapisanie zmian wynikajacych z artykułu referencyjneg
                                    //  Czyli jak nie było zmian (id art ref) nie ma potrezeby zapisu...
                                    updateAnalysisArticle( analysisArticleJoin );
                                }
                            };
                            insertResult.observeForever(insertingResultObserver);
                            LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                            localDataRepository.insertCompetitorPrice( competitorPrice, insertResult );

                        }

                private void updateAnalysisArticle(AnalysisArticleJoin analysisArticleJoin ) {
                    AnalysisArticle analysisArticle = prepareAnalysisArticleData( analysisArticleJoin );
                    MutableLiveData<Integer> updateResult = new MutableLiveData<>();
                    Observer<Integer> updatingResultObserver = new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer intResult /* TODO co to za nazwa? */) {
                            updateResult.removeObserver(this); // this = observer...
                        }
                    };
                    updateResult.observeForever(updatingResultObserver);
                    LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
                    localDataRepository.updateAnalysisArticle( analysisArticle, updateResult );
                }

                private AnalysisArticle prepareAnalysisArticleData(AnalysisArticleJoin analysisArticleJoin ) {
                    AnalysisArticle analysisArticle = new AnalysisArticle();
                    analysisArticle.setId( analysisArticleJoin.getAnalysisArticleId());
                    analysisArticle.setAnalysisId( analysisArticleJoin.getAnalysisId() );
                    analysisArticle.setArticleId( analysisArticleJoin.getArticleId() );
                    analysisArticle.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
                    analysisArticle.setArticleStorePrice( analysisArticleJoin.getArticleStorePrice() );
                    analysisArticle.setArticleRefPrice( analysisArticleJoin.getArticleRefPrice() );
                    // analysisArticle.setArticleNewPrice( null );
                    analysisArticle.setReferenceArticleId( analysisArticleJoin.getReferenceArticleIdInt() );
                    // analysisArticle.setCompetitorStorePriceId( analysisArticleJoin.getCompetitorStorePriceId() );
                    return analysisArticle;
                }

                // TODO XXX
                private void saveArticle( AnalysisArticleJoin analysisArticleJoin ) {
                    Article article = getArticle( analysisArticleJoin );
                    // TODO Pobierz Article
                    // TODO Uaktualnij Article
                    // TODO Zapisz Article
                }

                // TODO XXX
                private Article getArticle( AnalysisArticleJoin analysisArticleJoin ) {
                    // todo trzeba pobrać Article i uzupełnić
                    Article article = new Article();
                    return article;
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
                                analysisArticleJoinViewModel.getAnalysisArticleJoinsRecyclerViewPosition(),
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

    /* TODO XXX
    interface ArticleAddedDataCleaner {
        void cleanArticleAddedData();
    }
    */
}
