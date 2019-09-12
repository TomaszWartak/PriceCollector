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
public interface StoreDao extends _Dao<Store> {
    @Override
    @Query("DELETE FROM stores")
    int deleteAll();

    @Override
    @Query("SELECT * from stores ORDER BY name ASC")
    LiveData<List<Store>> getAll();

    @RawQuery(observedEntities = Store.class)
    LiveData<List<Store>> getViaQuery(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from stores WHERE id= :id")
    List<Store> findById(int id);

    @Query("SELECT * from stores WHERE name= :name")
    List<Store> findByName(String name);
}
