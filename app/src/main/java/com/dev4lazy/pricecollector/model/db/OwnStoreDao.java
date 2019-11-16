package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.OwnStore;

import java.util.List;

@Dao
public interface OwnStoreDao extends _Dao<OwnStore> {

    @Override
    @Query("SELECT COUNT(*) FROM own_stores")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM own_stores")
    LiveData<Integer> getNumberOfLiveData ();

    @Override
    @Query("DELETE FROM own_stores")
    int deleteAll();

    @Override
    @Query("SELECT * from own_stores ORDER BY name ASC")
    List<OwnStore> getAll();

    @Override
    @Query("SELECT * from own_stores ORDER BY name ASC")
    LiveData<List<OwnStore>> getAllLiveData();

    @Override
    @Query("SELECT * from own_stores ORDER BY name ASC")
    DataSource.Factory<Integer, OwnStore> getAllPaged();

    @Override
    @RawQuery(observedEntities = OwnStore.class)
    List<OwnStore> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = OwnStore.class)
    LiveData<List<OwnStore>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from own_stores WHERE id= :id")
    List<OwnStore> findById(int id);

    @Override
    @Query("SELECT * FROM own_stores WHERE id= :id")
    LiveData<List<OwnStore>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from own_stores WHERE name= :name")
    List<OwnStore> findByName(String name);

 }
