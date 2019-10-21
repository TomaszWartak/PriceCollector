package com.dev4lazy.pricecollector.remote_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteDatabase;

public class RemoteAnalysisRowViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteAnalysisRow>> analysisRowsLiveData;

    public RemoteAnalysisRowViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, RemoteAnalysisRow>  factory = RemoteDatabase.getInstance().analysisRowDao().getAllAnalysisRowsPaged();
        LivePagedListBuilder<Integer, RemoteAnalysisRow> pagedListBuilder = new LivePagedListBuilder<Integer, RemoteAnalysisRow>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteAnalysisRow>> getAnalysisRowsLiveData() {
        return analysisRowsLiveData;
    }
}