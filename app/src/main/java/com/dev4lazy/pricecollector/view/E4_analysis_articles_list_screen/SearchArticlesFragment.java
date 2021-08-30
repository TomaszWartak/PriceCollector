package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class SearchArticlesFragment extends Fragment {

    public static SearchArticlesFragment newInstance() {
        return new SearchArticlesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_fragment, container, false);
        setupTabLayout( view );
        return view;
    }

    private void setupTabLayout(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.search_articles_viewpager);
        SearchArticlesPagerAdapter searchArticlesPagerAdapter = new SearchArticlesPagerAdapter(this);
        viewPager.setAdapter(searchArticlesPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.search_articles_tabs);
        /**/
        new TabLayoutMediator(
                tabLayout,
                viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case SearchArticlesPagerAdapter.SEARCH_BY_DATA:
                        tab.setText(R.string.articles_search_tab_data_name);
                        // tab.setCustomView( view.findViewById(R.id.search_articles_by_data_tab ) );
                        break;
                    case SearchArticlesPagerAdapter.SEARCH_BY_STRUCTURE:
                        tab.setText(R.string.articles_search_tab_structure_name);
                        // tab.setCustomView( view.findViewById(R.id.search_articles_by_structure_tab ) );
                        break;
                }
            }
        }).attach();
    }

}
