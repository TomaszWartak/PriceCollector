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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteSectorsListFragment extends Fragment {

    private RemoteSectorListViewModel viewModel;
    private RecyclerView recyclerView;
    private RemoteSectorAdapter remoteSectorAdapter;
    public RemoteSectorsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_sectors_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RemoteSectorListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        remoteSectorAdapter = new RemoteSectorAdapter(new RemoteSectorDiffCallback());
        recyclerView = getView().findViewById(R.id.remote_sectors_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(remoteSectorAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getRemoteSectorsLiveData().observe(this, new Observer<PagedList<RemoteSector>>() {
            @Override
            public void onChanged(PagedList<RemoteSector> remoteSectorsList ) {
                if (!remoteSectorsList.isEmpty()) {
                    remoteSectorAdapter.submitList(remoteSectorsList);
                }
            }
        });
    }


}
