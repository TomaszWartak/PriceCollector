package com.dev4lazy.pricecollector.view;


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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment {


    //private AnalysisArticleViewModel viewModel;
    private AnalysisArticleJoinViewModel viewModel;
    private RecyclerView recyclerView;
    //private AnalysisArticleAdapter analysisArticleJoinAdapter;
    private AnalysisArticleJoinAdapter analysisArticleJoinAdapter;

    public AnalysisFragment() {
        // Required empty public constructor
    }

    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analysis_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        //viewModel = ViewModelProviders.of(this).get(AnalysisArticleViewModel.class);
        viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        analysisArticleJoinAdapter = new AnalysisArticleJoinAdapter(new AnalysisArticleJoinDiffCalback());
        recyclerView = getView().findViewById(R.id.analysis_articles_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(analysisArticleJoinAdapter);
    }

    private void subscribeRecycler() {
        //viewModel.getAnalysisRowsLiveData().observe(this, new Observer<PagedList<AnalysisArticle>>() {
        viewModel.getAnalysisArticleJoinLiveData().observe(this, new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    analysisArticleJoinAdapter.submitList(analysisArticlesJoins);
                }
            }
        });
    }

}
