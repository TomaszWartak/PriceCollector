package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.entities.AnalysisCompetitorSlot;
import com.dev4lazy.pricecollector.model.entities.Country;

import java.util.List;

@Dao
public interface AnalysisCompetitorSlotDao extends _Dao<AnalysisCompetitorSlot> {
    @Override
    @Query("DELETE FROM competitor_slots")
    int deleteAll();

    @Override
    @Query("SELECT * from competitor_slots ORDER BY slot_nr ASC")
    LiveData<List<AnalysisCompetitorSlot>> getAll();

    @RawQuery(observedEntities = AnalysisCompetitorSlot.class)
    LiveData<List<AnalysisCompetitorSlot>> getViaQuery(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from competitor_slots WHERE id= :id")
    List<AnalysisCompetitorSlot> findById(int id);

    @Query("SELECT * from competitor_slots ORDER BY slot_nr ASC")
    DataSource.Factory<Integer, AnalysisCompetitorSlot> getAllSlotsPaged();
}
