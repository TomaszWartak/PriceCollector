package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.OwnArticleInfo;

import java.util.List;

@Dao
public interface OwnArticleInfoDao extends _Dao<OwnArticleInfo>{

    @Override
    @Query("SELECT COUNT(*) FROM own_articles_infos")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM own_articles_infos")
    LiveData<Integer> getNumberOfLiveData ();

    @Override
    @Query("DELETE FROM own_articles_infos")
    int deleteAll();

    @Override
    @Query("SELECT * FROM own_articles_infos ORDER BY ownCode ASC")
    List<OwnArticleInfo> getAll();

    @Override
    @Query("SELECT * FROM own_articles_infos ORDER BY ownCode ASC")
    LiveData<List<OwnArticleInfo>> getAllLiveData();

    @Override
    @Query("SELECT * FROM own_articles_infos ORDER BY ownCode ASC")
    DataSource.Factory<Integer, OwnArticleInfo> getAllPaged();

    @Override
    @RawQuery(observedEntities = OwnArticleInfo.class)
    List<OwnArticleInfo> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = OwnArticleInfo.class)
    LiveData<List<OwnArticleInfo>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * FROM own_articles_infos WHERE id= :id")
    List<OwnArticleInfo> findById(int id);

    @Override
    @Query("SELECT * FROM own_articles_infos WHERE id= :id")
    LiveData<List<OwnArticleInfo>> findByIdLiveData( int id );

    // dummy method
    @Override
    @Query("SELECT * FROM own_articles_infos WHERE id= :name")
    List<OwnArticleInfo> findByName(String name);

}
