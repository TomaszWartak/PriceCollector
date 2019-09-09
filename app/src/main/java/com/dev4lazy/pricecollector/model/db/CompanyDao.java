package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Company;

import java.util.List;


@Dao
public interface CompanyDao extends _Dao<Company>{

    @Override
    @Query("DELETE FROM companies")
    int deleteAll();

    @Override
    @Query("SELECT * from companies ORDER BY name ASC")
    LiveData<List<Company>> getAll();

    @Override
    @Query("SELECT * from companies WHERE id= :id")
    LiveData<List<Company>> findByIdLD(int id);

    @Override
    @Query("SELECT * from companies WHERE id= :id")
    List<Company> findById(int id);

    @Query("SELECT * from companies WHERE name= :name")
    LiveData<List<Company>> findByNameLD(String name);

    @Query("SELECT * from companies WHERE name= :name")
    List<Company> findByName(String name);

    // todo zrób Override analogicznie
    @RawQuery(observedEntities = Company.class)
    LiveData<List<Company>> getViaQuery(SupportSQLiteQuery query) ;

}
