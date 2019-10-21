package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleViewModel;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment {


    private AnalysisArticleViewModel viewModel;
    private RecyclerView recyclerView;
    private AnalysisArticleAdapter analysisArticleAdapter;

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
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AnalysisArticleViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        analysisArticleAdapter = new AnalysisArticleAdapter(new AnalysisArticleDiffCalback());
        recyclerView = getView().findViewById(R.id.analysis_articles_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(analysisArticleAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getAnalysisRowsLiveData().observe(this, new Observer<PagedList<AnalysisArticle>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticle> analysisArticles) {
                if (!analysisArticles.isEmpty()) {
                    analysisArticleAdapter.submitList(analysisArticles);
                }
            }
        });
    }

}
