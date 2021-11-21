package com.dev4lazy.pricecollector.view.E5_article_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.logic.AnalysisArticleJoinSaver;
import com.dev4lazy.pricecollector.model.logic.AnalysisArticleJoinValuesStateHolder;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.navigation.NavigationView;

public class AnalysisArticlesPagerFragment extends Fragment { // OK

    private StoreViewModel competitorStoreViewModel;
    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;
    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
    private AnalysisArticleJoinSaver analysisArticleJoinSaver;
    // Niestety nie da się dziedziczyc po ViewPager2, bo jest final.
    // Dlatego implementacja Adaptera została tutaj.
    private ViewPager2 analysisArticlesViewPager;
    private AnalysisArticleJoinPagerAdapter analysisArticleJoinPagerAdapter;

    public static AnalysisArticlesPagerFragment newInstance() {
        return new AnalysisArticlesPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_pager_fragment, container, false);
        viewModelsSetup();
        analysisArticleJoinSaver = new AnalysisArticleJoinSaver(
                analysisArticleJoinsListViewModel,
                analysisArticleJoinViewModel.getValuesStateHolder()
        );
        viewPagerSetup( view );
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
                /*
                This method will be invoked when a new page becomes selected. Animation is not necessarily complete.
                Params:
                    position – Position index of the new selected page.
                */
                @Override
                public void onPageSelected(int position) {
                    // The position gives the index of newly selected page
                    super.onPageSelected(position);
                    AnalysisArticleJoin analysisArticleJoin;
                    analysisArticleJoin = analysisArticleJoinPagerAdapter.getCurrentList().get(position);
                    analysisArticleJoinViewModel.setAnalysisArticleJoin( analysisArticleJoin );
                    analysisArticleJoinViewModel.setPositionOnList( position );
                    if (analysisArticleJoinViewModel.isNotToRestoreAfterEanValueDupliaction() ) {
                        analysisArticleJoinViewModel.setAnalysisArticleJoinForRestore( analysisArticleJoin );
                    } else {
                        restoreReferenceArticleEanCodeValue();
                        refreshPagerAdapter();
                    }
                    setToolbarText( analysisArticleJoin.getArticleName() );
                }

