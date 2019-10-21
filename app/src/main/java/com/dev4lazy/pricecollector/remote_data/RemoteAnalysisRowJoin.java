package com.dev4lazy.pricecollector.remote_data;

import androidx.room.ColumnInfo;

public class RemoteAnalysisRowJoin {
    @ColumnInfo(name = "id")
    private int remoteAnalysisRowId;
    @ColumnInfo(name = "name")
    private String remoteAnalysisRowArticleName;
    @ColumnInfo(name = "code")
    private int remoteAnalysisRowArticleCode;
    @ColumnInfo(name = "ean")
    private String remoteEanCodeValue;
}
