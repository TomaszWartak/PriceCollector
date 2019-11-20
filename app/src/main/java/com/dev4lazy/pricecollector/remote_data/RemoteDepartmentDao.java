package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteDepartmentDao extends _Dao<RemoteDepartment> {

    @Override
    @Query("SELECT COUNT(*) FROM departments")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM departments")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM departments")
    int deleteAll();

    @Override
    @Query("SELECT * FROM departments ORDER BY name ASC")
    List<RemoteDepartment> getAll();

    @Override
    @Query("SELECT * FROM departments ORDER BY name ASC")
    LiveData<List<RemoteDepartment>> getAllLiveData();

    @Override
    @Query("SELECT * FROM departments ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteDepartment> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteDepartment.class)
    List<RemoteDepartment> getViaQuery(SimpleSQLiteQuery query);

    @RawQuery(observedEntities = RemoteDepartment.class)
    LiveData<List<RemoteDepartment>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM departments WHERE id= :id")
    List<RemoteDepartment> findById(int id);

    @Override
    @Query("SELECT * FROM departments WHERE id= :id")
    LiveData<List<RemoteDepartment>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * FROM departments WHERE name= :name")
    List<RemoteDepartment> findByName(String name);

}
