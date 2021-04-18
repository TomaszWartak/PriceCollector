package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinsViewModel extends AndroidViewModel {

    private final LiveData<PagedList<AnalysisArticleJoin>> analysisRowsLiveData;

    public AnalysisArticleJoinsViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, AnalysisArticleJoin> factory = LocalDatabase.getInstance().analysisArticleDao().getAllAnalysisArticlesJoin();
        LivePagedListBuilder<Integer, AnalysisArticleJoin> pagedListBuilder = new LivePagedListBuilder<Integer, AnalysisArticleJoin>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<AnalysisArticleJoin>> getAnalysisArticleJoinLiveData() {
        return analysisRowsLiveData;
    }

}