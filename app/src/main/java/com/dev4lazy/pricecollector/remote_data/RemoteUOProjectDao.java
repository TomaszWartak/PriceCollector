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
public interface RemoteUOProjectDao extends _Dao<RemoteUOProject> {

    @Override
    @Query( "SELECT COUNT(*) FROM uo_projects" )
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM uo_projects")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM uo_projects")
    int deleteAll();

    @Override
    @Query("SELECT * FROM uo_projects ORDER BY id ASC" )
    List<RemoteUOProject> getAll();

    @Override
    @Query("SELECT * FROM uo_projects ORDER BY id ASC" )
    LiveData<List<RemoteUOProject>> getAllLiveData();

    @Override
    @Query("SELECT * FROM uo_projects ORDER BY id ASC" )
    DataSource.Factory<Integer, RemoteUOProject> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteUOProject.class)
    List<RemoteUOProject> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = RemoteUOProject.class)
    LiveData<List<RemoteUOProject>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM uo_projects WHERE id= :id")
    List<RemoteUOProject> findById( int id );

    @Override
    @Query("SELECT * FROM uo_projects WHERE id= :id")
    LiveData<List<RemoteUOProject>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * FROM uo_projects WHERE id= :name")
    List<RemoteUOProject> findByName( String name);

}