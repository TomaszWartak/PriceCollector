package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;

import java.util.List;

@Dao
public interface OwnArticleInfoDao {
    @Insert
    void insert( OwnArticleInfo ownArticleInfo );

    @Update
    void update( OwnArticleInfo ownArticleInfo );

    @Delete
    void delete( OwnArticleInfo ownArticleInfo );

    @Query("DELETE FROM own_articles_infos")
    void deleteAll();

    @Query("SELECT * FROM own_articles_infos WHERE id= :id")
    LiveData<List<OwnArticleInfo>> findOwnArticleInfoById(String id);

    @Query("SELECT * FROM own_articles_infos")
    LiveData<List<OwnArticleInfo>> getAllOwnArticleInfos();

    @RawQuery(observedEntities = OwnArticleInfo.class)
    LiveData<List<OwnArticleInfo>> getOwnArticleInfosViaQuery( SupportSQLiteQuery query );
}
