package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.OwnStore;

import java.util.List;

@Dao
public interface OwnStoreDao {
    @Insert
    void insert( OwnStore ownStore );

    @Update
    void update( OwnStore ownStore );

    @Delete
    void delete( OwnStore ownStore );

    @Query("DELETE FROM own_stores")
    void deleteAll();

    @Query("SELECT * FROM own_stores WHERE id= :id")
    LiveData<List<OwnStore>> findOwnStoreById(String id);

    @Query("SELECT * FROM own_stores")
    LiveData<List<OwnStore>> getAllOwnStores();

    @RawQuery(observedEntities = OwnStore.class)
    LiveData<List<OwnStore>> getOwnStoresViaQuery( SupportSQLiteQuery query );
}
