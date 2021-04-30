package com.dev4lazy.pricecollector.unused;

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
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;

public class DepartmentInSectorsListFragment extends Fragment {

    private DepartmentInSectorListViewModel viewModel;
    private RecyclerView recyclerView;
    private DepartmentInSectorAdapter departmentInSectorAdapter;

    public DepartmentInSectorsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.depts_in_secs_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of( this ).get( DepartmentInSectorListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        departmentInSectorAdapter = new DepartmentInSectorAdapter( new DepartmentInSectorDiffCallback() );
        recyclerView = getView().findViewById( R.id.depts_in_secs_recycler );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) ); // todo ????
        // todo recyclerView.addItemDecoration( new DividerItemDecoration( getActivity(), VERTICAL ));
        recyclerView.setAdapter( departmentInSectorAdapter );
    }

    private void subscribeRecycler() {
        viewModel.getDepartmentInSectorsLiveData().observe( getViewLifecycleOwner(),  new Observer<PagedList<DepartmentInSector>>() {
            @Override
            public void onChanged( PagedList<DepartmentInSector> departmentInSectorsList  ) {
                if (!departmentInSectorsList.isEmpty()) {
                    departmentInSectorAdapter.submitList( departmentInSectorsList);
                }
            }
        });
    }

}