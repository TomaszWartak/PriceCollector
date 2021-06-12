package com.dev4lazy.pricecollector.unused;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "analysis_articles_in_comp_store" )
public class AnalysisArticleShortData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "analysis_id")
    private int analysisId;
    @ColumnInfo(name = "article_id")
    private int articleId;
    @ColumnInfo(name = "competitor_store_id")
    private int competitorStoreId;
    private boolean completed = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getCompetitorStoreId() {
        return competitorStoreId;
    }

    public void setCompetitorStoreId(int competitorStoreId) {
        this.competitorStoreId = competitorStoreId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

