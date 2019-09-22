package com.dev4lazy.pricecollector.csv2pojo_recycler;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.csv2pojo.AnalysisRow;
import com.dev4lazy.pricecollector.csv2pojo.AnalyzesDatabase;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class AnalysisRowViewModel extends AndroidViewModel {

    private LiveData<PagedList<AnalysisRow>> analysisRowsLiveData;

    public AnalysisRowViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, AnalysisRow>  factory = AnalyzesDatabase.getInstance().analysisRowDao().getAllAnalysisRowsPaged();
        LivePagedListBuilder<Integer, AnalysisRow> pagedListBuilder = new LivePagedListBuilder<Integer, AnalysisRow>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<AnalysisRow>> getAnalysisRowsLiveData() {
        return analysisRowsLiveData;
    }
}