package com.dev4lazy.pricecollector.remote_model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;

import java.util.List;

@Dao
public interface RemoteSectorDao extends _Dao<RemoteSector> {

    @Override
    @Query("SELECT COUNT(*) FROM sectors")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM sectors")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM sectors")
    int deleteAll();

    @Override
    @Query("SELECT * FROM sectors ORDER BY name ASC")
    List<RemoteSector> getAll();

    @Override
    @Query("SELECT * FROM sectors ORDER BY name ASC")
    LiveData<List<RemoteSector>> getAllLiveData();

    @Override
    @Query("SELECT * FROM sectors ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteSector> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteSector.class)
    List<RemoteSector> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = RemoteSector.class)
    LiveData<List<RemoteSector>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM sectors WHERE id= :id")
    List<RemoteSector> findById(int id);

    @Override
    @Query("SELECT * FROM sectors WHERE id= :id")
    LiveData<List<RemoteSector>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * FROM sectors WHERE name= :name")
    List<RemoteSector> findByName(String name);

}
