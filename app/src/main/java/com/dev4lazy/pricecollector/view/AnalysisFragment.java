package com.dev4lazy.pricecollector.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.csv2pojo.AnalysisRow;
import com.dev4lazy.pricecollector.csv2pojo_recycler.AnalysisRowAdapter;
import com.dev4lazy.pricecollector.csv2pojo_recycler.AnalysisRowDiffCalback;
import com.dev4lazy.pricecollector.csv2pojo_recycler.AnalysisRowFragment;
import com.dev4lazy.pricecollector.csv2pojo_recycler.AnalysisRowViewModel;

import static android.widget.LinearLayout.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisFragment extends Fragment {


    private AnalysisRowViewModel viewModel;
    private RecyclerView recyclerView;
    private AnalysisRowAdapter analysisRowAdapter;
    public AnalysisFragment() {
        // Required empty public constructor
    }

    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analysis_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(AnalysisRowViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        analysisRowAdapter = new AnalysisRowAdapter(new AnalysisRowDiffCalback());
        recyclerView = getView().findViewById(R.id.analysis_articles_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(analysisRowAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getAnalysisRowsLiveData().observe(this, new Observer<PagedList<AnalysisRow>>() {
            @Override
            public void onChanged(PagedList<AnalysisRow> analysisRows) {
                if (!analysisRows.isEmpty()) {
                    analysisRowAdapter.submitList(analysisRows);
                }
            }
        });
    }

}
