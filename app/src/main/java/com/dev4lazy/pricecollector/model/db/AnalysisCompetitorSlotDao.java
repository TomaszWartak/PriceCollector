package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;

import java.util.List;

@Dao
public interface AnalysisCompetitorSlotDao extends _Dao<AnalysisCompetitorSlot> {

    @Override
    @Query("SELECT COUNT(*) FROM competitor_slots")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM competitor_slots")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM competitor_slots")
    int deleteAll();

    @Override
    @Query("SELECT * from competitor_slots ORDER BY slot_nr ASC")
    List<AnalysisCompetitorSlot> getAll();

    @Override
    @Query("SELECT * from competitor_slots ORDER BY slot_nr ASC")
    LiveData<List<AnalysisCompetitorSlot>> getAllLiveData();

    @Override
    @Query("SELECT * from competitor_slots ORDER BY slot_nr ASC")
    DataSource.Factory<Integer, AnalysisCompetitorSlot> getAllPaged();

    @Override
    @RawQuery(observedEntities = AnalysisCompetitorSlot.class)
    List<AnalysisCompetitorSlot> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = AnalysisCompetitorSlot.class)
    LiveData<List<AnalysisCompetitorSlot>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from competitor_slots WHERE id= :id")
    List<AnalysisCompetitorSlot> findById(int id);

    @Override
    @Query("SELECT * FROM competitor_slots WHERE id= :id")
    LiveData<List<AnalysisCompetitorSlot>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from competitor_slots WHERE id= :name") // todo no nie wiem...
    List<AnalysisCompetitorSlot> findByName(String name);

}
