package com.dev4lazy.pricecollector.test_view.countries_screen;

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
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.unused.CountryListViewModel;

public class CountriesListFragment extends Fragment {

    private CountryListViewModel viewModel;
    private RecyclerView recyclerView;
    private CountryAdapter countryAdapter;

    public static CountriesListFragment newInstance() {
        return new CountriesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countries_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CountryListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        countryAdapter = new CountryAdapter(new CountryDiffCallback());
        recyclerView = getView().findViewById(R.id.countries_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(countryAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getCountrysLiveData().observe(this, new Observer<PagedList<Country>>() {
            @Override
            public void onChanged(PagedList<Country> storesList) {
                if (!storesList.isEmpty()) {
                    countryAdapter.submitList(storesList);
                }
            }
        });
    }


}
