package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

@Dao
public interface StoreDao {
    @Insert
    void insert( Store store );

    @Update
    void update( Store store );

    @Delete
    void delete( Store store );

    @Query("DELETE FROM stores")
    void deleteAll();

    @Query("SELECT * FROM stores WHERE id= :id")
    LiveData<List<Store>> findStoreById(String id);

    @Query("SELECT * FROM stores")
    LiveData<List<Store>> getAllStores();

    @RawQuery(observedEntities = Store.class)
    LiveData<List<Store>> getStoresViaQuery( SupportSQLiteQuery query );
}
