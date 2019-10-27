package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteAnalysisDao extends _Dao<RemoteAnalysis> {

    @Override
    @Query("SELECT COUNT(*) FROM analyzes")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM analyzes")
    int deleteAll();

    @Override
    @Query("SELECT * from analyzes ORDER BY id ASC")
    List<RemoteAnalysis> getAll();

    @Override
    @Query("SELECT * from analyzes ORDER BY id ASC")
    LiveData<List<RemoteAnalysis>> getAllLiveData();

    @Query("SELECT * from analyzes ORDER BY id ASC")
    DataSource.Factory<Integer, RemoteAnalysis> getAllRemoteAnalysissPaged();

    @RawQuery(observedEntities = RemoteAnalysis.class)
    LiveData<List<RemoteAnalysis>> getViaQueryLiveData(SupportSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = RemoteAnalysis.class)
    List<RemoteAnalysis> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from analyzes WHERE id= :id")
    List<RemoteAnalysis> findById(int id);

    // dummy method
    @Override
    @Query("SELECT * from analyzes WHERE id= :name")
    List<RemoteAnalysis> findByName(String name);

    // dummy method
    @Query("SELECT * from analyzes WHERE id= :value")
    List<RemoteAnalysis> findByValue(String value);

    // dummy method
    @Query("SELECT * from analyzes WHERE id= :articleId")
    List<RemoteAnalysis> findByArticleId(Integer articleId);

}
