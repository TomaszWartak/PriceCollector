package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Department;

import java.util.List;

@Dao
public interface DepartmentDao extends _Dao<Department> {

    @Override
    @Query("SELECT COUNT(*) FROM departments")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM departments")
    int deleteAll();

    @Override
    @Query("SELECT * from departments ORDER BY name ASC")
    List<Department> getAll();

    @Override
    @Query("SELECT * from departments ORDER BY name ASC")
    LiveData<List<Department>> getAllLiveData();

    @Override
    @Query("SELECT * FROM departments ORDER BY name ASC")
    DataSource.Factory<Integer, Department> getAllPaged();

    @Override
    @RawQuery(observedEntities = Department.class)
    List<Department> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Department.class)
    LiveData<List<Department>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from departments WHERE id= :id")
    List<Department> findById(int id);

    @Override
    @Query("SELECT * FROM departments WHERE id= :id")
    LiveData<List<Department>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from departments WHERE name= :name")
    List<Department> findByName(String name);
    
}