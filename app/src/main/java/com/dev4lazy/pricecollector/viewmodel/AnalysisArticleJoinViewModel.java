package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinViewModel extends AndroidViewModel {

    private AnalysisArticleJoin analysisArticleJoin;
    private int AnalysisArticleJoinsRecyclerViewPosition;
    private boolean analysisArticleJoinNeedToSave;

    public AnalysisArticleJoinViewModel(Application application) {
        super(application);
        analysisArticleJoinNeedToSave = false;
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    public AnalysisArticleJoin getAnalysisArticleJoin() {
        return analysisArticleJoin;
    }

    public void setAnalysisArticleJoinsRecyclerViewPosition(int analysisArticleJoinsRecyclerViewPosition) {
        this.AnalysisArticleJoinsRecyclerViewPosition = analysisArticleJoinsRecyclerViewPosition;
    }

    public int getAnalysisArticleJoinsRecyclerViewPosition() {
        return AnalysisArticleJoinsRecyclerViewPosition;
    }

    public boolean isAnalysisArticleJoinNeedToSave() {
        return analysisArticleJoinNeedToSave;
    }

    public boolean isAnalysisArticleJoinNotModified() {
        return !analysisArticleJoinNeedToSave;
    }

    public void setAnalysisArticleJoinNeedToSave(boolean analysisArticleJoinNeedToSave) {
        this.analysisArticleJoinNeedToSave = analysisArticleJoinNeedToSave;
    }
}