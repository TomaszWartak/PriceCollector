package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.model.logic.LocalDataRepository;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class AnalysisArticleJoinsListViewModel extends AndroidViewModel {

    private final LiveData<PagedList<AnalysisArticleJoin>> analysisRowsLiveData;

    public AnalysisArticleJoinsListViewModel(Application application) {
        super(application);
        LocalDataRepository localDataRepository = AppHandle.getHandle().getRepository().getLocalDataRepository();
        DataSource.Factory<Integer, AnalysisArticleJoin> factory = localDataRepository.getAllAnalysisArticlesJoin();
        LivePagedListBuilder<Integer, AnalysisArticleJoin> pagedListBuilder = new LivePagedListBuilder<Integer, AnalysisArticleJoin>(factory, 50);
        analysisRowsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<AnalysisArticleJoin>> getAnalysisArticleJoinsListLiveData() {
        return analysisRowsLiveData;
    }

}