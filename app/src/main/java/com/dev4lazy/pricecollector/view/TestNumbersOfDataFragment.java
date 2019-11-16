package com.dev4lazy.pricecollector.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.viewmodel.NumbersOfDataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestNumbersOfDataFragment extends Fragment {

    private NumbersOfDataModel viewModel;

    public TestNumbersOfDataFragment() {
        // Required empty public constructor
    }

    public static TestNumbersOfDataFragment newInstance() {
        return new TestNumbersOfDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_number_of_data_fragment, container, false);
        prepareView();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        viewModel = ViewModelProviders.of(this).get(NumbersOfDataModel.class);
        goForData();
    }

    private void prepareView() {

    }

    private void goForData() {
        viewModel.getNumberOfAnalysis().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Analysis );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfAnalysisArticle().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisArticle );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfAnalysisArticleShortData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisArticleShortData );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfAnalysisCompetitorSlot().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisCompetitorSlot );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfArticle().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Article );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfCompany().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Company);
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfCompetitorPrice().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_CompetitorPrice );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfCountry().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Country);
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfDepartment().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Department );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfDepartmentInSector().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_DepartmentInSector );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfEanCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_EanCode );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfFamily().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Family);
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfMarket().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Market );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfModule().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Module );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfOwnArticleInfo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_OwnArticleInfo );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfOwnStore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_OwnStore);
                    textView.setText( value.toString() );
                }
            }
        });
       viewModel.getNumberOfSector().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Sector);
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfStore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Module );
                    textView.setText( value.toString() );
                }
            }
        });
        viewModel.getNumberOfUOProject().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_UOProject );
                    textView.setText( value.toString() );
                }
            }
        });
    }

    /*
    numberOfAnalysis
    numberOfAnalysisArticle
    numberOfAnalysisArticleShortData
    numberOfAnalysisCompetitorSlot
    numberOfArticle
    numberOfCompany
    numberOfCompetitorPrice
    numberOfCountry
    numberOfDepartment
    numberOfDepartmentInSector
    numberOfEanCode
    numberOfFamily
    numberOfMarket
    numberOfModule
    numberOfOwnArticleInfo
    numberOfOwnStore
    numberOfSector
    numberOfStore
    numberOfUOProject
     */
}


