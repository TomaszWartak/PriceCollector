package com.dev4lazy.pricecollector.test_view.remote_users_screen;

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
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;

public class RemoteUsersListFragment extends Fragment {

    private RemoteUserListViewModel viewModel;
    private RecyclerView recyclerView;
    private RemoteUserAdapter remoteUserAdapter;

    public static RemoteUsersListFragment newInstance() {
        return new RemoteUsersListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remote_users_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RemoteUserListViewModel.class);
        recyclerSetup();
        recyclerSubscribtion();
    }

    private void recyclerSetup() {
        remoteUserAdapter = new RemoteUserAdapter(new RemoteUserDiffCallback());
        recyclerView = getView().findViewById(R.id.remote_users_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // todo ????
        // todo recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setAdapter(remoteUserAdapter);
    }

    private void recyclerSubscribtion() {
        viewModel.getRemoteUsersLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<RemoteUser>>() {
            @Override
            public void onChanged(PagedList<RemoteUser> remoteUsersList ) {
                if (!remoteUsersList.isEmpty()) {
                    remoteUserAdapter.submitList(remoteUsersList);
                }
            }
        });
    }


}
