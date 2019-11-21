package com.dev4lazy.pricecollector.view;


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
import com.dev4lazy.pricecollector.viewmodel.NumbersOfDataModel2;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestNumbersOfDataFragment2 extends Fragment {

    private NumbersOfDataModel2 viewModel;

    public TestNumbersOfDataFragment2() {
        // Required empty public constructor
    }

    public static TestNumbersOfDataFragment2 newInstance() {
        return new TestNumbersOfDataFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_number_of_data_fragment2, container, false);
        prepareView();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        viewModel = ViewModelProviders.of(this).get(NumbersOfDataModel2.class);
        goForData();
    }

    private void prepareView() {

    }

    private void goForData() {
        viewModel.getNumberOfRemoteAnalysis().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteAnalysis );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteAnalysisRow().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteAnalysisRow );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteDepartment().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteDepartment );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteEanCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteEanCode );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteFamily().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteFamily);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteMarket().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteMarket );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteModule().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteModule );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
       viewModel.getNumberOfRemoteSector().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteSector);
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteUOProject().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteUOProject );
                    if (value>0) {
                        textView.setTextColor( Color.BLUE );
                    }
                    textView.setText( textView.getText()+value.toString() );
                }
            }
        });
        viewModel.getNumberOfRemoteUser().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged( Integer value ) {
                if (value!=null) {
                    TextView textView = getView().findViewById( R.id.number_of_RemoteUser );
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