                /*
                This method will be invoked when the current page is scrolled, either as part of a programmatically initiated smooth scroll or a user initiated touch scroll.
                Params:
                position – Position index of the first page currently being displayed. Page position+1 will be visible if positionOffset is nonzero.
                positionOffset – Value from [0, 1) indicating the offset from the page at position.
                positionOffsetPixels – Value in pixels indicating the offset from position.
                */
                @Override
                public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ) {
                    super.onPageScrolled( position, positionOffset, positionOffsetPixels );
                    if (analysisArticleJoinViewModel.isNotToRestoreAfterEanValueDupliaction()) {
                        if (analysisArticleJoinViewModel.isNeedToSave()) {
                            analysisArticleJoinViewModel.clearNeedToSave();
                            int positionScrolledFrom = analysisArticleJoinViewModel.getPositionOnList();
                            PagedList<AnalysisArticleJoin> pagedList = analysisArticleJoinPagerAdapter.getCurrentList();
                            AnalysisArticleJoin analysisArticleJoinScrolledFrom = pagedList.get(positionScrolledFrom);
                            if (analysisArticleJoinScrolledFrom.isCompetitorStoreIdNotSet()) {
                                analysisArticleJoinScrolledFrom.setCompetitorStoreId(competitorStoreViewModel.getStore().getId());
                            }
                            validateAndStartSavingDataScroledFrom(
                                    analysisArticleJoinScrolledFrom,
                                    positionScrolledFrom);
                        }
                    } else {
                        analysisArticleJoinViewModel.setToRestoreAfterEanValueDupliaction(false);
                    }
                }

                /*
                Called when the scroll state changes.
                Useful for discovering when the user begins dragging,
                when a fake drag is started,
                when the pager is automatically settling to the current page, or
                when it is fully stopped/idle.
                State can be one of
                SCROLL_STATE_IDLE -
                    Indicates that the ViewPager2 is in an idle, settled state.
                    The current page is fully in view and no animation is in progress.,
                SCROLL_STATE_DRAGGING -
                    Indicates that the ViewPager2 is currently being dragged by the user,
                    or programmatically via fake drag functionality.
                SCROLL_STATE_SETTLING -
                    Indicates that the ViewPager2 is in the process of settling to a final position.

                public void onPageScrollStateChanged(@ScrollState int state) {
                }

                */

            });
        }

    public void restoreReferenceArticleEanCodeValue() {
        analysisArticleJoinViewModel.restoreReferenceArticleEanCodeValue();
        AnalysisArticleJoinValuesStateHolder valuesStateHolder =
                analysisArticleJoinViewModel.getValuesStateHolder();
        valuesStateHolder.clearFlagReferenceArticleEanChanged();
        valuesStateHolder.setFlagNeedToSave( valuesStateHolder.isAnyDataChangedFlagSet() );
    }

    public void refreshPagerAdapter() {
        AnalysisArticleJoinPagerAdapter analysisArticleJoinPagerAdapter =
                ((AnalysisArticleJoinPagerAdapter)analysisArticlesViewPager.getAdapter());
        analysisArticleJoinPagerAdapter.notifyDataSetChanged();
    }

            private void validateAndStartSavingDataScroledFrom(
                    AnalysisArticleJoin analysisArticleJoin,
                    int positionToBack ) {
                AnalysisArticleJoinValuesStateHolder valuesStateHolder =
                        analysisArticleJoinViewModel.getValuesStateHolder();
                if (valuesStateHolder.isReferenceArticleEanChangedFlagSet()) {
                    checkEanCodeForDuplication_IfNot_StartSavingDataChain(
                            analysisArticleJoin,
                            positionToBack);
                } else {
                    analysisArticleJoinSaver.startSavingDataChain( analysisArticleJoin );
                }
            }

    private void checkEanCodeForDuplication_IfNot_StartSavingDataChain(
            AnalysisArticleJoin analysisArticleJoin,
            int positionToBack) {
        AnalysisArticleJoin tempAnalysisArticleJoinToRestore =
                analysisArticleJoinViewModel.getCopyOfAnalysisArticleJoinForRestore();
        AnalysisArticleJoinValuesStateHolder tempAnalysisArticleJoinValuesStateHolder =
                analysisArticleJoinViewModel.getCopyOfValuesStateHolder();
        MutableLiveData<Integer> countEanCodeWithValueResult = new MutableLiveData<>();
        Observer<Integer> resultObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer eanCodesCount) {
                // Jeśli eanCodesCount > 0, to znaczy, że taki ean już istnieje w BD.
                // Czyli nie można dodać tego Eana...
                if (eanCodesCount>0) {
                    String duplicationMessage =
                            getString(R.string.ean_duplication_message_part1) +
                            analysisArticleJoin.getReferenceArticleEanCodeValue() +
                            getString(R.string.ean_duplication_message_part2);
                    showMessage( duplicationMessage );
                    analysisArticleJoinViewModel.setToRestoreAfterEanValueDupliaction( true );
                    analysisArticleJoinViewModel.setValuesStateHolder( tempAnalysisArticleJoinValuesStateHolder );
                    analysisArticleJoinViewModel.setAnalysisArticleJoinForRestore( tempAnalysisArticleJoinToRestore );
                    backToPage(positionToBack);
                } else {
                    analysisArticleJoinSaver.startSavingDataChain( analysisArticleJoin );
                }
            }
        };
        countEanCodeWithValueResult.observeForever( resultObserver );
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        localDataRepository.countEanCodesWithValue(
                analysisArticleJoin.getReferenceArticleEanCodeValue(),
                countEanCodeWithValueResult
        );
    }

            private void showMessage( String validationMessage ) {
                Toast.makeText(
                        getContext(),
                        validationMessage,
                        Toast.LENGTH_SHORT).show();
            };

            private void backToPage( int positionToBack ) {
                analysisArticlesViewPager.setCurrentItem( positionToBack, false );
            };

    private void setToolbarText( String toolbarText ) {
            /*
            int maxLength = toolbarText.length();
            if (maxLength>24) { // TODO hardcoded
                maxLength=24;
            }
            toolbarText = toolbarText.substring(0,maxLength);
            */
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
        }

        private void viewPagerSubscribtion( AnalysisArticleJoinViewModel analysisArticleJoinViewModel ) {
            analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
                @Override
                public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                    if (!analysisArticlesJoins.isEmpty()) {
                        analysisArticleJoinPagerAdapter.submitList(analysisArticlesJoins);
                        analysisArticlesViewPager.setCurrentItem(
                                analysisArticleJoinViewModel.getPositionOnList(),
                                false
                        );
                    }
                }
            });
        }


    @Override
    public void onStart() {
        super.onStart();
        setToolbarText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getArticleName() );
        navigationViewMenuSetup();
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
                    case R.id.article_screen_gotoanalyzes_menu_item:
                        analysisArticleJoinsListViewModel.getSearchArticlesCriteria().clearAll();
                        analysisArticleJoinViewModel.setPositionOnList(0);
                        Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesPagerFragment_to_analyzesListFragment );
                        break;
                    case R.id.article_screen_clear_competitor_article_data_item:
                        clearCompetitorArticleData();
                        break;
                    case R.id.article_screen_logout_menu_item:
                        new LogoutQuestionDialog( getContext(), getActivity() ).get();
                        // TODO XXX getLogoutQuestionDialog();
                        break;
                }
                return false;
            }
        });
    }

    private void clearCompetitorArticleData() {
        ((AnalysisArticleJoinPagerAdapter)analysisArticlesViewPager.getAdapter()).clearCompetitorArticleDataOnScreen();
        clearCompetitorArticleDataInAnalysisArticleJoin();
    }

    private void clearCompetitorArticleDataInAnalysisArticleJoin() {
        // Own Article
        AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinViewModel.getAnalysisArticleJoin();
        analysisArticleJoin.setComments( "" );
        analysisArticleJoin.setCompetitorStorePrice( null );
        // Ref Article
        analysisArticleJoin.setReferenceArticleName( "" );
        analysisArticleJoin.setReferenceArticleEanCodeValue( "" );
        analysisArticleJoin.setReferenceArticleDescription( "" );
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

    /*
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

}
