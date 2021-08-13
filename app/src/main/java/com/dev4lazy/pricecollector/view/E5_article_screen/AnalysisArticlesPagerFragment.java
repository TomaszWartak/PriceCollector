package com.dev4lazy.pricecollector.view.E5_article_screen;


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
import androidx.viewpager2.widget.ViewPager2;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCalback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticlesPagerFragment extends Fragment {


    private AnalysisArticleJoinsViewModel viewModel;
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
        return view;
    }

    private void viewPagerSetup( View view ) {
        analysisArticleJoinPagerAdapter = new AnalysisArticleJoinPagerAdapter( new AnalysisArticleJoinDiffCalback() );
        viewPager = view.findViewById(R.id.analysis_articles_pager);
        viewPager.setAdapter(analysisArticleJoinPagerAdapter);
    }

    private void viewPagerSubscribtion() {
        viewModel = new ViewModelProvider(this).get(AnalysisArticleJoinsViewModel.class);
        viewModel.getAnalysisArticleJoinLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<AnalysisArticleJoin>>() {
            @Override
            public void onChanged(PagedList<AnalysisArticleJoin> analysisArticlesJoins) {
                if (!analysisArticlesJoins.isEmpty()) {
                    analysisArticleJoinPagerAdapter.submitList(analysisArticlesJoins);
                }
            }
        });
    }

}
