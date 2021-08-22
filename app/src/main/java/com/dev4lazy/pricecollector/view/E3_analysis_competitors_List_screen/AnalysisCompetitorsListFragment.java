package com.dev4lazy.pricecollector.view.E3_analysis_competitors_List_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.logic.CompetitorSlotFullData;
import com.dev4lazy.pricecollector.viewmodel.CompetitorsSlotsViewModel;

import java.util.ArrayList;

public class AnalysisCompetitorsListFragment extends Fragment {

    private CompetitorsSlotsListView competitorsSlotsListView;
    private CompetitorsSlotsViewModel viewModel;

    public static AnalysisCompetitorsListFragment newInstance() {
        return new AnalysisCompetitorsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analysis_competitors_fragment, container, false);
        listViewSetup(view);
        listViewSubscribtion();
        return view;
    }

    private void listViewSetup(View view) {
        competitorsSlotsListView = view.findViewById(R.id.analysis_competitors_listview);
        competitorsSlotsListView.setup();
    }

    private void listViewSubscribtion() {
        // todo askForSlots();
        viewModel = new ViewModelProvider(this).get(CompetitorsSlotsViewModel.class);
        viewModel.getCompetitorsSlotsLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<CompetitorSlotFullData>>() {
            @Override
            public void onChanged(ArrayList<CompetitorSlotFullData> competitorSlotFullData) {
                if (!competitorSlotFullData.isEmpty()) {
                    competitorsSlotsListView.submitCompetitorsSlotsList(competitorSlotFullData);
                }
            }
        });
    }

}