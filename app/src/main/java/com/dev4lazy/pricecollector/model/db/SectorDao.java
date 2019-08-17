package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Sector;

import java.util.List;

@Dao
public interface SectorDao {
    @Insert
    void insert( Sector sector );

    @Update
    void update( Sector sector );

    @Delete
    void delete( Sector sector );

    @Query("DELETE FROM sectors")
    void deleteAll();

    @Query("SELECT * FROM sectors WHERE id= :id")
    LiveData<List<Sector>> findSectorById(String id);

    @Query("SELECT * FROM sectors")
    LiveData<List<Sector>> getAllSectors();

    @RawQuery(observedEntities = Sector.class)
    LiveData<List<Sector>> getSectorsViaQuery( SupportSQLiteQuery query );
}
