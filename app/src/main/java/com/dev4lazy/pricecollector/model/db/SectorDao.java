package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Sector;

import java.util.List;

@Dao
public interface SectorDao extends _Dao<Sector> {

    @Override
    @Query("SELECT COUNT(*) FROM sectors")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM sectors")
    int deleteAll();

    @Override
    @Query("SELECT * from sectors ORDER BY name ASC")
    List<Sector> getAll();

    @Override
    @Query("SELECT * from sectors ORDER BY name ASC")
    LiveData<List<Sector>> getAllLiveData();

    @Override
    @Query("SELECT * FROM sectors ORDER BY id ASC")
    DataSource.Factory<Integer, Sector> getAllPaged();

    @Override
    @RawQuery(observedEntities = Sector.class)
    List<Sector> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Sector.class)
    LiveData<List<Sector>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from sectors WHERE id= :id")
    List<Sector> findById(int id);

    @Override
    @Query("SELECT * FROM sectors WHERE id= :id")
    LiveData<List<Sector>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from sectors WHERE name= :name")
    List<Sector> findByName(String name);
}
