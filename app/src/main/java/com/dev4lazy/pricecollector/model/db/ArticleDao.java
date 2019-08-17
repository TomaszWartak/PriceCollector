package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert
    void insert( Article article );

    @Update
    void update( Article article );

    @Delete
    void delete( Article article );

    @Query("DELETE FROM articles")
    void deleteAll();

    @Query("SELECT * FROM articles WHERE id= :id")
    LiveData<List<Article>> findArticleById(String id);

    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getAllArticles();

    @RawQuery(observedEntities = Article.class)
    LiveData<List<Article>> getArticlesViaQuery( SupportSQLiteQuery query );
}
