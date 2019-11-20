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
public interface RemoteFamilyDao extends _Dao<RemoteFamily> {

    @Override
    @Query( "SELECT COUNT(*) FROM families " )
    Integer getNumberOf();

    @Override
    @Query( "SELECT COUNT(*) FROM families " )
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM families ")
    int deleteAll();

    @Override
    @Query("SELECT * FROM families  ORDER BY id ASC" )
    List<RemoteFamily> getAll();

    @Override
    @Query("SELECT * FROM families  ORDER BY id ASC" )
    LiveData<List<RemoteFamily>> getAllLiveData();

    @Override
    @Query("SELECT * FROM families  ORDER BY id ASC" )
    DataSource.Factory<Integer, RemoteFamily> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteFamily.class)
    List<RemoteFamily> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = RemoteFamily.class)
    LiveData<List<RemoteFamily>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM families  WHERE id= :id")
    List<RemoteFamily> findById( int id );

    @Override
    @Query("SELECT * FROM families  WHERE id= :id")
    LiveData<List<RemoteFamily>> findByIdLiveData(int id );

    @Override
    @Query("SELECT * FROM families  WHERE id= :name")
    List<RemoteFamily> findByName(String name);

}
