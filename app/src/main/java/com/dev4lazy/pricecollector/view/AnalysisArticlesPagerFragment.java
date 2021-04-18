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
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {


    //private AnalysisArticleJoinViewModel viewModel;
    private AnalysisArticleJoinsViewModel viewModel;
    private ViewPager2 viewPager;
    //private AnalysisArticleAdapter analysisArticleJoinPagerAdapter;
    private AnalysisArticleJoinPagerAdapter analysisArticleJoinPagerAdapter;

    public AnalysisArticlesPagerFragment() {
        // Required empty public constructor
    }

    public static AnalysisArticlesPagerFragment newInstance() {
        return new AnalysisArticlesPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analysis_articles_pager_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        //viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinViewModel.class);
        viewPagerSetup();
        subscribeViewPager();
    }

    private void viewPagerSetup() {
        analysisArticleJoinPagerAdapter = new AnalysisArticleJoinPagerAdapter( new AnalysisArticleJoinDiffCalback() );
        viewPager = getView().findViewById(R.id.analysis_articles_pager);
        // viewPager.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // viewPager.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        viewPager.setAdapter(analysisArticleJoinPagerAdapter);
    }

    private void subscribeViewPager() {
        viewModel = ViewModelProviders.of(this).get(AnalysisArticleJoinsViewModel.class);
        viewModel.getAnalysisArticleJoinLiveData().observe(this, new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    analysisArticleJoinPagerAdapter.submitList(analysisArticlesJoins);
                }
            }
        });
    }

}
