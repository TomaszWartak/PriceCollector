package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Market;

import java.util.List;

@Dao
public interface MarketDao extends _Dao<Market> {

    @Override
    @Query("SELECT COUNT(*) FROM markets")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM markets")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM markets")
    int deleteAll();

    @Override
    @Query("SELECT * from markets ORDER BY id ASC")
    List<Market> getAll();

    @Override
    @Query("SELECT * from markets ORDER BY id ASC")
    LiveData<List<Market>> getAllLiveData();

    @Override
    @Query("SELECT * FROM markets ORDER BY id ASC")
    DataSource.Factory<Integer, Market> getAllPaged();

    @Override
    @RawQuery(observedEntities = Market.class)
    List<Market> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Market.class)
    LiveData<List<Market>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from markets WHERE id= :id")
    List<Market> findById(int id);

    @Override
    @Query("SELECT * FROM markets WHERE id= :id")
    LiveData<List<Market>> findByIdLiveData(int id);

    @Override
    @Query("SELECT * from markets WHERE id= :dummy")
    List<Market> findByName(String dummy);
}
