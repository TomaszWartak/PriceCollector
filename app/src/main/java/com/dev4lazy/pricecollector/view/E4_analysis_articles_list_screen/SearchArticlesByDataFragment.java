package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.SearchArticlesCriteria;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.dev4lazy.pricecollector.viewmodel.UserViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class SearchArticlesByDataFragment extends Fragment {

    EditText articleNameEditText = null;
    EditText articleEANEditText = null;
    EditText articleSKUEditText = null;
    EditText articleAnyTextEditText = null;

    AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel = null;
    StoreViewModel storeViewModel = null;
    SearchArticlesCriteria searchArticlesCriteria = null;


    public static SearchArticlesByDataFragment newInstance() {
        return new SearchArticlesByDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_by_data_fragment, container, false);
        viewModelsSetup();
        searchArticlesCriteria = analysisArticleJoinsListViewModel.getSearchArticlesCriteria();
        viewSetup( view );
        return view;
    }

    private void viewModelsSetup() {
        analysisArticleJoinsListViewModel =
                new ViewModelProvider( getActivity() ).get( AnalysisArticleJoinsListViewModel.class );
        storeViewModel =
                new ViewModelProvider( getActivity() ).get( StoreViewModel.class );
    }

    private void viewSetup(View view) {
        editTextsSetup( view );
        buttonsSetup( view );
    }

    private void editTextsSetup(View view) {
        articleNameEditTextSetup(view);
        articleEANEditTextSetup(view);
        articleSKUEditTextSetup(view);
        articleAnyTextEditTextSetup(view);
        if (searchArticlesCriteria.isDataFilterSet()) {
            setEditTexts();
        } else {
            clearEditTexts();
        }
    }

    private void articleAnyTextEditTextSetup(View view) {
        articleAnyTextEditText = view.findViewById(R.id.search_articles_any_text_edit_text);
        articleAnyTextEditText.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchArticlesCriteria.setArticleAnyText( articleAnyTextEditText.getText().toString() );
                    setTabDataName();
                    setToolbarText();
                }
            }
        } );
    }

    private void articleSKUEditTextSetup(View view) {
        articleSKUEditText = view.findViewById(R.id.search_articles_sku_edit_text);
        articleSKUEditText.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchArticlesCriteria.setArticleSKU( articleSKUEditText.getText().toString() );
                    setTabDataName();
                    setToolbarText();
                }
            }
        } );
    }

    private void articleEANEditTextSetup(View view) {
        articleEANEditText = view.findViewById(R.id.search_articles_ean_edit_text);
        articleEANEditText.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchArticlesCriteria.setArticleEAN( articleEANEditText.getText().toString() );
                    setTabDataName();
                    setToolbarText();
                }
            }
        } );
    }

    private void articleNameEditTextSetup(View view) {
        articleNameEditText = view.findViewById(R.id.search_articles_article_name_edit_text);
        articleNameEditText.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchArticlesCriteria.setArticleName( articleNameEditText.getText().toString() );
                    setTabDataName();
                    setToolbarText();
                }
            }
        } );
    }

    private void setEditTexts() {
            articleNameEditText.setText( searchArticlesCriteria.getArticleName() );
            articleEANEditText.setText( searchArticlesCriteria.getArticleEAN() );
            articleSKUEditText.setText( searchArticlesCriteria.getArticleSKU() );
            articleAnyTextEditText.setText( searchArticlesCriteria.getArticleAnyText() );
        }

        private void clearEditTexts() {
            articleNameEditText.setText( "" );
            articleEANEditText.setText( "" );
            articleSKUEditText.setText( "" );
            articleAnyTextEditText.setText( "" );

        }

    private void buttonsSetup( View view) {
        view.findViewById(R.id.search_articles_by_data_button_clear).setOnClickListener((View v) -> {
            clearEditTexts();
            clearSearchCriteria();
            clearAllTabAsterisk();
            clearToolbarAsterisk();
        });
        view.findViewById(R.id.search_articles_by_data_button_search).setOnClickListener((View v) -> {
            searchArticles();
        });
    }

        private void clearSearchCriteria() {
            searchArticlesCriteria.clearAll();
        }

        private void clearAllTabAsterisk() {
            TabLayout tabLayout = getParentFragment().getView().findViewById(R.id.search_articles_tabs);
            TabLayout.Tab tab = tabLayout.getTabAt(SearchArticlesPagerAdapter.SEARCH_BY_DATA);
            tab.setText(R.string.articles_search_tab_data_name);
            tab = tabLayout.getTabAt(SearchArticlesPagerAdapter.SEARCH_BY_STRUCTURE);
            tab.setText(R.string.articles_search_tab_structure_name);
        }

        private void clearToolbarAsterisk() {
            setToolbarText();
        }

        private void searchArticles( ) {
            setSearchByDataCriteria();
            Navigation.findNavController( getView() ).navigate( R.id.action_searchArticlesFragment_to_analysisArticlesListFragment );
        }

            private void setSearchByDataCriteria() {
                searchArticlesCriteria.setArticleName( articleNameEditText.getText().toString() );
                searchArticlesCriteria.setArticleEAN( articleEANEditText.getText().toString() );
                searchArticlesCriteria.setArticleSKU( articleSKUEditText.getText().toString() );
                searchArticlesCriteria.setArticleAnyText( articleAnyTextEditText.getText().toString() );
            }

        private void setTabDataName() {
            // TODO niestety getView daje bieżący fragment a potrzebujemy SearchArtriclesFragment
            TabLayout tabLayout = getParentFragment().getView().findViewById(R.id.search_articles_tabs);
            TabLayout.Tab tab = tabLayout.getTabAt(SearchArticlesPagerAdapter.SEARCH_BY_DATA);
            if (searchArticlesCriteria.isDataFilterSet()) {
                tab.setText(getResources().getString(R.string.articles_search_tab_data_name)+" *");
            } else {
                tab.setText(R.string.articles_search_tab_data_name);
            }
        }

        private void setToolbarText() {
            String toolbarText = storeViewModel.getStore().getName();
            /* TODO XXX
            int maxLength = toolbarText.length();
            if (maxLength>24) {
                maxLength=24;
            }
            toolbarText = toolbarText.substring(0,maxLength);
             */
            String filtered = " *";
            if (searchArticlesCriteria.isFilterSet()) {
                toolbarText = toolbarText + filtered;
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( toolbarText );
        }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarText();
    }

    @Override
    public void onResume() {
        super.onResume();
        clearViewIfCriteriaAreNotSet( );
    }

    private void clearViewIfCriteriaAreNotSet( ) {
        if ( searchArticlesCriteria.isFilterNotSet() ) {
            clearEditTexts();
        }
    }

}
