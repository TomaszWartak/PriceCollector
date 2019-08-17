package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Market;

import java.util.List;

@Dao
public interface MarketDao {
    @Insert
    void insert( Market market );

    @Update
    void update( Market market );

    @Delete
    void delete( Market market );

    @Query("DELETE FROM markets")
    void deleteAll();

    @Query("SELECT * FROM markets WHERE id= :id")
    LiveData<List<Market>> findMarketById(String id);

    @Query("SELECT * FROM markets")
    LiveData<List<Market>> getAllMarkets();

    @RawQuery(observedEntities = Market.class)
    LiveData<List<Market>> getMarketsViaQuery( SupportSQLiteQuery query );
}
