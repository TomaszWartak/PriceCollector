package com.dev4lazy.pricecollector.remote_model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRowJoin;

import java.util.List;

@Dao
public interface RemoteAnalysisRowDao extends _Dao<RemoteAnalysisRow> {

    @Override
    @Query("SELECT COUNT(*) FROM analysis_rows")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM analysis_rows")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM analysis_rows")
    int deleteAll();

    @Override
    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    List<RemoteAnalysisRow> getAll();

    @Override
    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    LiveData<List<RemoteAnalysisRow>> getAllLiveData();

    @Override
    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    DataSource.Factory<Integer, RemoteAnalysisRow> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteAnalysisRow.class)
    List<RemoteAnalysisRow> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = RemoteAnalysis.class)
    LiveData<List<RemoteAnalysisRow>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from analysis_rows WHERE id= :id")
    List<RemoteAnalysisRow> findById(int id);

    @Override
    @Query("SELECT * from analysis_rows WHERE id= :id")
    LiveData<List<RemoteAnalysisRow>> findByIdLiveData( int id );

    // dummy method
    @Override
    @Query("SELECT * from analysis_rows WHERE id= :name")
    List<RemoteAnalysisRow> findByName(String name);

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
    DataSource.Factory<Integer, RemoteAnalysisRowJoin> getAllRemoteAnalysisRowJoinPaged();

}
