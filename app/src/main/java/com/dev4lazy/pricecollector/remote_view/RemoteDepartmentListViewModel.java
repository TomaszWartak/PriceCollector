package com.dev4lazy.pricecollector.remote_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_data.RemoteDepartment;
import com.dev4lazy.pricecollector.remote_data.RemoteDepartmentDao;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class RemoteDepartmentListViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteDepartment>> remoteDepartmentsLiveData;

    public RemoteDepartmentListViewModel(Application application) {
        super(application);
        RemoteDepartmentDao remoteDepartmentDao = AppHandle.getHandle().getRemoteDatabase().departmentDao();
        DataSource.Factory<Integer, RemoteDepartment>  factory = remoteDepartmentDao.getAllPaged();
        LivePagedListBuilder<Integer, RemoteDepartment> pagedListBuilder = new LivePagedListBuilder<Integer, RemoteDepartment>(factory, 50);
        remoteDepartmentsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteDepartment>> getRemoteDepartmentsLiveData() {
        return remoteDepartmentsLiveData;
    }
}

