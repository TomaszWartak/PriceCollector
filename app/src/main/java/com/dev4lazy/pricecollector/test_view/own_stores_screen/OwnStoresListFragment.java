package com.dev4lazy.pricecollector.test_view.own_stores_screen;

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
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.viewmodel.OwnStoreListViewModel;

public class OwnStoresListFragment extends Fragment {

    private OwnStoreListViewModel viewModel;
    private RecyclerView recyclerView;
    private OwnStoreAdapter ownStoreAdapter;

    public static OwnStoresListFragment newInstance() {
        return new OwnStoresListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.own_stores_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(OwnStoreListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        ownStoreAdapter = new OwnStoreAdapter(new OwnStoreDiffCallback());
        recyclerView = getView().findViewById(R.id.own_stores_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(ownStoreAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getOwnStoresLiveData().observe(this, new Observer<PagedList<OwnStore>>() {
            @Override
            public void onChanged(PagedList<OwnStore> storesList) {
                if (!storesList.isEmpty()) {
                    ownStoreAdapter.submitList(storesList);
                }
            }
        });
    }


}
