package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Family;

import java.util.List;

@Dao
public interface FamilyDao extends _Dao<Family> {

    @Override
    @Query("SELECT COUNT(*) FROM families")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM families")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM families")
    int deleteAll();

    @Override
    @Query("SELECT * from families ORDER BY id ASC")
    List<Family> getAll();

    @Override
    @Query("SELECT * from families ORDER BY id ASC")
    LiveData<List<Family>> getAllLiveData();

    @Override
    @Query("SELECT * FROM families ORDER BY id ASC")
    DataSource.Factory<Integer, Family> getAllPaged();

    @Override
    @RawQuery(observedEntities = Family.class)
    List<Family> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Family.class)
    LiveData<List<Family>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from families WHERE id= :id")
    List<Family> findById(int id);

    @Override
    @Query("SELECT * FROM families WHERE id= :id")
    LiveData<List<Family>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from families WHERE id= :dummy")
    List<Family> findByName(String dummy);

}
