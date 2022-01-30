package com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.model.logic.SearchArticlesCriteria;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinsListViewModel;
import com.dev4lazy.pricecollector.viewmodel.StoreViewModel;
import com.google.android.material.tabs.TabLayout;
// TODO XXX import com.dev4lazy.pricecollector.viewmodel.SearchArticlesCriteriaViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class SearchArticlesByStructureFragment extends Fragment { //OK

    AutoCompleteTextView articleSectorDropdown = null;
    AutoCompleteTextView articleDepartmentDropdown = null;

    AnalysisArticleJoinsListViewModel analysisArticleJoinsListViewModel = null;
    SearchArticlesCriteria searchArticlesCriteria = null;
    StoreViewModel storeViewModel = null;

    public static SearchArticlesByStructureFragment newInstance() {
        return new SearchArticlesByStructureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_articles_by_structure_fragment, container, false);
        viewModelsSetup();
        searchArticlesCriteria = analysisArticleJoinsListViewModel.getSearchArticlesCriteria();
        viewSetup(view);
        return view;
    }

        private void viewModelsSetup() {
            analysisArticleJoinsListViewModel =
                    new ViewModelProvider( getActivity() ).get( AnalysisArticleJoinsListViewModel.class );
            storeViewModel =
                    new ViewModelProvider( getActivity() ).get( StoreViewModel.class );
        }

        private void viewSetup(View view) {
            dropdownsValuesSetup( view );
            buttonsSetup(view);
        }

        private void dropdownsValuesSetup( View view ) {
            articleSectorDropdown = view.findViewById(R.id.search_articles_sector_dropdown);
            articleDepartmentDropdown = view.findViewById(R.id.search_articles_department_dropdown);
            if (searchArticlesCriteria.isStructureFilterSet()) {
                setDropdownValues();
            } else {
                clearDropdowns();
            }
        }

            private void setDropdownValues() {
                articleSectorDropdown.setText( searchArticlesCriteria.getSelectedSector().getName() );
                articleDepartmentDropdown.setText( searchArticlesCriteria.getSelectedDepartment().getName() );
            }

            private void clearDropdowns() {
                articleSectorDropdown.clearListSelection();
                articleDepartmentDropdown.clearListSelection();
                articleSectorDropdown.setText( "" );
                articleDepartmentDropdown.setText( "" );
            }

        private void buttonsSetup(View view) {
            view.findViewById(R.id.search_articles_by_structures_button_clear).setOnClickListener((View v) -> {
                clearDropdowns();
                clearSearchCriteria();
                clearAllTabAsterisk();
                clearToolbarAsterisk();
            });
            view.findViewById(R.id.search_articles_by_structures_button_search).setOnClickListener((View v) -> {
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
        Navigation.findNavController( getView() ).navigate( R.id.action_searchArticlesFragment_to_analysisArticlesListFragment );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dropdownsSetup(view);
    }

    private void dropdownsSetup( View view ) {
        sectorDropdownSetup( view );
        departmentDropdownSetup( view );
    }

    private void sectorDropdownSetup( View view ) {
        MutableLiveData<List<Sector>> result = new MutableLiveData<>();
        Observer<List<Sector>> resultObserver = new Observer<List<Sector>>() {
            @Override
            public void onChanged(List<Sector> sectorList) {
                if ((sectorList != null)&&(!sectorList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    for (Sector sector : sectorList ) {
                        StringBuilder stringBuilder = new StringBuilder(sector.getName());
                        stringBuilder.append( NONE.substring( 0, NONE.length()-stringBuilder.length() ) );
                        sector.setName( stringBuilder.toString() );
                    }
                    sectorList.add( 0, getDummySector() );
                    ArrayAdapter sectorAdapter = new ArrayAdapter( getContext(), R.layout.dropdown_item, sectorList );
                    articleSectorDropdown.setAdapter( sectorAdapter );
                    articleSectorDropdown.setOnItemClickListener(
                        new AdapterView.OnItemClickListener( ) {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                searchArticlesCriteria.setSelectedSector( (Sector)sectorAdapter.getItem( position ) );
                                setTabDataName();
                                setToolbarText();
                            }
                        }

                    );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllSectors(result);
    }

    private final String NONE = "                                        ";

    private Sector getDummySector() {
        Sector sector = new Sector();
        sector.setName( NONE );
        return sector;
    }

    private void departmentDropdownSetup( View view ) {
        MutableLiveData<List<Department>> result = new MutableLiveData<>();
        Observer<List<Department>> resultObserver = new Observer<List<Department>>() {
            @Override
            public void onChanged(List<Department> departmentList) {
                if ((departmentList != null)&&(!departmentList.isEmpty())) {
                    result.removeObserver(this); // this = observer...
                    for (Department department : departmentList) {
                        StringBuilder stringBuilder = new StringBuilder(department.getName());
                        stringBuilder.append( NONE.substring( 0, NONE.length()-stringBuilder.length() ) );
                        department.setName( stringBuilder.toString() );
                    }
                    departmentList.add( 0, getDummyDepartment() );
                    ArrayAdapter departmentAdapter = new ArrayAdapter( getContext(), R.layout.dropdown_item, departmentList );
                    articleDepartmentDropdown.setAdapter( departmentAdapter );
                    articleDepartmentDropdown.setOnItemClickListener(
                            new AdapterView.OnItemClickListener( ) {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    searchArticlesCriteria.setSelectedDepartment( (Department)departmentAdapter.getItem( position ) );
                                    setTabDataName();
                                    setToolbarText();
                                }
                            }

                    );
                }
            }
        };
        result.observeForever(resultObserver);
        AppHandle.getHandle().getRepository().getLocalDataRepository().getAllDepartments(result);
    }

    private Department getDummyDepartment() {
        Department department = new Department();
        department.setName( NONE );
        return department;
    }

        private void setTabDataName() {
            TabLayout tabLayout = getParentFragment().getView().findViewById(R.id.search_articles_tabs);
            TabLayout.Tab tab = tabLayout.getTabAt(SearchArticlesPagerAdapter.SEARCH_BY_STRUCTURE);
            if (searchArticlesCriteria.isStructureFilterSet()) {
                String filtered = "* ";
                tab.setText(filtered+getResources().getString(R.string.articles_search_tab_structure_name));
            } else {
                tab.setText(R.string.articles_search_tab_structure_name);
            }
        }

        private void setToolbarText() {
            String toolbarText = storeViewModel.getStore().getName();
            String filtered = "* ";
            if (searchArticlesCriteria.isFilterSet()) {
                toolbarText = filtered + toolbarText;
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
        setProperHeightOfView();
        clearViewIfCriteriaAreNotSet( );
    }

    private void setProperHeightOfView() {
        View layoutView = getView().findViewById( R.id.search_articles_by_structure_layout );
        if (layoutView!=null) {
            ViewGroup.LayoutParams layoutParams = layoutView.getLayoutParams();
            if (layoutParams!=null) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutView.requestLayout();
            }
        }
    }

    private void clearViewIfCriteriaAreNotSet( ) {
        if ( searchArticlesCriteria.isFilterNotSet() ) {
            clearDropdowns();
        }
    }

}