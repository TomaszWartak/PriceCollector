package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface RemoteAnalysisRowDao {

    @Query("SELECT COUNT(*) FROM analysis_rows")
    LiveData<Integer> getCountLiveData();

    @Query("SELECT COUNT(*) FROM analysis_rows")
    Integer getCountInteger();

    @Insert
    void insert(RemoteAnalysisRow remoteAnalysisRow);

    @Update
    void update(RemoteAnalysisRow remoteAnalysisRow);

    @Delete
    void delete(RemoteAnalysisRow remoteAnalysisRow);

    @Query("DELETE FROM analysis_rows")
    void deleteAll();

    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    LiveData<List<RemoteAnalysisRow>> getAllAnalysisRows();

    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    DataSource.Factory<Integer, RemoteAnalysisRow> getAllAnalysisRowsPaged();

    @Query("SELECT * from analysis_rows WHERE id= :id")
    LiveData<List<RemoteAnalysisRow>> findAnalysisRowById(String id);

    //todo findByNameLD like
    @RawQuery(observedEntities = RemoteAnalysisRow.class)
    LiveData<List<RemoteAnalysisRow>> getAnalysisRowsViaQuery(SupportSQLiteQuery query);

    @Query(
            "SELECT " +
                    "analysis_rows.id, " +
                    "analysis_rows.articleName, " +
                    "analysis_rows.articleCode, " +
                    "ean_codes.value " +
                "FROM " +
                    "analysis_rows " +
                "INNER JOIN " +
                    "ean_codes ON article_id = articleCode "
    )
    LiveData<List<RemoteAnalysisRowJoin>> getAllRemoteAnalysisRowJoin();

}
