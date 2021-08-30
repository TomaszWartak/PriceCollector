package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SearchArticlesPagerAdapter extends FragmentStateAdapter {
    final int PAGE_COUNT = 2;
    final static int SEARCH_BY_DATA = 0;
    final static int SEARCH_BY_STRUCTURE = 1;

    public SearchArticlesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case SEARCH_BY_DATA:
                return new SearchArticlesByDataFragment();
            case SEARCH_BY_STRUCTURE:
                return new SearchArticlesByStructureFragment();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }

}