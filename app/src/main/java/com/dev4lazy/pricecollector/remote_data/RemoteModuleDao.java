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
public interface RemoteModuleDao extends _Dao<RemoteModule> {

    @Override
    @Query( "SELECT COUNT(*) FROM modules" )
    Integer getNumberOf();

    @Override
    @Query( "SELECT COUNT(*) FROM modules " )
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM modules")
    int deleteAll();

    @Override
    @Query("SELECT * FROM modules ORDER BY id ASC" )
    List<RemoteModule> getAll();

    @Override
    @Query("SELECT * FROM modules ORDER BY id ASC" )
    LiveData<List<RemoteModule>> getAllLiveData();

    @Override
    @Query("SELECT * FROM modules ORDER BY id ASC" )
    DataSource.Factory<Integer, RemoteModule> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteModule.class)
    List<RemoteModule> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = RemoteModule.class)
    LiveData<List<RemoteModule>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM modules WHERE id= :id")
    List<RemoteModule> findById( int id );

    @Override
    @Query("SELECT * FROM modules WHERE id= :id")
    LiveData<List<RemoteModule>> findByIdLiveData(int id );

    @Override
    @Query("SELECT * FROM modules WHERE id= :name")
    List<RemoteModule> findByName(String name);

}
