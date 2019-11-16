package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Country;

import java.util.List;

@Dao
public interface CountryDao extends _Dao<Country>{

    @Override
    @Query("SELECT COUNT(*) FROM countries")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM countries")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM countries")
    int deleteAll();

    @Override
    @Query("SELECT * FROM countries ORDER BY name ASC")
    List<Country> getAll();

    @Override
    @Query("SELECT * FROM countries ORDER BY name ASC")
    LiveData<List<Country>> getAllLiveData();

    @Query("SELECT * FROM countries ORDER BY name ASC")
    DataSource.Factory<Integer, Country> getAllPaged();

    @Override
    @RawQuery(observedEntities = Country.class)
    List<Country> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Country.class)
    LiveData<List<Country>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM countries WHERE id= :id")
    List<Country> findById(int id);

    @Override
    @Query("SELECT * FROM countries WHERE id= :id")
    LiveData<List<Country>> findByIdLiveData( int id );

    // dummy method?
    @Override
    @Query("SELECT * FROM countries WHERE name= :name")
    List<Country> findByName(String name);

}
