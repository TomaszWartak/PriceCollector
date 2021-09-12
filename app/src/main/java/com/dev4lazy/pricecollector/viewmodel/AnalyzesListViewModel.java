package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.model.db.AnalysisDao;
import com.dev4lazy.pricecollector.model.entities.Analysis;

public class AnalyzesListViewModel extends AndroidViewModel {

    private final LiveData<PagedList<Analysis>> analyzesLiveData;
    private int chosenAnalysisId = 0;

    public AnalyzesListViewModel(Application application) {
        super(application);
        AnalysisDao analysisDao = AppHandle.getHandle().getLocalDatabase().analysisDao();
        DataSource.Factory<Integer, Analysis>  factory = analysisDao.getAllPaged();
        LivePagedListBuilder<Integer, Analysis> pagedListBuilder = new LivePagedListBuilder<Integer, Analysis>(factory, 50);
        analyzesLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<Analysis>> getAnalyzesLiveData() {
        return analyzesLiveData;
    }

    public int getChosenAnalysisId() {
        return chosenAnalysisId;
    }

    public void setChosenAnalysisId(int chosenAnalysisId) {
        this.chosenAnalysisId = chosenAnalysisId;
    }
}
