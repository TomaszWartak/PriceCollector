package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteDepartmentDao extends _Dao<RemoteDepartment> {

    @Override
    @Query("SELECT COUNT(*) FROM departments")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM departments")
    int deleteAll();

    @Override
    @Query("SELECT * from departments ORDER BY name ASC")
    List<RemoteDepartment> getAll();

    @Override
    @Query("SELECT * from departments ORDER BY name ASC")
    LiveData<List<RemoteDepartment>> getAllLiveData();

    @Query("SELECT * from departments ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteDepartment> getAllRemoteDepartmentsPaged();

    @RawQuery(observedEntities = RemoteDepartment.class)
    LiveData<List<RemoteDepartment>> getViaQueryLiveData(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from departments WHERE id= :id")
    List<RemoteDepartment> findById(int id);

    @Override
    @Query("SELECT * from departments WHERE name= :name")
    List<RemoteDepartment> findByName(String name);

}
