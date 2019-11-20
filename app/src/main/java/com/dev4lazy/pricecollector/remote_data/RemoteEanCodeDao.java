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
public interface RemoteEanCodeDao extends _Dao<RemoteEanCode> {

    @Override
    @Query("SELECT COUNT(*) FROM ean_codes")
    Integer getNumberOf();

    @Override
    @Query("SELECT COUNT(*) FROM ean_codes")
    LiveData<Integer> getNumberOfLiveData();

    @Override
    @Query("DELETE FROM ean_codes")
    int deleteAll();

    @Override
    @Query("SELECT * from ean_codes ORDER BY value ASC")
    List<RemoteEanCode> getAll();

    @Override
    @Query("SELECT * from ean_codes ORDER BY value ASC")
    LiveData<List<RemoteEanCode>> getAllLiveData();

    @Override
    @Query("SELECT * from ean_codes ORDER BY value ASC")
    DataSource.Factory<Integer, RemoteEanCode> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteEanCode.class)
    List<RemoteEanCode> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = RemoteEanCode.class)
    LiveData<List<RemoteEanCode>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from ean_codes WHERE id= :id")
    List<RemoteEanCode> findById(int id);

    @Override
    @Query("SELECT * from ean_codes WHERE id= :id")
    LiveData<List<RemoteEanCode>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from ean_codes WHERE id= :name")
    List<RemoteEanCode> findByName(String name);

    /* todo
    @Query("SELECT * from ean_codes WHERE value= :value")
    List<RemoteEanCode> findByValue(String value);

    @Query("SELECT * from ean_codes WHERE article_id= :articleId")
    List<RemoteEanCode> findByArticleId(Integer articleId);

     */

}
