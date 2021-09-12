package com.dev4lazy.pricecollector.test_view.remote_articles_screen;

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
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRowJoin;
import com.dev4lazy.pricecollector.remote_view_viewmodel.RemoteAnalysisRowJoinViewModel;

import static android.widget.LinearLayout.VERTICAL;

public class RemoteAnalysisRowJoinFragment extends Fragment {

    private RemoteAnalysisRowJoinViewModel viewModel;
    private RecyclerView recyclerView;
    private RemoteAnalysisRowJoinAdapter remoteAnalysisRowJoinAdapter;
    public static RemoteAnalysisRowJoinFragment newInstance() {
        return new RemoteAnalysisRowJoinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.remote_analysis_row_join_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RemoteAnalysisRowJoinViewModel.class);
        recyclerSetup();
        recyclerSubscribtion();
    }

    private void recyclerSetup() {
        // todo remoteAnalysisRowAdapter = new RemoteAnalysisRowAdapter(new RemoteAnalysisRowDiffCalback());
        remoteAnalysisRowJoinAdapter = new RemoteAnalysisRowJoinAdapter(new RemoteAnalysisRowJoinDiffCallback());
        recyclerView = getView().findViewById(R.id.remote_analysis_rows_join_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(remoteAnalysisRowJoinAdapter);
    }

    private void recyclerSubscribtion() {
        viewModel.getAnalysisRowsJoinLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<RemoteAnalysisRowJoin>>() {
            @Override
            public void onChanged(PagedList<RemoteAnalysisRowJoin> remoteAnalysisRowsJoin) {
                if (!remoteAnalysisRowsJoin.isEmpty()) {
                    remoteAnalysisRowJoinAdapter.submitList(remoteAnalysisRowsJoin);
                }
            }
        });
    }

}
