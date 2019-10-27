package com.dev4lazy.pricecollector.remote_data;

import androidx.room.ColumnInfo;

public class RemoteAnalysisRowJoin {
    @ColumnInfo(name = "id")
    private int remoteAnalysisRowId;
    @ColumnInfo(name = "articleName")
    private String remoteAnalysisRowArticleName;
    @ColumnInfo(name = "articleCode")
    private int remoteAnalysisRowArticleCode;
    @ColumnInfo(name = "value")
    private String remoteEanCodeValue;

    public int getRemoteAnalysisRowId() {
        return remoteAnalysisRowId;
    }

    public void setRemoteAnalysisRowId(int remoteAnalysisRowId) {
        this.remoteAnalysisRowId = remoteAnalysisRowId;
    }

    public String getRemoteAnalysisRowArticleName() {
        return remoteAnalysisRowArticleName;
    }

    public void setRemoteAnalysisRowArticleName(String remoteAnalysisRowArticleName) {
        this.remoteAnalysisRowArticleName = remoteAnalysisRowArticleName;
    }

    public int getRemoteAnalysisRowArticleCode() {
        return remoteAnalysisRowArticleCode;
    }

    public void setRemoteAnalysisRowArticleCode(int remoteAnalysisRowArticleCode) {
        this.remoteAnalysisRowArticleCode = remoteAnalysisRowArticleCode;
    }

    public String getRemoteEanCodeValue() {
        return remoteEanCodeValue;
    }

    public void setRemoteEanCodeValue(String remoteEanCodeValue) {
        this.remoteEanCodeValue = remoteEanCodeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoteAnalysisRowJoin)) return false;
        RemoteAnalysisRowJoin that = (RemoteAnalysisRowJoin) o;
        return remoteAnalysisRowId == that.remoteAnalysisRowId;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
