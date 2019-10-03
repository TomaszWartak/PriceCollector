package com.dev4lazy.pricecollector.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.viewmodel.StoreListViewModel;

import static android.widget.LinearLayout.VERTICAL;

public class OtherStoresListFragment extends Fragment {

    private StoreListViewModel viewModel;
    private RecyclerView recyclerView;
    private StoreAdapter storeAdapter;

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
        viewModel = ViewModelProviders.of(this).get(StoreListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        storeAdapter = new StoreAdapter(new StoreDiffCallback());
        recyclerView = getView().findViewById(R.id.other_stores_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo? getContext()
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(storeAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getStoresLiveData().observe(this, new Observer<PagedList<Store>>() {
            @Override
            public void onChanged(PagedList<Store> storesList) {
                if (!storesList.isEmpty()) {
                    storeAdapter.submitList(storesList);
                }
            }
        });
    }


}
