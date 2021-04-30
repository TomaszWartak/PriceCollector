package com.dev4lazy.pricecollector.test_view.other_stores_screen;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.viewmodel.OtherStoreListViewModel;

public class OtherStoresListFragment extends Fragment {

    private OtherStoreListViewModel viewModel;
    private RecyclerView recyclerView;
    private OtherStoreAdapter otherStoreAdapter;

    public static OtherStoresListFragment newInstance() {
        return new OtherStoresListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_stores_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(OtherStoreListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        otherStoreAdapter = new OtherStoreAdapter(new OtherStoreDiffCallback());
        recyclerView = getView().findViewById(R.id.other_stores_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo? getContext()
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(otherStoreAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getStoresLiveData().observe(this, new Observer<PagedList<Store>>() {
            @Override
            public void onChanged(PagedList<Store> storesList) {
                if (!storesList.isEmpty()) {
                    otherStoreAdapter.submitList(storesList);
                }
            }
        });
    }


}
