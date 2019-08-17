package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.entities.Module;

import java.util.List;

@Dao
public interface ModuleDao {
    @Insert
    void insert( Module module );

    @Update
    void update( Module module );

    @Delete
    void delete( Module module );

    @Query("DELETE FROM modules")
    void deleteAll();

    @Query("SELECT * FROM modules WHERE id= :id")
    LiveData<List<Module>> findModuleById(String id);

    @Query("SELECT * FROM modules")
    LiveData<List<Module>> getAllModules();

    @RawQuery(observedEntities = Module.class)
    LiveData<List<Module>> getModulesViaQuery( SupportSQLiteQuery query );
}
