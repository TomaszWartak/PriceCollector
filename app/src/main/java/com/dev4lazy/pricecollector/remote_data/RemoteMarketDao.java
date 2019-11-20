package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteMarketDao extends _Dao<RemoteMarket> {

    @Override
    @Query( "SELECT COUNT(*) FROM markets" )
    Integer getNumberOf();

    @Override
    @Query( "SELECT COUNT(*) FROM markets " )
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM markets")
    int deleteAll();

    @Override
    @Query("SELECT * FROM markets ORDER BY id ASC" )
    List<RemoteMarket> getAll();

    @Override
    @Query("SELECT * FROM markets ORDER BY id ASC" )
    LiveData<List<RemoteMarket>> getAllLiveData();

    @Override
    @Query("SELECT * FROM markets ORDER BY id ASC" )
    DataSource.Factory<Integer, RemoteMarket> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteMarket.class)
    List<RemoteMarket> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = RemoteMarket.class)
    LiveData<List<RemoteMarket>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM markets WHERE id= :id")
    List<RemoteMarket> findById( int id );

    @Override
    @Query("SELECT * FROM markets WHERE id= :id")
    LiveData<List<RemoteMarket>> findByIdLiveData(int id );

    @Override
    @Query("SELECT * FROM markets WHERE id= :name")
    List<RemoteMarket> findByName(String name);

}
