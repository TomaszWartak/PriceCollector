package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.EanCode;

import java.util.List;

@Dao
public interface EanCodeDao {
    @Insert
    void insert( EanCode eanCode );

    @Update
    void update( EanCode eanCode );

    @Delete
    void delete( EanCode eanCode );

    @Query("DELETE FROM ean_codes")
    void deleteAll();

    @Query("SELECT * FROM ean_codes WHERE id= :id")
    LiveData<List<EanCode>> findEanCodeById(String id);

    @Query("SELECT * FROM ean_codes")
    LiveData<List<EanCode>> getAllEanCodes();

    @RawQuery(observedEntities = EanCode.class)
    LiveData<List<EanCode>> getEanCodesViaQuery( SupportSQLiteQuery query );
}
