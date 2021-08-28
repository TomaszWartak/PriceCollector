package com.dev4lazy.pricecollector.remote_view_viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;
import com.dev4lazy.pricecollector.remote_model.db.RemoteUserDao;
import com.dev4lazy.pricecollector.AppHandle;

public class RemoteUserListViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteUser>> remoteUsersLiveData;

    public RemoteUserListViewModel(Application application) {
        super(application);
        RemoteUserDao remoteUserDao = AppHandle.getHandle().getRemoteDatabase().remoteUserDao();
        DataSource.Factory<Integer, RemoteUser>  factory = remoteUserDao.getAllPaged();
        LivePagedListBuilder<Integer, RemoteUser> pagedListBuilder = new LivePagedListBuilder<Integer, RemoteUser>(factory, 50);
        remoteUsersLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteUser>> getRemoteUsersLiveData() {
        return remoteUsersLiveData;
    }
}
