package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesListFragment extends Fragment {


    //private AnalysisArticleJoinViewModel viewModel;
    private AnalysisArticleJoinsViewModel viewModel;
    private AnalysisArticleJoinsRecyclerView recyclerView;
    //private AnalysisArticleAdapter analysisArticleJoinAdapter;
    private AnalysisArticleJoinAdapter analysisArticleJoinAdapter;

    public AnalysisArticlesListFragment() {
        // Required empty public constructor
    }

    public static AnalysisArticlesListFragment newInstance() {
        return new AnalysisArticlesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analysis_articles_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        //viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinViewModel.class);
        recyclerSetup();
        recyclerSubscribtion();
    }

    private void recyclerSetup() {
        // analysisArticleJoinAdapter = new AnalysisArticleJoinAdapter(new AnalysisArticleJoinDiffCalback());
        recyclerView = getView().findViewById(R.id.analysis_articles_recycler);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(analysisArticleJoinAdapter); */
        recyclerView.setup();
    }

    private void recyclerSubscribtion() {
        viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinsViewModel.class);
        viewModel.getAnalysisArticleJoinLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    //analysisArticleJoinAdapter.submitList(analysisArticlesJoins);
                    recyclerView.submitArticlesList(analysisArticlesJoins);
                }
            }
        });
    }

}
