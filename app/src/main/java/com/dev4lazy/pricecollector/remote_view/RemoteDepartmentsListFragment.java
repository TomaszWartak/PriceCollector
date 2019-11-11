package com.dev4lazy.pricecollector.remote_view;


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
import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteDepartmentsListFragment extends Fragment {

    private RemoteDepartmentListViewModel viewModel;
    private RecyclerView recyclerView;
    private RemoteDepartmentAdapter remoteDepartmentAdapter;

    public RemoteDepartmentsListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_departments_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of( this ).get( RemoteDepartmentListViewModel.class);
        recyclerSetup();
        subscribeRecycler();
    }

    private void recyclerSetup() {
        remoteDepartmentAdapter = new RemoteDepartmentAdapter( new RemoteDepartmentDiffCallback() );
        recyclerView = getView().findViewById( R.id.departments_recycler );
        recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) ); // todo ????
        // todo recyclerView.addItemDecoration( new DividerItemDecoration( getActivity(), VERTICAL ));
        recyclerView.setAdapter( remoteDepartmentAdapter );
    }

    private void subscribeRecycler() {
        viewModel.getRemoteDepartmentsLiveData().observe( this,  new Observer<PagedList<RemoteDepartment>>() {
            @Override
            public void onChanged( PagedList<RemoteDepartment> remoteDepartmentsList  ) {
                if (!remoteDepartmentsList.isEmpty()) {
                    remoteDepartmentAdapter.submitList( remoteDepartmentsList);
                }
            }
        });
    }

}