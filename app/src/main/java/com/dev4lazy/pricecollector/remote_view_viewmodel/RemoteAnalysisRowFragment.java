package com.dev4lazy.pricecollector.remote_view_viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;

import static android.widget.LinearLayout.VERTICAL;

public class RemoteAnalysisRowFragment extends Fragment {

    private RemoteAnalysisRowViewModel viewModel;
    private RecyclerView recyclerView;
    private RemoteAnalysisRowAdapter remoteAnalysisRowAdapter;
    public static RemoteAnalysisRowFragment newInstance() {
        return new RemoteAnalysisRowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.remote_analysis_row_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RemoteAnalysisRowViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        remoteAnalysisRowAdapter = new RemoteAnalysisRowAdapter(new RemoteAnalysisRowDiffCalback());
        recyclerView = getView().findViewById(R.id.remote_analysis_rows_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(remoteAnalysisRowAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getAnalysisRowsLiveData().observe(this, new Observer<PagedList<RemoteAnalysisRow>>() {
            @Override
            public void onChanged(PagedList<RemoteAnalysisRow> remoteAnalysisRows) {
                if (!remoteAnalysisRows.isEmpty()) {
                    remoteAnalysisRowAdapter.submitList(remoteAnalysisRows);
                }
            }
        });
    }

}
