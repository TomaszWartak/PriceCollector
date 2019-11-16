package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.UOProject;

import java.util.List;

@Dao
public interface UOProjectDao extends _Dao<UOProject> {

    @Override
    @Query("SELECT COUNT(*) FROM uo_projects")
    Integer getNumberOf ();

    @Override
    @Query("SELECT COUNT(*) FROM uo_projects")
    LiveData<Integer> getNumberOfLiveData ();

    @Override
    @Query("DELETE FROM uo_projects")
    int deleteAll ();

    @Override
    @Query("SELECT * from uo_projects ORDER BY id ASC")
    List<UOProject> getAll ();

    @Override
    @Query("SELECT * from uo_projects ORDER BY id ASC")
    LiveData<List<UOProject>> getAllLiveData ();

    @Override
    @Query("SELECT * FROM uo_projects ORDER BY id ASC")
    DataSource.Factory<Integer, UOProject> getAllPaged ();

    @Override
    @RawQuery(observedEntities = UOProject.class)
    List<UOProject> getViaQuery (SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = UOProject.class)
    LiveData<List<UOProject>> getViaQueryLiveData (SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from uo_projects WHERE id= :id")
    List<UOProject> findById ( int id);

    @Override
    @Query("SELECT * FROM uo_projects WHERE id= :id")
    LiveData<List<UOProject>> findByIdLiveData ( int id);

    @Override
    @Query("SELECT * from uo_projects WHERE id= :dummy")
    List<UOProject> findByName (String dummy);

}
