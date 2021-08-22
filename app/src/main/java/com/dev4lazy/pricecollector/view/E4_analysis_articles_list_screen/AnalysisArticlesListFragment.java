package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;


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

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.google.android.material.navigation.NavigationView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesListFragment extends Fragment {

    private AnalysisArticleJoinsRecyclerView analysisArticleJoinsRecyclerView;
    private AnalysisArticleJoinsListViewModel viewModel;

    public static AnalysisArticlesListFragment newInstance() {
        return new AnalysisArticlesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_articles_list_fragment, container, false);
        recyclerViewSetup( view );
        recyclerViewSubscribtion();
        navigationViewMenuSetup();
        return view;
    }

    private void recyclerViewSetup( View view ) {
        analysisArticleJoinsRecyclerView = view.findViewById(R.id.analysis_articles_recycler);
        analysisArticleJoinsRecyclerView.setup();
    }

    private void recyclerViewSubscribtion() {
        viewModel = new ViewModelProvider(this).get( AnalysisArticleJoinsListViewModel.class );
        viewModel.getAnalysisArticleJoinsListLiveData().observe( getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged( PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    analysisArticleJoinsRecyclerView.submitArticlesList( analysisArticlesJoins );
                }
            }
        });
    }

    private void navigationViewMenuSetup() {
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
                    case R.id.anlysis_articles_list_screen_1:
                        break;
                    case R.id.anlysis_articles_list_screen_2:
                        break;
                }
                return false;
            }
        });
    }
}
