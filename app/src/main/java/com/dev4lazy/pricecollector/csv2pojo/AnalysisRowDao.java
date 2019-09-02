package com.dev4lazy.pricecollector.csv2pojo;

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
public interface AnalysisRowDao {

    @Query("SELECT COUNT(*) FROM analysis_rows")
    LiveData<Integer> getCountLiveData();

    @Query("SELECT COUNT(*) FROM analysis_rows")
    Integer getCountInteger();

    @Insert
    void insert(AnalysisRow analysisRow);

    @Update
    void update(AnalysisRow analysisRow);

    @Delete
    void delete(AnalysisRow analysisRow);

    @Query("DELETE FROM analysis_rows")
    void deleteAll();

    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    LiveData<List<AnalysisRow>> getAllAnalysisRows();

    @Query("SELECT * from analysis_rows ORDER BY articleCode ASC")
    DataSource.Factory<Integer, AnalysisRow> getAllAnalysisRowsPaged();

    @Query("SELECT * from analysis_rows WHERE id= :id")
    LiveData<List<AnalysisRow>> findAnalysisRowById(String id);

    //todo findByName like
    @RawQuery(observedEntities = AnalysisRow.class)
    LiveData<List<AnalysisRow>> getAnalysisRowsViaQuery(SupportSQLiteQuery query) ;
}
