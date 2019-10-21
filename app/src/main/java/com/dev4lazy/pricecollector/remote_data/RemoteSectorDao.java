package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteSectorDao extends _Dao<RemoteSector> {

    @Override
    @Query("SELECT COUNT(*) FROM sectors")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM sectors")
    int deleteAll();

    @Override
    @Query("SELECT * from sectors ORDER BY name ASC")
    List<RemoteSector> getAll();

    @Override
    @Query("SELECT * from sectors ORDER BY name ASC")
    LiveData<List<RemoteSector>> getAllLiveData();

    @Query("SELECT * from sectors ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteSector> getAllRemoteSectorsPaged();

    @RawQuery(observedEntities = RemoteSector.class)
    LiveData<List<RemoteSector>> getViaQueryLiveData(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from sectors WHERE id= :id")
    List<RemoteSector> findById(int id);

    @Override
    @Query("SELECT * from sectors WHERE name= :name")
    List<RemoteSector> findByName(String name);

}
