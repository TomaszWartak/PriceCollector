package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Country;

import java.util.List;

@Dao
public interface _Dao<D> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Long insert(D data);

    @Update
    int update(D data);

    @Delete
    int delete(D data);

    int deleteAll();

    List<D> getAll();

    LiveData<List<D>> getAllLiveData();

    List<D> findById(int id);

    List<D> findByName(String name);

    @RawQuery
    List<D> getViaQuery(SimpleSQLiteQuery query);

}
