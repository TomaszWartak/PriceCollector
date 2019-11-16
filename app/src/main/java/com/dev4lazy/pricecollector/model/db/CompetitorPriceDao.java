package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.CompetitorPrice;

import java.util.List;

@Dao
public interface CompetitorPriceDao extends _Dao<CompetitorPrice> {

    @Override
    @Query( "SELECT COUNT(*) FROM competitors_prices" )
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM competitors_prices")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM competitors_prices")
    int deleteAll();

    @Override
    @Query("SELECT * FROM competitors_prices ORDER BY id ASC")
    List<CompetitorPrice> getAll();

    @Override
    @Query("SELECT * FROM competitors_prices ORDER BY id ASC")
    LiveData<List<CompetitorPrice>> getAllLiveData();

    @Override
    @Query("SELECT * FROM competitors_prices ORDER BY id ASC")
    DataSource.Factory<Integer, CompetitorPrice> getAllPaged();

    @Override
    @RawQuery(observedEntities = CompetitorPrice.class)
    List<CompetitorPrice> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = CompetitorPrice.class)
    LiveData<List<CompetitorPrice>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM competitors_prices WHERE id= :id")
    List<CompetitorPrice> findById(int id);

    @Override
    @Query("SELECT * FROM competitors_prices WHERE id= :id")
    LiveData<List<CompetitorPrice>> findByIdLiveData(int id);

    @Override
    @Query("SELECT * FROM competitors_prices WHERE id= :name")
    List<CompetitorPrice> findByName( String name);

}
