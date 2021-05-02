package com.dev4lazy.pricecollector.remote_model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysis;

import java.util.List;

@Dao
public interface RemoteAnalysisDao extends _Dao<RemoteAnalysis> {

    @Override
    @Query("SELECT COUNT(*) FROM analyzes")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM analyzes")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM analyzes")
    int deleteAll();

    @Override
    @Query("SELECT * FROM analyzes ORDER BY id ASC")
    List<RemoteAnalysis> getAll();

    @Override
    @Query("SELECT * FROM analyzes ORDER BY id ASC")
    LiveData<List<RemoteAnalysis>> getAllLiveData();

    @Override
    @Query("SELECT * FROM analyzes ORDER BY id ASC")
    DataSource.Factory<Integer, RemoteAnalysis> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteAnalysis.class)
    List<RemoteAnalysis> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = RemoteAnalysis.class)
    LiveData<List<RemoteAnalysis>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM analyzes WHERE id= :id")
    List<RemoteAnalysis> findById(int id);

    @Override
    @Query("SELECT * FROM analyzes WHERE id= :id")
    LiveData<List<RemoteAnalysis>> findByIdLiveData( int id );

    // dummy method
    @Override
    @Query("SELECT * FROM analyzes WHERE id= :name")
    List<RemoteAnalysis> findByName(String name);

    /*
    // dummy method
    @Query("SELECT * FROM analyzes WHERE id= :value")
    List<RemoteAnalysis> findByValue(String value);

    // dummy method
    @Query("SELECT * FROM analyzes WHERE id= :articleId")
    List<RemoteAnalysis> findByArticleId(Integer articleId);
    */

}
