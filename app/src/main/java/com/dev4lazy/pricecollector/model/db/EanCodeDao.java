package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.EanCode;

import java.util.List;

@Dao
public interface EanCodeDao extends _Dao<EanCode>{

    @Override
    @Query("SELECT COUNT(*) FROM ean_codes")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM ean_codes")
    int deleteAll();

    @Override
    @Query("SELECT * from ean_codes ORDER BY id ASC")
    List<EanCode> getAll();

    @Override
    @Query("SELECT * from ean_codes ORDER BY id ASC")
    LiveData<List<EanCode>> getAllLiveData();

    @Override
    @Query("SELECT * FROM ean_codes ORDER BY id ASC")
    DataSource.Factory<Integer, EanCode> getAllPaged();

    @Override
    @RawQuery(observedEntities = EanCode.class)
    List<EanCode> getViaQuery(SimpleSQLiteQuery query);

    @Override
    @RawQuery(observedEntities = EanCode.class)
    LiveData<List<EanCode>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from ean_codes WHERE id= :id")
    List<EanCode> findById(int id);

    @Override
    @Query("SELECT * FROM ean_codes WHERE id= :id")
    LiveData<List<EanCode>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from ean_codes WHERE id= :dummy")
    List<EanCode> findByName(String dummy);

}
