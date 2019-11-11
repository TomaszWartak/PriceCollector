package com.dev4lazy.pricecollector.remote_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_data.RemoteSector;
import com.dev4lazy.pricecollector.remote_data.RemoteSectorDao;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class RemoteSectorListViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteSector>> remoteSectorsLiveData;

    public RemoteSectorListViewModel(Application application) {
        super(application);
        RemoteSectorDao remoteSectorDao = AppHandle.getHandle().getRemoteDatabase().sectorDao();
        DataSource.Factory<Integer, RemoteSector>  factory = remoteSectorDao.getAllPaged();
        LivePagedListBuilder<Integer, RemoteSector> pagedListBuilder = new LivePagedListBuilder<Integer, RemoteSector>(factory, 50);
        remoteSectorsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteSector>> getRemoteSectorsLiveData() {
        return remoteSectorsLiveData;
    }
}

