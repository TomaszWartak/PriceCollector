package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface _Dao<D> {

    Integer getNumberOf();

    @RawQuery
    Integer getNumberOfViaQuery( SimpleSQLiteQuery query );

    LiveData<Integer> getNumberOfLiveData();

    @Insert( onConflict = OnConflictStrategy.IGNORE ) // <-- domyślnie jest OnConflictStrategy.ABORT
    /**
     * Zwraca id zapisanej danej
     */
    Long insert( D data );

    @Update
    /**
     * Zwraca ilość zaktualizowanych wierszy
     */
    int update( D data );

    @Delete
    /**
     * Zwraca ilość usuniętych wierszy
     */
    int delete( D data );

    /*
    @Delete
    int deleteById(int id);
    */

    int deleteAll();

    List<D> getAll();

    LiveData<List<D>> getAllLiveData();

    DataSource.Factory<Integer, D> getAllPaged();

    List<D> getViaQuery( SimpleSQLiteQuery query );

    LiveData<List<D>> getViaQueryLiveData( SimpleSQLiteQuery query );

    List<D> findById(int id);

    LiveData<List<D>> findByIdLiveData( int id );

    List<D> findByName( String name );

}
