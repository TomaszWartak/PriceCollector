package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Module;

import java.util.List;

@Dao
public interface ModuleDao extends _Dao<Module> {

    @Override
    @Query("SELECT COUNT(*) FROM modules")
        Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM modules")
        LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM modules")
        int deleteAll();

    @Override
    @Query("SELECT * from modules ORDER BY id ASC")
        List<Module> getAll();

    @Override
    @Query("SELECT * from modules ORDER BY id ASC")
        LiveData<List<Module>> getAllLiveData();

    @Override
    @Query("SELECT * FROM modules ORDER BY id ASC")
        DataSource.Factory<Integer, Module> getAllPaged();

    @Override
    @RawQuery(observedEntities = Module.class)
        List<Module> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Module.class)
        LiveData<List<Module>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from modules WHERE id= :id")
        List<Module> findById(int id);

    @Override
    @Query("SELECT * FROM modules WHERE id= :id")
        LiveData<List<Module>> findByIdLiveData(int id);

    @Override
    @Query("SELECT * from modules WHERE id= :dummy")
        List<Module> findByName(String dummy);

}
