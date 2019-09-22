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

import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;

import java.util.List;

@Dao
public interface OwnStoreDao extends _Dao<OwnStore> {

    @Override
    @Query("DELETE FROM own_stores")
    int deleteAll();

    @Override
    @Query("SELECT * from own_stores ORDER BY name ASC")
    LiveData<List<OwnStore>> getAll();

    @RawQuery(observedEntities = OwnStore.class)
    LiveData<List<OwnStore>> getViaQuery(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from own_stores WHERE id= :id")
    List<OwnStore> findById(int id);

    @Query("SELECT * from own_stores WHERE name= :name")
    List<OwnStore> findByName(String name);

    @Query("SELECT * from own_stores ORDER BY name ASC")
    DataSource.Factory<Integer, OwnStore> getAllOwnStoresPaged();

}
