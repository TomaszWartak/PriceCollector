package com.dev4lazy.pricecollector.test_view.articles_screen;


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
import com.dev4lazy.pricecollector.model.entities.Article;
import com.dev4lazy.pricecollector.viewmodel.ArticleViewModel;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesListFragment extends Fragment {


    private ArticleViewModel viewModel;
    //private ArticleJoinViewModel viewModel;
    private RecyclerView recyclerView;
    private ArticleAdapter analysisArticleAdapter;
    //private ArticleJoinAdapter analysisArticleJoinAdapter;

    public ArticlesListFragment() {
        // Required empty public constructor
    }

    public static ArticlesListFragment newInstance() {
        return new ArticlesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.articles_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        analysisArticleAdapter = new ArticleAdapter(new ArticleDiffCalback());
        //analysisArticleJoinAdapter = new ArticleJoinAdapter(new ArticleJoinDiffCalback());
        recyclerView = getView().findViewById(R.id.articles_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(analysisArticleAdapter);
        //recyclerView.setAdapter(analysisArticleJoinAdapter);
    }

    private void subscribeRecycler() {
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        //viewModel = ViewModelProviders.of(this).get(ArticleJoinViewModel.class);
        viewModel.getArticlesLiveDataPaged().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
        //viewModel.getArticleJoinLiveData().observe(this, new Observer<PagedList<ArticleJoin>>() {
            @Override
            public void onChanged(PagedList<Article> articles) {
                if (!articles.isEmpty()) {
                    analysisArticleAdapter.submitList(articles);
                }
            }
            /*@Override
            public void onChanged(PagedList<ArticleJoin> articlesJoins) {
                if (!articlesJoins.isEmpty()) {
                    analysisArticleJoinAdapter.submitList(articlesJoins);
                }
            }
             */
        });
    }

}
