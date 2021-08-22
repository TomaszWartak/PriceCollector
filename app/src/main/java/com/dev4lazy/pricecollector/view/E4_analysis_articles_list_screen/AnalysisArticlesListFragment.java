package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;

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

}
