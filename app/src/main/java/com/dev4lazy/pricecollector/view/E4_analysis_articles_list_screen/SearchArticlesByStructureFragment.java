package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.viewmodel.SearchArticlesViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SearchArticlesByStructureFragment extends Fragment {

    public static SearchArticlesByStructureFragment newInstance() {
        return new SearchArticlesByStructureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_by_structure_fragment, container, false);
        viewSetup(view);
        return view;
    }

    private void viewSetup(View view) {
        view.findViewById(R.id.search_articles_by_structures_button_clear).setOnClickListener((View v) -> {
            clearSearchCriteria();
        });
        view.findViewById(R.id.search_articles_by_structures_button_search).setOnClickListener((View v) -> {
            searchArticles();
        });
    }

    private void clearSearchCriteria() {
        SearchArticlesViewModel searchArticlesViewModel = new ViewModelProvider( this ).get( SearchArticlesViewModel.class );
        searchArticlesViewModel.clearAllData();
    }

    private void searchArticles() {
        SearchArticlesViewModel searchArticlesViewModel = new ViewModelProvider( this )
                .get( SearchArticlesViewModel.class );
        searchArticlesViewModel.setArticleSectorId( -1 );
        searchArticlesViewModel.setArticleDepartmentId( -2 );
    }


}