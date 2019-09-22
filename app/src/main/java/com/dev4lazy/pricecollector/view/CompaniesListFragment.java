package com.dev4lazy.pricecollector.view;

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
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.viewmodel.CompanyListViewModel;
import com.dev4lazy.pricecollector.viewmodel.OwnStoreListViewModel;

public class CompaniesListFragment extends Fragment {

    private CompanyListViewModel viewModel;
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;

    public static CompaniesListFragment newInstance() {
        return new CompaniesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comapnies_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CompanyListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        companyAdapter = new CompanyAdapter(new CompanyDiffCallback());
        recyclerView = getView().findViewById(R.id.companies_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(companyAdapter);
    }

    private void subscribeRecycler() {
        viewModel.getCompanysLiveData().observe(this, new Observer<PagedList<Company>>() {
            @Override
            public void onChanged(PagedList<Company> storesList) {
                if (!storesList.isEmpty()) {
                    companyAdapter.submitList(storesList);
                }
            }
        });
    }


}
