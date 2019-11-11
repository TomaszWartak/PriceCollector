package com.dev4lazy.pricecollector.remote_view;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRowDao;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class RemoteAnalysisRowViewModel extends AndroidViewModel {

    private LiveData<PagedList<RemoteAnalysisRow>> analysisRowsLiveData;

    public RemoteAnalysisRowViewModel(Application application) {
        super(application);
        RemoteAnalysisRowDao analysisRowDao = AppHandle.getHandle().getRemoteDatabase().analysisRowDao();
        DataSource.Factory<Integer, RemoteAnalysisRow>  factory = analysisRowDao.getAllPaged();
        LivePagedListBuilder<Integer, RemoteAnalysisRow> pagedListBuilder = new LivePagedListBuilder<Integer, RemoteAnalysisRow>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<RemoteAnalysisRow>> getAnalysisRowsLiveData() {
        return analysisRowsLiveData;
    }
}