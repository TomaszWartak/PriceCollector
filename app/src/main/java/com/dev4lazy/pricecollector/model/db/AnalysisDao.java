package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import java.util.List;

@Dao
public interface AnalysisDao {
    @Insert
    void insert( Analysis analysis );

    @Update
    void update( Analysis analysis );

    @Delete
    void delete( Analysis analysis );

    @Query("DELETE FROM analyzes")
    void deleteAll();

    @Query("SELECT * FROM analyzes WHERE id= :id")
    LiveData<List<Analysis>> findAnalysisById( String id);

    @Query("SELECT * FROM analyzes")
    LiveData<List<Analysis>> getAllAnalyzes();

    @RawQuery(observedEntities = Analysis.class)
    LiveData<List<Analysis>> getAnalyzesViaQuery( SupportSQLiteQuery query );
}
