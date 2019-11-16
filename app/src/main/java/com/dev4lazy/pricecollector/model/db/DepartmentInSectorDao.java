package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;

import java.util.List;

@Dao
public interface DepartmentInSectorDao extends _Dao<DepartmentInSector> {

    @Override
    @Query("SELECT COUNT(*) FROM departments_in_sector")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM departments_in_sector")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM departments_in_sector")
    int deleteAll();

    @Override
    @Query("SELECT * from departments_in_sector ORDER BY id ASC")
    List<DepartmentInSector> getAll();

    @Override
    @Query("SELECT * from departments_in_sector ORDER BY id ASC")
    LiveData<List<DepartmentInSector>> getAllLiveData();

    @Override
    @Query("SELECT * FROM departments_in_sector ORDER BY id ASC")
    DataSource.Factory<Integer, DepartmentInSector> getAllPaged();

    @Override
    @RawQuery(observedEntities = DepartmentInSector.class)
    List<DepartmentInSector> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = DepartmentInSector.class)
    LiveData<List<DepartmentInSector>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from departments_in_sector WHERE id= :id")
    List<DepartmentInSector> findById(int id);

    @Override
    @Query("SELECT * FROM departments_in_sector WHERE id= :id")
    LiveData<List<DepartmentInSector>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from departments_in_sector WHERE id= :name")
    List<DepartmentInSector> findByName(String name);

}
