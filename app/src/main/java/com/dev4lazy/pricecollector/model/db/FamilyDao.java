package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Family;

import java.util.List;

@Dao
public interface FamilyDao {
    @Insert
    void insert( Family family );

    @Update
    void update( Family family );

    @Delete
    void delete( Family family );

    @Query("DELETE FROM families")
    void deleteAll();

    @Query("SELECT * FROM families WHERE id= :id")
    LiveData<List<Family>> findFamilyById( String id);

    @Query("SELECT * FROM families")
    LiveData<List<Family>> getAllFamilies();

    @RawQuery(observedEntities = Family.class)
    LiveData<List<Family>> getFamiliesViaQuery( SupportSQLiteQuery query );
}
