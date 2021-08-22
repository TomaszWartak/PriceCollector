package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinViewModel extends AndroidViewModel {

    private AnalysisArticleJoin analysisArticleJoin;

    private int recyclerViewPosition;

    public AnalysisArticleJoinViewModel(Application application) {
        super(application);
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    public AnalysisArticleJoin getAnalysisArticleJoin() {
        return analysisArticleJoin;
    }

    public void setRecyclerViewPosition(int recyclerViewPosition) {
        this.recyclerViewPosition = recyclerViewPosition;
    }

    public int getRecyclerViewPosition() {
        return recyclerViewPosition;
    }

}