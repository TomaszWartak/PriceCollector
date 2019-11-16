package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Analysis;

import java.util.List;

@Dao
public interface AnalysisDao extends _Dao<Analysis> {

    @Override
    @Query( "SELECT COUNT(*) FROM analyzes " )
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM analyzes")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM analyzes ")
    int deleteAll();

    @Override
    @Query("SELECT * FROM analyzes  ORDER BY id ASC" )
    List<Analysis> getAll();

    @Override
    @Query("SELECT * FROM analyzes  ORDER BY id ASC" )
    LiveData<List<Analysis>> getAllLiveData();

    @Override
    @Query("SELECT * FROM analyzes  ORDER BY id ASC" )
    DataSource.Factory<Integer, Analysis> getAllPaged();

    @Override
    @RawQuery(observedEntities = Analysis.class)
    List<Analysis> getViaQuery( SimpleSQLiteQuery query );

    @Override
    @RawQuery(observedEntities = Analysis.class)
    LiveData<List<Analysis>> getViaQueryLiveData( SimpleSQLiteQuery query );

    @Override
    @Query("SELECT * FROM analyzes  WHERE id= :id")
    List<Analysis> findById( int id );

    @Override
    @Query("SELECT * FROM analyzes  WHERE id= :id")
    LiveData<List<Analysis>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * FROM analyzes  WHERE id= :name")
    List<Analysis> findByName( String name);

}
