package com.dev4lazy.pricecollector.remote_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRowJoin;
import com.dev4lazy.pricecollector.remote_data.RemoteDatabase;

public class RemoteAnalysisRowJoinViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteAnalysisRowJoin>> analysisRowsLiveData;

    public RemoteAnalysisRowJoinViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, RemoteAnalysisRowJoin>  factory = RemoteDatabase.getInstance().remoteAnalysisRowDao().getAllRemoteAnalysisRowJoinPaged();
        LivePagedListBuilder<Integer, RemoteAnalysisRowJoin> pagedListBuilder = new LivePagedListBuilder<>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteAnalysisRowJoin>> getAnalysisRowsJoinLiveData() {
        return analysisRowsLiveData;
    }
}