package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Article;

import java.util.List;

@Dao
public interface ArticleDao extends _Dao<Article> {

    @Override
    @Query("SELECT COUNT(*) FROM articles")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM articles")
    int deleteAll();

    @Override
    @Query("SELECT * FROM articles ORDER BY remote_id ASC")
    List<Article> getAll();

    @Override
    @Query("SELECT * FROM articles ORDER BY remote_id ASC")
    LiveData<List<Article>> getAllLiveData();

    @Override
    @Query("SELECT * FROM articles ORDER BY remote_id ASC")
    DataSource.Factory<Integer, Article> getAllPaged();

    @Override
    @RawQuery(observedEntities = Article.class)
    List<Article> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = Article.class)
    LiveData<List<Article>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM articles WHERE id= :id")
    List<Article> findById(int id);


    @Override
    @Query("SELECT * FROM articles WHERE id= :id")
    LiveData<List<Article>> findByIdLiveData( int id );

    // dummy method
    @Override
    @Query("SELECT * FROM articles WHERE id= :name")
    List<Article> findByName(String name);

}
