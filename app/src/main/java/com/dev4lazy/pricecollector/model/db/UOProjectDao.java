package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.UOProject;

import java.util.List;

@Dao
public interface UOProjectDao {
    @Insert
    void insert( UOProject uOProject );

    @Update
    void update( UOProject uOProject );

    @Delete
    void delete( UOProject uOProject );

    @Query("DELETE FROM uo_projects")
    void deleteAll();

    @Query("SELECT * FROM uo_projects WHERE id= :id")
    LiveData<List<UOProject>> findUOProjectById(String id);

    @Query("SELECT * FROM uo_projects")
    LiveData<List<UOProject>> getAllUOProjects();

    @RawQuery(observedEntities = UOProject.class)
    LiveData<List<UOProject>> getUOProjectsViaQuery( SupportSQLiteQuery query );
}
