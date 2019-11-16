package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

@Dao
public interface StoreDao extends _Dao<Store> {

    @Override
    @Query("SELECT COUNT(*) FROM stores")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM stores")
    LiveData<Integer> getNumberOfLiveData ();

    @Override
    @Query("DELETE FROM stores")
    int deleteAll();

    @Override
    @Query("SELECT * from stores ORDER BY name ASC")
    List<Store> getAll();

    @Override
    @Query("SELECT * from stores ORDER BY name ASC")
    LiveData<List<Store>> getAllLiveData();

    @Override
    @Query("SELECT * from stores ORDER BY name ASC")
    DataSource.Factory<Integer, Store> getAllPaged();

    @Override
    @RawQuery(observedEntities = Store.class)
    List<Store> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Store.class)
    LiveData<List<Store>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from stores WHERE id= :id")
    List<Store> findById(int id);

    @Override
    @Query("SELECT * FROM stores WHERE id= :id")
    LiveData<List<Store>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from stores WHERE name= :name")
    List<Store> findByName(String name);


}
