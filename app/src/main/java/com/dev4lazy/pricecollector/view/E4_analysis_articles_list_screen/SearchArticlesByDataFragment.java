package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;

import androidx.fragment.app.Fragment;

public class SearchArticlesByDataFragment extends Fragment {

    public static SearchArticlesByDataFragment newInstance() {
        return new SearchArticlesByDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_by_data_fragment, container, false);
        viewSetup(view);
        return view;
    }

    private void viewSetup(View view) {
        view.findViewById(R.id.search_articles_by_data_button_clear).setOnClickListener((View v) -> {
            clearSearchCriteria();
        });
        view.findViewById(R.id.search_articles_by_data_button_search).setOnClickListener((View v) -> {
            searchArticles();
        });
    }

    private void clearSearchCriteria() {

    }

    private void searchArticles() {

    }

}
