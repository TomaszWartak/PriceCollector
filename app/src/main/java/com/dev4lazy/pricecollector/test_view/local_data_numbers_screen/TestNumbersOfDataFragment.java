package com.dev4lazy.pricecollector.test_view.local_data_numbers_screen;


import android.graphics.Color;
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
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfAnalysisArticle().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisArticle );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        /* todo ??
        viewModel.getNumberOfAnalysisArticleShortData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisArticleShortData );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    };
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        
         */
        viewModel.getNumberOfAnalysisCompetitorSlot().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_AnalysisCompetitorSlot );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfArticle().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Article );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfCompany().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Company);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfCompetitorPrice().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_CompetitorPrice );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfCountry().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Country);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfDepartment().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Department );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfDepartmentInSector().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_DepartmentInSector );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfEanCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_EanCode );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfFamily().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Family);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfMarket().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Market );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfModule().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Module );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfOwnArticleInfo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_OwnArticleInfo );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfOwnStore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_OwnStore);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
       viewModel.getNumberOfSector().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Sector);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfStore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_Store );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfUOProject().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_UOProject );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
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


