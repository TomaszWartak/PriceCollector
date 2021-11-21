package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.BuildConfig;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.AnalysisArticleJoinSaver;
import com.dev4lazy.pricecollector.model.logic.AnalysisArticleJoinValuesStateHolder;
import com.dev4lazy.pricecollector.view.utils.LogoutQuestionDialog;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class AnalysisArticlesListFragment extends Fragment { // OK

    private AnalysisArticleJoinsRecyclerView analysisArticleJoinsRecyclerView;

    private StoreViewModel storeViewModel;
    private AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel;
    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;

    public static AnalysisArticlesListFragment newInstance() {
        return new AnalysisArticlesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_list_fragment, container, false);
        viewModelsSetup();
        setOnBackPressedCallback();
        // TODO XXX setToolbarText();
        recyclerViewSetup( view );
        recyclerViewSubscribtion();
        return view;
    }

        private void viewModelsSetup() {
            storeViewModel =
                    new ViewModelProvider( getActivity() )
                        .get( StoreViewModel.class );
            analysisArticleJoinsListViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinsListViewModel.class );
            analysisArticleJoinViewModel =
                    new ViewModelProvider( getActivity() )
                            .get( AnalysisArticleJoinViewModel.class );
        }

        private void setOnBackPressedCallback() {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    resetViewModels();
                    Navigation.findNavController(getView()).navigate(R.id.action_analysisArticlesListFragment_to_analysisCompetitorsFragment);
                }
            };
            getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        }

        private void resetViewModels() {
            analysisArticleJoinsListViewModel.getSearchArticlesCriteria().clearAll();
            analysisArticleJoinViewModel.setPositionOnList( 0 );
            analysisArticleJoinViewModel.setAnyArticleDisplayed( false );
        }

        private void setToolbarText() {
            String title = storeViewModel.getStore().getName();
            String filtered = " *";
            if (analysisArticleJoinsListViewModel.getSearchArticlesCriteria().isFilterSet()) {
                title = title + filtered;
            }
            ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            toolbar.setTitle(title);
        }

        private void recyclerViewSetup( View view ) {
            analysisArticleJoinsRecyclerView = view.findViewById(R.id.analysis_articles_recycler);
            analysisArticleJoinsRecyclerView.setup();
        }

        private void recyclerViewSubscribtion() {
            AnalyzesListViewModel analyzesListViewModel
                    = new ViewModelProvider( getActivity() ).get( AnalyzesListViewModel.class );
            analysisArticleJoinsListViewModel.buildAnalysisiArticleJoinsPagedList(
                    analyzesListViewModel.getChosenAnalysisId(),
                    storeViewModel.getStore().getId()
            );
            analysisArticleJoinsListViewModel.getAnalysisArticleJoinsListLiveData().observe(
                    getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
                @Override
                public void onChanged( PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                    if (!analysisArticlesJoins.isEmpty()) {
                        analysisArticleJoinsRecyclerView.submitArticlesList( analysisArticlesJoins );
                        // TODO XXX przewinęcie RecyclerView do pozycji ustawionej w ViewPagerze powinno sie inaczej odbywać...
                        //  Bo za każdym razem gdy wychodzę z ViewPagera, to item jest u samej góry, a ja się spodziwewam go
                        //  w innym miejscu (tam gdzie go kliknąłem).
                        //  Natomiast trzeba ustalić zachowanie RecyclerView, kiedy w ViewPager wyjdziesz poza stronę,
                        //  z której otwierałeś Item.
                        //  Proponuję:
                        //  - jesli jestem na tej samej stronie - podświetlić po prostu Itewm, który był wyświetlony w ViewPagerze
                        //  - jesli na innej stronie - podświetlić Item, który był wyświetlony w ViewPagerze i ustawić go na śrdku strony
                        // TODO wypełnij metodę AnalysisArticleJoinsRecyclerView.scrollToItem()
                        // TODO TEST
                        // ((LinearLayoutManager)layoutManager).scrollToPositionWithOffset(positionToScroll - 1,0);
                        // TODO END TEST
                        int positionToScroll = analysisArticleJoinViewModel.getPositionOnList();
                        int firstVisibleItemPosition = analysisArticleJoinViewModel.getFirstVisibleItemPosition();
                        int lastVisibleItemPosition = analysisArticleJoinViewModel.getLastVisibleItemPosition();
                        if (isPositionToScrollBeyondFirstPage( positionToScroll, firstVisibleItemPosition, lastVisibleItemPosition )) {
                            if (isPositionToScrollBeyondLatelyVisiblePage( positionToScroll, firstVisibleItemPosition, lastVisibleItemPosition)) {
                                analysisArticleJoinsRecyclerView.scrollToPosition( getPositionOnMiddleOfPage( positionToScroll, firstVisibleItemPosition, lastVisibleItemPosition ) );
                            } else {
                                analysisArticleJoinsRecyclerView.scrollToPosition( firstVisibleItemPosition );
                            }
                        }
                    }
                }
            });
        }

    private boolean isPositionToScrollBeyondFirstPage(
            int positionToScroll,
            int firstVisibleItemPosition,
            int lastVisibleItemPosition ) {
        // Pozycje są liczone od 0, więc trzeba dodać 1
        return (positionToScroll+1) > getPageHeight( firstVisibleItemPosition, lastVisibleItemPosition );
    }

    private int getPageHeight( int firstVisibleItemPosition,
                              int lastVisibleItemPosition ) {
        return lastVisibleItemPosition-firstVisibleItemPosition+1;
    }

    private boolean isPositionToScrollBeyondLatelyVisiblePage(
            int positionToScroll,
            int firstVisibleItemPosition,
            int lastVisibleItemPosition ) {
        return (positionToScroll<firstVisibleItemPosition) || (positionToScroll>lastVisibleItemPosition);
    }

    public int getPositionOnMiddleOfPage(
            int positionToScroll,
            int firstVisibleItemPosition,
            int lastVisibleItemPosition ) {
        return positionToScroll - (getPageHeight(firstVisibleItemPosition, lastVisibleItemPosition ) / 2);
    }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarText();
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
                        case R.id.analysis_articles_list_screen_gotoanalyzes_menu_item:
                            resetViewModels();
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesListFragment_to_analyzesListFragment );
                            break;
                        case R.id.analysis_articles_list_screen_search_menu_item:
                            Navigation.findNavController( getView() ).navigate( R.id.action_analysisArticlesListFragment_to_searchArticlesFragment );
                            break;
                        case R.id.analysis_articles_list_screen_populate_data:
                            if (BuildConfig.DEBUG) {
                                populateTestData();
                                break;
                            } else {
                                Toast.makeText(
                                    getContext(),
                                    R.string.only_for_testing,
                                    Toast.LENGTH_SHORT).show();
                            }
                        case R.id.analysis_articles_list_screen_logout_menu_item:
                            new LogoutQuestionDialog( getContext(), getActivity() ).get();
                            // TODO XXX getLogoutQuestionDialog();
                            break;
                    }
                    return false;
                }
            });
        }

    private void populateTestData() {
        ArrayList<AnalysisArticleJoin> analysisArticleJoinList =
                (ArrayList<AnalysisArticleJoin>)analysisArticleJoinsListViewModel
                        .getAnalysisArticleJoinsListLiveData()
                        .getValue()
                        .stream()
                        .limit(10)
                        .collect(Collectors.toList());
        Random random = new Random();
        for (int joinIndex = 0; joinIndex<analysisArticleJoinList.size(); joinIndex++ ) {
            AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinList.get(joinIndex);
            if (analysisArticleJoin.isCompetitorStoreIdNotSet()) {
                analysisArticleJoin.setCompetitorStoreId(storeViewModel.getStore().getId());
            }
            AnalysisArticleJoinValuesStateHolder valuesStateHolder = new AnalysisArticleJoinValuesStateHolder( );
            switch (joinIndex) {
                case 0:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 10.0 );
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 11.0 );
                    }
                    break;
                case 1:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 21.0 )
                                .setComments( "Uwagi 21" )
                                .setReferenceArticleName( "Nazwa Ref 21")
                                .setReferenceArticleEanCodeValue( "2000000000001");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 20.0 )
                                .setComments( "Uwagi 20" )
                                .setReferenceArticleName( "Nazwa Ref 20")
                                .setReferenceArticleEanCodeValue( "2000000000000");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 40.0 )
                                .setReferenceArticleName( "Nazwa Ref 40")
                                .setReferenceArticleEanCodeValue( "4000000000000");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 41.0 )
                                .setReferenceArticleName( "Nazwa Ref 41")
                                .setReferenceArticleEanCodeValue( "40000000000010");
                    }
                    break;
                case 4:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 50.0 )
                                .setReferenceArticleName( "Nazwa Ref 50")
                                .setReferenceArticleEanCodeValue( "5000000000000")
                                .setReferenceArticleDescription( "Uwagi ref 50");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 51.0 )
                                .setReferenceArticleName( "Nazwa Ref 51")
                                .setReferenceArticleEanCodeValue( "5000000000001")
                                .setReferenceArticleDescription( "Uwagi ref 51");
                    }
                    break;
                case 5:
                    break;
                case 6:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 71.0 )
                                .setReferenceArticleName( "Nazwa Ref 71")
                                .setReferenceArticleEanCodeValue( "7000000000001");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 71.0 )
                                .setReferenceArticleName( "Nazwa Ref 71")
                                .setReferenceArticleEanCodeValue( "7000000000001");
                    }
                    break;
                case 7:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 80.0 )
                                .setReferenceArticleName( "Nazwa Ref 80")
                                .setReferenceArticleEanCodeValue( "8000000000000");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 81.0 )
                                .setReferenceArticleName( "Nazwa Ref 81")
                                .setReferenceArticleEanCodeValue( "8000000000001");
                    }
                    break;
                case 8:
                    break;
                case 9:
                    if (random.nextBoolean()) {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 101.0 )
                                .setReferenceArticleName( "Nazwa Ref 101")
                                .setReferenceArticleEanCodeValue( "100_000000001");
                    } else {
                        valuesStateHolder
                                .setAnalysisArticleJoin( analysisArticleJoin )
                                .setCompetitorStorePrice( 100.0 )
                                .setReferenceArticleName( "Nazwa Ref 100")
                                .setReferenceArticleEanCodeValue( "100_000000000");
                    }
                    break;
                default:
            }
            new AnalysisArticleJoinSaver(
                    analysisArticleJoinsListViewModel,
                    valuesStateHolder
            ).startSavingDataChain( analysisArticleJoin );
        }
        analysisArticleJoinsRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /* TODO XXX
            private void getLogoutQuestionDialog() {
                new MaterialAlertDialogBuilder(getContext()))
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

}
