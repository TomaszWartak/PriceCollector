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
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsViewModel;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesListFragment extends Fragment {


    //private AnalysisArticleJoinViewModel viewModel;
    private AnalysisArticleJoinsViewModel viewModel;
    private RecyclerView recyclerView;
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
        viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinsViewModel.class);
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
