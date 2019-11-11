package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Company;

import java.util.List;


@Dao
public interface CompanyDao extends _Dao<Company>{

    @Override
    @Query("SELECT COUNT(*) FROM companies")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM companies")
    int deleteAll();

    @Override
    @Query("SELECT * from companies ORDER BY name ASC")
    List<Company> getAll();

    @Override
    @Query("SELECT * from companies ORDER BY name ASC")
    LiveData<List<Company>> getAllLiveData();

    @Override
    @Query("SELECT * from companies ORDER BY name ASC")
    DataSource.Factory<Integer, Company> getAllPaged();

    @Override
    @RawQuery(observedEntities = Company.class)
    List<Company> getViaQuery(SimpleSQLiteQuery query);


    @Override
    @RawQuery(observedEntities = Company.class)
    LiveData<List<Company>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from companies WHERE id= :id")
    List<Company> findById(int id);

    @Override
    @Query("SELECT * FROM companies WHERE id= :id")
    LiveData<List<Company>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from companies WHERE name= :name")
    List<Company> findByName(String name);

}
