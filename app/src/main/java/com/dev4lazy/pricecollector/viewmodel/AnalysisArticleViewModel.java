package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;

public class AnalysisArticleViewModel extends AndroidViewModel {

    private LiveData<PagedList<AnalysisArticle>> analysisRowsLiveData;

    public AnalysisArticleViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, AnalysisArticle> factory = LocalDatabase.getInstance().analysisArticleDao().getAllPaged();
        LivePagedListBuilder<Integer, AnalysisArticle> pagedListBuilder = new LivePagedListBuilder<Integer, AnalysisArticle>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<AnalysisArticle>> getAnalysisRowsLiveData() {
        return analysisRowsLiveData;
    }

}