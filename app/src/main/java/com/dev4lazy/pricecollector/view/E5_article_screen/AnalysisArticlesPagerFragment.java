package com.dev4lazy.pricecollector.view.E5_article_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.MainActivity;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCalback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {


    private AnalysisArticleJoinsListViewModel viewModel;
    // Niestety nie da się dziedziczyc po ViewPager2, bo jest final.
    // Dlatego implementacja Adaptera została tutaj.
    private ViewPager2 viewPager;
    private AnalysisArticleJoinPagerAdapter analysisArticleJoinPagerAdapter;

    public static AnalysisArticlesPagerFragment newInstance() {
        return new AnalysisArticlesPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_pager_fragment, container, false);
        viewPagerSetup( view );
        viewPagerSubscribtion();
        navigationViewMenuSetup();
        return view;
    }

    private void viewPagerSetup( View view ) {
        analysisArticleJoinPagerAdapter = new AnalysisArticleJoinPagerAdapter( new AnalysisArticleJoinDiffCalback() );
        viewPager = view.findViewById(R.id.analysis_articles_pager);
        viewPager.setAdapter(analysisArticleJoinPagerAdapter);
    }

    private void viewPagerSubscribtion() {
        viewModel = new ViewModelProvider(this).get(AnalysisArticleJoinsListViewModel.class);
        viewModel.getAnalysisArticleJoinsListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    analysisArticleJoinPagerAdapter.submitList(analysisArticlesJoins);
                    // TODO XXX jeśli pojawi się zmianna na liście, to będzie się ustawiac w tym miejscu
                    AnalysisArticleJoinViewModel analysisArticleJoinViewModel =
                            new ViewModelProvider( (MainActivity)viewPager.getContext() ).get( AnalysisArticleJoinViewModel.class );
                    viewPager.setCurrentItem(
                            analysisArticleJoinViewModel.getRecyclerViewPosition(),
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
                    case R.id.article_screen_1:
                        break;
                    case R.id.article_screen_2:
                        break;
                }
                return false;
            }
        });
    }
}
