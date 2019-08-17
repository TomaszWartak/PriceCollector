package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Department;

import java.util.List;

@Dao
public interface DepartmentDao {
    @Insert
    void insert( Department department );

    @Update
    void update( Department department );

    @Delete
    void delete( Department department );

    @Query("DELETE FROM departments")
    void deleteAll();

    @Query("SELECT * FROM departments WHERE id= :id")
    LiveData<List<Department>> findDepartmentById(String id);

    @Query("SELECT * FROM departments")
    LiveData<List<Department>> getAllDepartments();

    @RawQuery(observedEntities = Department.class)
    LiveData<List<Department>> getDepartmentsViaQuery( SupportSQLiteQuery query );
}