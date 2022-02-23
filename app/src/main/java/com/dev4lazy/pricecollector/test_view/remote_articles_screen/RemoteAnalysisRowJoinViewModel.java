package com.dev4lazy.pricecollector.test_view.remote_articles_screen;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRowJoin;
import com.dev4lazy.pricecollector.remote_model.db.RemoteDatabase;

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