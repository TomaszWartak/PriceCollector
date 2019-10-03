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

import com.dev4lazy.pricecollector.model.entities.Country;

import java.util.List;

@Dao
public interface CountryDao extends _Dao<Country>{

    @Override
    @Query("DELETE FROM countries")
    int deleteAll();

    @Override
    @Query("SELECT * from countries ORDER BY name ASC")
    List<Country> getAll();

    @Override
    @Query("SELECT * from countries ORDER BY name ASC")
    LiveData<List<Country>> getAllLiveData();

    @RawQuery(observedEntities = Country.class)
    LiveData<List<Country>> getViaQuery(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from countries WHERE id= :id")
    List<Country> findById(int id);

    @Override
    @Query("SELECT * from countries WHERE name= :name")
    List<Country> findByName(String name);

    @Query("SELECT * from countries ORDER BY name ASC")
    DataSource.Factory<Integer, Country> getAllCountriesPaged();


}
